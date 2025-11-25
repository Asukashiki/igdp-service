package com.inspur.seed.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.seed.domain.EnterpriseAudit;

import java.util.List;

/**
 * 企业审核服务接口
 *
 * @author system
 */
public interface IEnterpriseAuditService extends IService<EnterpriseAudit> {

    /**
     * 处理企业审核
     *
     * @param enterpriseAudit 审核信息
     * @return 审核ID
     */
    String handleAudit(EnterpriseAudit enterpriseAudit);

    /**
     * 查询审核记录列表
     *
     * @param enterpriseId 企业ID
     * @param auditResult 审核结果
     * @param auditStage 审核阶段
     * @return 审核记录列表
     */
    List<EnterpriseAudit> queryAuditList(String enterpriseId, Integer auditResult, String auditStage);

    /**
     * 根据审核ID查询审核记录
     *
     * @param auditId 审核ID
     * @return 审核记录
     */
    EnterpriseAudit queryByAuditId(String auditId);

    /**
     * 根据企业ID查询最新审核记录
     *
     * @param enterpriseId 企业ID
     * @return 最新审核记录
     */
    EnterpriseAudit queryLatestByEnterpriseId(String enterpriseId);
}
