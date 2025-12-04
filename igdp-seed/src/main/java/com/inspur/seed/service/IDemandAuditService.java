package com.inspur.seed.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.inspur.seed.domain.dto.*;
import com.inspur.seed.domain.vo.DemandAuditResultVO;
import com.inspur.seed.domain.vo.DemandPendingPageVO;

/**
 * Demand Audit Service Interface
 *
 * @author igdp
 * @date 2025-12-04
 */
public interface IDemandAuditService {

    /**
     * Submit demands for audit (DA submits to village level)
     *
     * @param dto Submit DTO
     * @return Result VO with success and fail counts
     */
    DemandAuditResultVO submitForAudit(DemandAuditSubmitDTO dto);

    /**
     * Query pending audit demands page
     *
     * @param dto Pending page query DTO
     * @return Page result
     */
    Page<DemandPendingPageVO> getPendingAuditPage(DemandAuditPendingPageDTO dto);

    /**
     * Approve demands and submit to next audit level
     *
     * @param dto Approve DTO
     * @return Result VO with success and fail counts
     */
    DemandAuditResultVO approveDemands(DemandAuditApproveDTO dto);

    /**
     * Reject demands back to DA for modification
     *
     * @param dto Reject DTO
     * @return Result VO with success and fail counts
     */
    DemandAuditResultVO rejectDemands(DemandAuditRejectDTO dto);

    /**
     * Lock batch demands (Ministry final approval)
     *
     * @param dto Lock DTO
     * @return Success flag
     */
    boolean lockBatchDemands(DemandLockDTO dto);
}
