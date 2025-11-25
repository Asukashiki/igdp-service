package com.inspur.seed.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.common.exception.ServiceException;
import com.inspur.common.utils.LoginHelper;
import com.inspur.common.utils.StringUtils;
import com.inspur.common.utils.uuid.IdUtils;
import com.inspur.seed.domain.EnterpriseInfo;
import com.inspur.seed.domain.VarietyRegistration;
import com.inspur.seed.mapper.VarietyRegistrationMapper;
import com.inspur.seed.service.IEnterpriseCertifyService;
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

    @Override
    public String submitRegistration(VarietyRegistration varietyRegistration) {
        // 前置条件：检查企业是否已完成认证备案
        EnterpriseInfo enterpriseInfo = enterpriseCertifyService.queryByEnterpriseId(varietyRegistration.getEnterpriseId());
        if (enterpriseInfo == null) {
            throw new ServiceException("企业信息不存在");
        }
        if (enterpriseInfo.getCertificationStatus() != 1) {
            throw new ServiceException("企业尚未完成认证备案，无法提交品种登记申请");
        }

        // 生成登记申请ID
        String registrationId = "VAR_REG" + IdUtils.fastSimpleUUID().substring(0, 16).toUpperCase();
        varietyRegistration.setRegistrationId(registrationId);

        // 生成登记申请号（格式：VAR+年月日+6位随机数）
        String registrationNo = generateRegistrationNo();
        varietyRegistration.setRegistrationNo(registrationNo);

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

        return registrationId;
    }

    @Override
    public List<VarietyRegistration> queryRegistrationList(String varietyName, String enterpriseName, String enterpriseType, String recordType) {
        LambdaQueryWrapper<VarietyRegistration> wrapper = new LambdaQueryWrapper<>();

        // 品种名称模糊查询
        if (StringUtils.isNotEmpty(varietyName)) {
            wrapper.like(VarietyRegistration::getVarietyName, varietyName);
        }

        // 企业名称模糊查询
        if (StringUtils.isNotEmpty(enterpriseName)) {
            wrapper.like(VarietyRegistration::getEnterpriseName, enterpriseName);
        }

        // 企业类型筛选
        if (StringUtils.isNotEmpty(enterpriseType)) {
            wrapper.eq(VarietyRegistration::getEnterpriseType, enterpriseType);
        }

        // 备案类型筛选
        if (StringUtils.isNotEmpty(recordType)) {
            wrapper.eq(VarietyRegistration::getRecordType, recordType);
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
