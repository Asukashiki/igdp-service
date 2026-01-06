package com.inspur.seed.breeding.trialBasic.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.inspur.common.core.controller.BaseController;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.seed.breeding.trialBasic.domain.dto.TrialBasicAuditDTO;
import com.inspur.seed.breeding.trialBasic.domain.vo.TrialBasicAuditVO;
import com.inspur.seed.breeding.trialBasic.service.ITrialBasicAuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 试验基础信息审核Controller
 *
 * @author system
 * @since 2025-01-30
 */
@RestController
@RequestMapping("/breeding/trial/audit")
public class TrialBasicAuditController extends BaseController {

    @Autowired
    private ITrialBasicAuditService trialBasicAuditService;

    /**
     * 分页查询审核列表
     */
    @PostMapping("/list")
    public AjaxResult list(@RequestBody TrialBasicAuditDTO dto) {
        IPage<TrialBasicAuditVO> page = trialBasicAuditService.selectAuditList(dto);
        return AjaxResult.success(page);
    }

    /**
     * 根据ID获取审核详情
     */
    @GetMapping("/getById/{id}")
    public AjaxResult getById(@PathVariable("id") String auditId) {
        return AjaxResult.success(trialBasicAuditService.getAuditById(auditId));
    }

    /**
     * 根据试验ID获取审核详情
     */
    @GetMapping("/getByTrialId/{trialId}")
    public AjaxResult getByTrialId(@PathVariable("trialId") String trialId) {
        return AjaxResult.success(trialBasicAuditService.getAuditByTrialId(trialId));
    }

    /**
     * 执行审核(通过/退回)
     */
    @PostMapping("/perform")
    public AjaxResult perform(@RequestBody TrialBasicAuditDTO dto) {
        return toAjax(trialBasicAuditService.performAudit(dto));
    }

    /**
     * 查询审核历史
     */
    @GetMapping("/history/{trialId}")
    public AjaxResult history(
            @PathVariable("trialId") String trialId,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        IPage<TrialBasicAuditVO> page = trialBasicAuditService.getAuditHistory(trialId, pageNum, pageSize);
        return AjaxResult.success(page);
    }

    /**
     * 作废审核记录
     */
    @PostMapping("/void/{auditId}")
    public AjaxResult voidAudit(
            @PathVariable("auditId") String auditId,
            @RequestParam("voidReason") String voidReason) {
        return toAjax(trialBasicAuditService.voidAudit(auditId, voidReason));
    }
}
