package com.inspur.seed.controller;

import com.inspur.common.core.domain.AjaxResult;
import com.inspur.seed.domain.dto.VarietyEvaluationDataDTO;
import com.inspur.seed.domain.vo.VarietyEvaluationDataVO;
import com.inspur.seed.service.IVarietyEvaluationDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 品种评估数据采集Controller
 *
 * @author igdp
 * @date 2025-11-26
 */
@RestController
@RequestMapping("/seed/variety/evaluation")
public class VarietyEvaluationDataController {

    @Autowired
    private IVarietyEvaluationDataService varietyEvaluationDataService;

    /**
     * 查询品种评估数据列表
     */
    @PostMapping("/list")
    public AjaxResult list(@RequestBody VarietyEvaluationDataDTO dto) {
        List<VarietyEvaluationDataVO> list = varietyEvaluationDataService.selectVarietyEvaluationDataList(dto);
        return AjaxResult.success(list);
    }

    /**
     * 获取品种评估数据详细信息
     */
    @GetMapping("/{dataId}")
    public AjaxResult getInfo(@PathVariable String dataId) {
        VarietyEvaluationDataVO vo = varietyEvaluationDataService.selectVarietyEvaluationDataById(dataId);
        return AjaxResult.success(vo);
    }

    /**
     * 新增品种评估数据
     */
    @PostMapping("/add")
    public AjaxResult add(@RequestBody VarietyEvaluationDataDTO dto) {
        return AjaxResult.success(varietyEvaluationDataService.insertVarietyEvaluationData(dto));
    }

    /**
     * 修改品种评估数据
     */
    @PostMapping("/edit")
    public AjaxResult edit(@RequestBody VarietyEvaluationDataDTO dto) {
        return AjaxResult.success(varietyEvaluationDataService.updateVarietyEvaluationData(dto));
    }

    /**
     * 删除品种评估数据
     */
    @PostMapping("/delete")
    public AjaxResult remove(@RequestBody String[] dataIds) {
        return AjaxResult.success(varietyEvaluationDataService.deleteVarietyEvaluationDataByIds(dataIds));
    }
}
