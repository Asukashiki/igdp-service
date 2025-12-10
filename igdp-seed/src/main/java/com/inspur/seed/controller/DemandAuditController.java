package com.inspur.seed.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.seed.domain.dto.*;
import com.inspur.seed.domain.vo.DemandAuditResultVO;
import com.inspur.seed.domain.vo.DemandPendingPageVO;
import com.inspur.seed.service.IDemandAuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Demand Audit Controller
 * Input Demand Audit Module
 *
 * @author igdp
 * @date 2025-12-04
 */
@RestController
@RequestMapping("/seed/demand/audit")
public class DemandAuditController {

    @Autowired
    private IDemandAuditService demandAuditService;

    /**
     * Submit demands for audit (DA submits to village level)
     */
    @PostMapping("/submit")
    public AjaxResult submitForAudit(@Validated @RequestBody DemandAuditSubmitDTO dto) {
        DemandAuditResultVO result = demandAuditService.submitForAudit(dto);
        return AjaxResult.success("Submitted successfully", result);
    }

    /**
     * Query pending audit demands page
     */
    @PostMapping("/pending/page")
    public AjaxResult getPendingAuditPage(@RequestBody DemandAuditPendingPageDTO dto) {
        Page<DemandPendingPageVO> page = demandAuditService.getPendingAuditPage(dto);
        return AjaxResult.success("Operation successful", page);
    }

    /**
     * Approve demands and submit to next audit level
     */
    @PostMapping("/approve")
    public AjaxResult approveDemands(@Validated @RequestBody DemandAuditApproveDTO dto) {
        DemandAuditResultVO result = demandAuditService.approveDemands(dto);
        return AjaxResult.success("Approved successfully", result);
    }

    /**
     * Reject demands back to DA for modification
     */
    @PostMapping("/reject")
    public AjaxResult rejectDemands(@Validated @RequestBody DemandAuditRejectDTO dto) {
        DemandAuditResultVO result = demandAuditService.rejectDemands(dto);
        return AjaxResult.success("Rejected successfully", result);
    }

    /**
     * Lock batch demands (Ministry final approval)
     */
    @PostMapping("/lock")
    public AjaxResult lockBatchDemands(@Validated @RequestBody DemandLockDTO dto) {
        demandAuditService.lockBatchDemands(dto);
        return AjaxResult.success("Batch locked successfully");
    }

    /**
     * Query approved audit demands page (status = approved)
     */
    @PostMapping("/approved/page")
    public AjaxResult getApprovedAuditPage(@RequestBody DemandAuditPendingPageDTO dto) {
        Page<DemandPendingPageVO> page = demandAuditService.getApprovedAuditPage(dto);
        return AjaxResult.success("Operation successful", page);
    }
}
