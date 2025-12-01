package com.inspur.seed.controller;

import com.inspur.common.core.domain.AjaxResult;
import com.inspur.seed.domain.dto.BreedingDatasetDTO;
import com.inspur.seed.domain.dto.BreedingDatasetQueryDTO;
import com.inspur.seed.service.IBreedingDatasetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 育种数据集Controller
 *
 * @author system
 * @date 2025-01-30
 */
@Slf4j
@RestController
@RequestMapping("/seed" +
        "/dataset")
public class BreedingDatasetController {

    @Autowired
    private IBreedingDatasetService breedingDatasetService;

    /**
     * 查询育种数据集列表
     */
    @PostMapping("/list")
    public AjaxResult list(@RequestBody BreedingDatasetQueryDTO queryDTO) {
        return breedingDatasetService.getDatasetList(queryDTO);
    }

    /**
     * 根据ID查询育种数据集详情
     */
    @GetMapping("/getById/{id}")
    public AjaxResult getById(@PathVariable String id) {
        return breedingDatasetService.getDatasetById(id);
    }

    /**
     * 新增育种数据集
     */
    @PostMapping("/add")
    public AjaxResult add(@RequestBody @Validated BreedingDatasetDTO dto) {
        return breedingDatasetService.addDataset(dto);
    }

    /**
     * 修改育种数据集
     */
    @PostMapping("/update")
    public AjaxResult update(@RequestBody @Validated BreedingDatasetDTO dto) {
        return breedingDatasetService.updateDataset(dto);
    }

    /**
     * 删除育种数据集
     */
    @PostMapping("/delete")
    public AjaxResult delete(@RequestBody String[] ids) {
        return breedingDatasetService.deleteDataset(ids);
    }

    /**
     * 提交审核
     */
    @PostMapping("/submit/{id}")
    public AjaxResult submit(@PathVariable String id) {
        return breedingDatasetService.submitDataset(id);
    }

    /**
     * 统计数据集各项数据记录数
     */
    @GetMapping("/statistics/{batchId}")
    public AjaxResult statistics(@PathVariable String batchId) {
        return breedingDatasetService.statisticsData(batchId);
    }
}
