package com.inspur.seed.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.common.exception.ServiceException;
import com.inspur.common.utils.LoginHelper;
import com.inspur.common.utils.StringUtils;
import com.inspur.common.utils.uuid.IdUtils;
import com.inspur.seed.domain.VarietyAudit;
import com.inspur.seed.domain.VarietyRegistration;
import com.inspur.seed.mapper.VarietyAuditMapper;
import com.inspur.seed.service.IVarietyAuditService;
import com.inspur.seed.service.IVarietyRegistrationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 品种审核服务实现类
 *
 * @author system
 */
@Service
public class VarietyAuditServiceImpl extends ServiceImpl<VarietyAuditMapper, VarietyAudit> implements IVarietyAuditService {

    @Resource
    private IVarietyRegistrationService varietyRegistrationService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String handleAudit(VarietyAudit varietyAudit) {
        // 校验品种登记申请是否存在
        VarietyRegistration registration = varietyRegistrationService.queryByRegistrationId(varietyAudit.getRegistrationId());
        if (registration == null) {
            throw new ServiceException("品种登记申请不存在");
        }

        // 校验品种登记申请状态是否为审核中
        if (registration.getRecordStatus() != 0) {
            throw new ServiceException("品种登记申请状态不是审核中，无法审核");
        }

        // 校验驳回时必须填写驳回原因
        if (varietyAudit.getAuditResult() == 2 && StringUtils.isEmpty(varietyAudit.getRejectReason())) {
            throw new ServiceException("驳回时必须填写驳回原因");
        }

        // 生成审核ID
        String auditId = "VAR_AUD" + IdUtils.fastSimpleUUID().substring(0, 16).toUpperCase();
        varietyAudit.setAuditId(auditId);

        // 设置审核时间
        varietyAudit.setAuditTime(LocalDateTime.now());

        // 设置创建信息
        varietyAudit.setCreateBy(LoginHelper.getUsername());
        varietyAudit.setCreateTime(LocalDateTime.now());

        // 保存审核记录
        save(varietyAudit);

        // 更新品种登记申请状态
        // 审核通过：更新为待发布(1)，驳回：更新为审核未通过(2)
        Integer recordStatus = varietyAudit.getAuditResult() == 1 ? 1 : 2;
        varietyRegistrationService.updateRecordStatus(varietyAudit.getRegistrationId(), recordStatus);

        return auditId;
    }

    @Override
    public List<VarietyAudit> queryAuditList(String varietyName, String enterpriseName, Integer auditResult) {
        LambdaQueryWrapper<VarietyAudit> wrapper = new LambdaQueryWrapper<>();

        // 品种名称筛选
        if (StringUtils.isNotEmpty(varietyName)) {
            wrapper.like(VarietyAudit::getVarietyName, varietyName);
        }

        // 企业名称筛选（需要关联查询，这里简化处理）
        // 如果需要按企业名称筛选，可以通过enterpriseId关联查询

        // 审核结果筛选
        if (auditResult != null) {
            wrapper.eq(VarietyAudit::getAuditResult, auditResult);
        }

        // 按审核时间倒序排列
        wrapper.orderByDesc(VarietyAudit::getAuditTime);

        return list(wrapper);
    }

    @Override
    public VarietyAudit queryByAuditId(String auditId) {
        return getById(auditId);
    }

    @Override
    public VarietyAudit queryLatestByRegistrationId(String registrationId) {
        LambdaQueryWrapper<VarietyAudit> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(VarietyAudit::getRegistrationId, registrationId);
        wrapper.orderByDesc(VarietyAudit::getAuditTime);
        wrapper.last("LIMIT 1");
        return getOne(wrapper);
    }
}
