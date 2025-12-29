package com.inspur.seed.controller;

import com.inspur.common.core.domain.AjaxResult;
import com.inspur.seed.domain.dto.BreedingLabTestDTO;
import com.inspur.seed.domain.vo.BreedingLabTestVO;
import com.inspur.seed.service.IBreedingLabTestService;
import com.inspur.seed.breeding.breedingBatch.service.IBreedingBatchService;
import com.inspur.seed.breeding.trialBasic.service.ITrialBasicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 实验室测试数据Controller
 *
 * @author igdp
 * @date 2025-11-29
 */
@RestController
@RequestMapping("/seed/lab-test")
public class BreedingLabTestController {

    @Autowired
    private IBreedingLabTestService breedingLabTestService;

    @Autowired
    private IBreedingBatchService breedingBatchService;

    @Autowired
    private ITrialBasicService trialBasicService;

    /**
     * 查询实验室测试数据列表
     */
    @PostMapping("/list")
    public AjaxResult list(@RequestBody BreedingLabTestDTO dto) {
        Map<String, Object> result = breedingLabTestService.selectBreedingLabTestList(dto);
        return AjaxResult.success(result);
    }

    /**
     * 获取实验室测试数据详细信息
     */
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable String id) {
        BreedingLabTestVO vo = breedingLabTestService.selectBreedingLabTestById(id);
        return AjaxResult.success(vo);
    }

    /**
     * 新增实验室测试数据
     */
    @PostMapping("/add")
    public AjaxResult add(@RequestBody BreedingLabTestDTO dto) {
        return AjaxResult.success(breedingLabTestService.insertBreedingLabTest(dto));
    }

    /**
     * 修改实验室测试数据
     */
    @PostMapping("/edit")
    public AjaxResult edit(@RequestBody BreedingLabTestDTO dto) {
        return AjaxResult.success(breedingLabTestService.updateBreedingLabTest(dto));
    }

    /**
     * 删除实验室测试数据
     */
    @PostMapping("/delete")
    public AjaxResult remove(@RequestBody String[] ids) {
        return AjaxResult.success(breedingLabTestService.deleteBreedingLabTestByIds(ids));
    }

    /**
     * 获取育种批次下拉列表
     */
    @GetMapping("/batch-options")
    public AjaxResult getBatchOptions() {
        return AjaxResult.success(breedingBatchService.selectBatchOptions());
    }

    /**
     * 根据育种批次ID获取试验列表
     */
    @GetMapping("/trial-options/{batchId}")
    public AjaxResult getTrialOptions(@PathVariable String batchId) {
        return AjaxResult.success(trialBasicService.selectTrialOptions(batchId));
    }
}
