package com.inspur.seed.breeding.breedingDataset.controller;

import com.inspur.common.core.domain.AjaxResult;
import com.inspur.seed.breeding.breedingDataset.domain.dto.BreedingDatasetAuditDTO;
import com.inspur.seed.breeding.breedingDataset.domain.dto.BreedingDatasetAuditQueryDTO;
import com.inspur.seed.breeding.breedingDataset.service.IBreedingDatasetAuditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 育种数据集审核Controller
 *
 * @author system
 * @since 2025-01-30
 */
@Slf4j
@RestController
@RequestMapping("/seed/dataset/audit")
public class BreedingDatasetAuditController {

    @Autowired
    private IBreedingDatasetAuditService breedingDatasetAuditService;

    /**
     * 查询待审核/已审核数据集列表
     */
    @PostMapping("/list")
    public AjaxResult list(@RequestBody BreedingDatasetAuditQueryDTO queryDTO) {
        return breedingDatasetAuditService.getAuditList(queryDTO);
    }

    /**
     * 根据ID查询审核详情
     */
    @GetMapping("/getById/{id}")
    public AjaxResult getById(@PathVariable String id) {
        return breedingDatasetAuditService.getAuditById(id);
    }

    /**
     * 根据数据集ID查询审核详情
     */
    @GetMapping("/getByDatasetId/{datasetId}")
    public AjaxResult getByDatasetId(@PathVariable String datasetId) {
        return breedingDatasetAuditService.getAuditByDatasetId(datasetId);
    }

    /**
     * 执行审核(通过/驳回)
     */
    @PostMapping("/perform")
    public AjaxResult performAudit(@RequestBody @Validated BreedingDatasetAuditDTO auditDTO) {
        return breedingDatasetAuditService.performAudit(auditDTO);
    }

    /**
     * 查询审核历史记录
     */
    @GetMapping("/history/{datasetId}")
    public AjaxResult getAuditHistory(@PathVariable String datasetId) {
        return breedingDatasetAuditService.getAuditHistory(datasetId);
    }
}
