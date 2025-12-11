package com.inspur.seed.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.common.exception.ServiceException;
import com.inspur.common.utils.LoginHelper;
import com.inspur.common.utils.StringUtils;
import com.inspur.common.utils.uuid.IdUtils;
import com.inspur.seed.domain.EnterpriseInfo;
import com.inspur.seed.domain.VarietyAudit;
import com.inspur.seed.domain.VarietyRegistration;
import com.inspur.seed.mapper.VarietyRegistrationMapper;
import com.inspur.seed.service.IEnterpriseCertifyService;
import com.inspur.seed.service.IVarietyAuditService;
import com.inspur.seed.service.IVarietyRegistrationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

/**
 * 品种登记服务实现类
 *
 * @author system
 */
@Service
public class VarietyRegistrationServiceImpl extends ServiceImpl<VarietyRegistrationMapper, VarietyRegistration> implements IVarietyRegistrationService {

    @Resource
    private IEnterpriseCertifyService enterpriseCertifyService;

    @Resource
    private IVarietyAuditService varietyAuditService;

    @Override
    public String submitRegistration(VarietyRegistration varietyRegistration) {
        // 查询企业信息（仅用于填充操作机构等信息，不进行认证检查）
        // 注释原因：育种许可数据录入时企业可能还未完成认证，允许使用默认企业ID
        EnterpriseInfo enterpriseInfo = null;
        if (StringUtils.isNotEmpty(varietyRegistration.getEnterpriseId())) {
            enterpriseInfo = enterpriseCertifyService.queryByEnterpriseId(varietyRegistration.getEnterpriseId());
            // 已去除：企业信息存在性检查和企业认证状态检查
            // 原因：育种许可录入不依赖企业认证，可使用"UNKNOWN"等默认企业ID
        }

        String registrationId = varietyRegistration.getRegistrationId();

        // 判断是否有 registrationId，没有则插入，有则更新
        if (StringUtils.isEmpty(registrationId)) {
            // 新增逻辑：生成登记申请ID
            registrationId = "VAR_REG" + IdUtils.fastSimpleUUID().substring(0, 16).toUpperCase();
            varietyRegistration.setRegistrationId(registrationId);

            // 生成登记申请号（格式：VAR+年月日+6位随机数）
            String registrationNo = generateRegistrationNo();
            varietyRegistration.setRegistrationNo(registrationNo);

            // 设置操作机构（如果有企业信息）
            if (enterpriseInfo != null) {
                varietyRegistration.setOperationOrg(enterpriseInfo.getEnterpriseName());
            }

            // 设置备案状态为审核中
            varietyRegistration.setRecordStatus(0);

            // 设置备案日期
            if (varietyRegistration.getRecordDate() == null) {
                varietyRegistration.setRecordDate(LocalDate.now());
            }

            // 设置操作时间
            varietyRegistration.setOperationTime(LocalDateTime.now());

            // 设置创建信息
            varietyRegistration.setCreateBy(LoginHelper.getUsername());
            varietyRegistration.setCreateTime(LocalDateTime.now());

            // 保存品种登记信息
            save(varietyRegistration);
        } else {
            // 更新逻辑：保存品种登记
            // 设置备案状态为审核中
            varietyRegistration.setRecordStatus(0);

            // 设置操作时间
            varietyRegistration.setOperationTime(LocalDateTime.now());

            // 设置更新信息
            varietyRegistration.setUpdateBy(LoginHelper.getUsername());
            varietyRegistration.setUpdateTime(LocalDateTime.now());

            // 更新品种登记信息
            updateById(varietyRegistration);
        }

        // 生成品种审核数据，添加为待审核状态
        VarietyAudit varietyAudit = new VarietyAudit();
        // 生成审核ID
        String auditId = "VAR_AUD" + IdUtils.fastSimpleUUID().substring(0, 16).toUpperCase();
        varietyAudit.setAuditId(auditId);
        // 设置关联的登记ID
        varietyAudit.setRegistrationId(registrationId);
        // 设置审核状态为待审核（0）
        varietyAudit.setAuditResult(0);
        // 设置企业ID
        varietyAudit.setEnterpriseId(varietyRegistration.getEnterpriseId());
        // 设置品种名称（冗余字段）
        varietyAudit.setVarietyName(varietyRegistration.getVarietyName());
        // 设置审核阶段为初审
        varietyAudit.setAuditStage("初审");
        // 设置审核人
        varietyAudit.setAuditor(LoginHelper.getUsername());
        // 设置创建信息
        varietyAudit.setCreateBy(LoginHelper.getUsername());
        varietyAudit.setCreateTime(LocalDateTime.now());

        // 保存审核记录
        varietyAuditService.save(varietyAudit);

        return registrationId;
    }

    @Override
    public List<VarietyRegistration> queryRegistrationList(String varietyName, String cropType, String recordStatus) {
        LambdaQueryWrapper<VarietyRegistration> wrapper = new LambdaQueryWrapper<>();

        // 品种名称模糊查询
        if (StringUtils.isNotEmpty(varietyName)) {
            wrapper.like(VarietyRegistration::getVarietyName, varietyName);
        }

        // 作物类型筛选
        if (StringUtils.isNotEmpty(cropType)) {
            wrapper.eq(VarietyRegistration::getCropType, cropType);
        }

        // 备案状态筛选
        if (StringUtils.isNotEmpty(recordStatus)) {
            wrapper.eq(VarietyRegistration::getRecordStatus, recordStatus);
        }

        // 按创建时间倒序排列
        wrapper.orderByDesc(VarietyRegistration::getCreateTime);

        return list(wrapper);
    }

    @Override
    public VarietyRegistration queryByRegistrationId(String registrationId) {
        return getById(registrationId);
    }

    @Override
    public void updateRecordStatus(String registrationId, Integer recordStatus) {
        VarietyRegistration varietyRegistration = new VarietyRegistration();
        varietyRegistration.setRegistrationId(registrationId);
        varietyRegistration.setRecordStatus(recordStatus);
        varietyRegistration.setUpdateBy(LoginHelper.getUsername());
        varietyRegistration.setUpdateTime(LocalDateTime.now());
        updateById(varietyRegistration);
    }

    @Override
    public List<VarietyRegistration> queryPendingPublishList(String varietyName, String cropType) {
        LambdaQueryWrapper<VarietyRegistration> wrapper = new LambdaQueryWrapper<>();

        // 查询待发布状态的品种（record_status = 1）
        wrapper.eq(VarietyRegistration::getRecordStatus, 1);

        // 品种名称筛选
        if (StringUtils.isNotEmpty(varietyName)) {
            wrapper.like(VarietyRegistration::getVarietyName, varietyName);
        }

        // 作物类型筛选
        if (StringUtils.isNotEmpty(cropType)) {
            wrapper.eq(VarietyRegistration::getCropType, cropType);
        }

        // 按创建时间倒序排列
        wrapper.orderByDesc(VarietyRegistration::getCreateTime);

        return list(wrapper);
    }

    /**
     * 生成登记申请号（格式：VAR+年月日+6位随机数）
     *
     * @return 登记申请号
     */
    private String generateRegistrationNo() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String dateStr = LocalDate.now().format(formatter);
        Random random = new Random();
        int randomNum = random.nextInt(900000) + 100000; // 生成100000-999999的随机数
        return "VAR" + dateStr + randomNum;
    }
}