package com.inspur.seed.controller;

import com.inspur.common.core.domain.AjaxResult;
import com.inspur.seed.domain.dto.AgronomicTraitAuditDTO;
import com.inspur.seed.domain.dto.AgronomicTraitAuditQueryDTO;
import com.inspur.seed.service.IAgronomicTraitAuditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 农艺性状审核Controller
 *
 * @author system
 * @since 2025-01-30
 */
@Slf4j
@RestController
@RequestMapping("/breeding/trait/audit") // 对应农艺性状业务路径
public class AgronomicTraitAuditController {

    @Autowired
    private IAgronomicTraitAuditService agronomicTraitAuditService; // 注入农艺性状审核Service

    /**
     * 查询待审核/已审核农艺性状列表
     */
    @PostMapping("/list")
    public AjaxResult list(@RequestBody AgronomicTraitAuditQueryDTO queryDTO) {
        return agronomicTraitAuditService.getAuditList(queryDTO);
    }

    /**
     * 根据审核记录ID查询审核详情
     */
    @GetMapping("/getById/{id}")
    public AjaxResult getById(@PathVariable String id) {
        return agronomicTraitAuditService.getAuditById(id);
    }

    /**
     * 根据农艺性状ID查询审核详情
     */
    @GetMapping("/getByTraitId/{traitId}")
    public AjaxResult getByTraitId(@PathVariable String traitId) {
        return agronomicTraitAuditService.getAuditByTraitId(traitId);
    }

    /**
     * 执行农艺性状审核(通过/驳回/需要修订)
     */
    @PostMapping("/perform")
    public AjaxResult performAudit(@RequestBody @Validated AgronomicTraitAuditDTO auditDTO) {
        return agronomicTraitAuditService.performAudit(auditDTO);
    }

    /**
     * 查询农艺性状审核历史记录
     */
    @GetMapping("/history/{traitId}")
    public AjaxResult getAuditHistory(@PathVariable String traitId) {
        return agronomicTraitAuditService.getAuditHistory(traitId);
    }
}