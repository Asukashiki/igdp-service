package com.inspur.seed.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.seed.domain.dto.TrialBasicAuditDTO;
import com.inspur.seed.domain.entity.TrialBasicAudit;
import com.inspur.seed.domain.vo.TrialBasicAuditVO;

/**
 * 试验基础信息审核Service接口
 *
 * @author system
 * @since 2025-01-30
 */
public interface ITrialBasicAuditService extends IService<TrialBasicAudit> {

    /**
     * 分页查询审核列表
     *
     * @param dto 查询条件
     * @return 审核列表
     */
    IPage<TrialBasicAuditVO> selectAuditList(TrialBasicAuditDTO dto);

    /**
     * 根据ID获取审核详情
     *
     * @param auditId 审核ID
     * @return 审核详情
     */
    TrialBasicAuditVO getAuditById(String auditId);

    /**
     * 根据试验ID获取审核详情
     *
     * @param trialId 试验ID
     * @return 审核详情
     */
    TrialBasicAuditVO getAuditByTrialId(String trialId);

    /**
     * 执行审核(通过/退回)
     *
     * @param dto 审核信息
     * @return 审核结果
     */
    boolean performAudit(TrialBasicAuditDTO dto);

    /**
     * 查询审核历史
     *
     * @param trialId 试验ID
     * @return 审核历史列表
     */
    IPage<TrialBasicAuditVO> getAuditHistory(String trialId, Integer pageNum, Integer pageSize);
}
