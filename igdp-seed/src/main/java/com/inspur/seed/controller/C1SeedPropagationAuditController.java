package com.inspur.seed.controller;

import com.inspur.common.core.domain.AjaxResult;
import com.inspur.seed.domain.dto.C1SeedPropagationDTO;
import com.inspur.seed.domain.dto.C1SeedPropagationQueryDTO;
import com.inspur.seed.service.IC1SeedPropagationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * C1种子繁殖申请审核Controller
 * 供OIA使用
 *
 * @author system
 * @since 2025-12-08
 */
@Slf4j
@RestController
@RequestMapping("/seed/c1-propagation-audit")
public class C1SeedPropagationAuditController {

    @Autowired
    private IC1SeedPropagationService propagationService;

    /**
     * 获取待审核列表（分页）
     *
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    @PostMapping("/list")
    public AjaxResult getPendingList(@RequestBody C1SeedPropagationQueryDTO queryDTO) {
        log.info("获取C1繁殖申请待审核列表,查询条件: {}", queryDTO);
        return propagationService.getPendingList(queryDTO);
    }

    /**
     * 获取所有申请列表（分页）
     *
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    @PostMapping("/all-list")
    public AjaxResult getAllList(@RequestBody C1SeedPropagationQueryDTO queryDTO) {
        log.info("获取C1繁殖申请所有列表,查询条件: {}", queryDTO);
        return propagationService.getList(queryDTO);
    }

    /**
     * 根据ID获取审核详情
     *
     * @param id 申请ID
     * @return 审核详情
     */
    @GetMapping("/getById/{id}")
    public AjaxResult getById(@PathVariable("id") String id) {
        log.info("获取C1繁殖申请审核详情,ID: {}", id);
        return propagationService.getById(id);
    }

    /**
     * 提交审核
     *
     * @param dto 审核信息
     * @return 操作结果
     */
    @PostMapping("/audit")
    public AjaxResult audit(@RequestBody C1SeedPropagationDTO dto) {
        log.info("审核C1繁殖申请,ID: {}, 结果: {}", dto.getId(), dto.getAuditResult());
        return propagationService.audit(dto);
    }
}
