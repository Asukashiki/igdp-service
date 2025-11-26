package com.inspur.seed.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.common.exception.ServiceException;
import com.inspur.common.utils.LoginHelper;
import com.inspur.common.utils.StringUtils;
import com.inspur.common.utils.uuid.IdUtils;
import com.inspur.seed.domain.EnterpriseAudit;
import com.inspur.seed.mapper.EnterpriseAuditMapper;
import com.inspur.seed.service.IEnterpriseAuditService;
import com.inspur.seed.service.IEnterpriseCertifyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 企业审核服务实现类
 *
 * @author system
 */
@Service
public class EnterpriseAuditServiceImpl extends ServiceImpl<EnterpriseAuditMapper, EnterpriseAudit> implements IEnterpriseAuditService {

    @Resource
    private IEnterpriseCertifyService enterpriseCertifyService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String handleAudit(EnterpriseAudit enterpriseAudit) {
        // 校验企业是否存在
        if (enterpriseCertifyService.queryByEnterpriseId(enterpriseAudit.getEnterpriseId()) == null) {
            throw new ServiceException("企业信息不存在");
        }

        // 校验驳回时必须填写驳回原因
        if (enterpriseAudit.getAuditResult() == 2 && StringUtils.isEmpty(enterpriseAudit.getRejectReason())) {
            throw new ServiceException("驳回时必须填写驳回原因");
        }

        // 生成审核ID
        String auditId = "AUD" + IdUtils.fastSimpleUUID().substring(0, 16).toUpperCase();
        enterpriseAudit.setAuditId(auditId);

        // 设置审核时间
        enterpriseAudit.setAuditTime(LocalDateTime.now());

        // 设置创建信息
        enterpriseAudit.setCreateBy(LoginHelper.getUsername());
        enterpriseAudit.setCreateTime(LocalDateTime.now());

        // 保存审核记录
        save(enterpriseAudit);

        // 更新企业认证状态
        Integer certificationStatus = enterpriseAudit.getAuditResult();
        enterpriseCertifyService.updateCertificationStatus(enterpriseAudit.getEnterpriseId(), certificationStatus);

        return auditId;
    }

    @Override
    public List<EnterpriseAudit> queryAuditList(String enterpriseId, Integer auditResult, String auditStage) {
        LambdaQueryWrapper<EnterpriseAudit> wrapper = new LambdaQueryWrapper<>();

        // 企业ID筛选
        if (StringUtils.isNotEmpty(enterpriseId)) {
            wrapper.eq(EnterpriseAudit::getEnterpriseId, enterpriseId);
        }

        // 审核结果筛选
        if (auditResult != null) {
            wrapper.eq(EnterpriseAudit::getAuditResult, auditResult);
        }

        // 审核阶段筛选
        if (StringUtils.isNotEmpty(auditStage)) {
            wrapper.eq(EnterpriseAudit::getAuditStage, auditStage);
        }

        // 按审核时间倒序排列
        wrapper.orderByDesc(EnterpriseAudit::getAuditTime);

        return list(wrapper);
    }

    @Override
    public EnterpriseAudit queryByAuditId(String auditId) {
        return getById(auditId);
    }

    @Override
    public EnterpriseAudit queryLatestByEnterpriseId(String enterpriseId) {
        LambdaQueryWrapper<EnterpriseAudit> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EnterpriseAudit::getEnterpriseId, enterpriseId);
        wrapper.orderByDesc(EnterpriseAudit::getAuditTime);
        wrapper.last("LIMIT 1");
        return getOne(wrapper);
    }
}
