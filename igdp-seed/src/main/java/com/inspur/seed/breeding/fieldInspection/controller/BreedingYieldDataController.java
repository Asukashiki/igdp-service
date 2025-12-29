package com.inspur.seed.breeding.fieldInspection.controller;

import com.inspur.common.core.domain.AjaxResult;
import com.inspur.seed.breeding.fieldInspection.domain.dto.BreedingYieldDataDTO;
import com.inspur.seed.breeding.fieldInspection.domain.vo.BreedingYieldDataVO;
import com.inspur.seed.breeding.fieldInspection.service.IBreedingYieldDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 产量数据Controller
 *
 * @author igdp
 * @date 2025-11-29
 */
@RestController
@RequestMapping("/seed/yield-data")
public class BreedingYieldDataController {

    @Autowired
    private IBreedingYieldDataService breedingYieldDataService;

    /**
     * 查询产量数据列表
     */
    @PostMapping("/list")
    public AjaxResult list(@RequestBody BreedingYieldDataDTO dto) {
        List<BreedingYieldDataVO> list = breedingYieldDataService.selectBreedingYieldDataList(dto);
        return AjaxResult.success(list);
    }

    /**
     * 获取产量数据详细信息
     */
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable String id) {
        BreedingYieldDataVO vo = breedingYieldDataService.selectBreedingYieldDataById(id);
        return AjaxResult.success(vo);
    }

    /**
     * 新增产量数据
     */
    @PostMapping("/add")
    public AjaxResult add(@RequestBody BreedingYieldDataDTO dto) {
        return AjaxResult.success(breedingYieldDataService.insertBreedingYieldData(dto));
    }

    /**
     * 修改产量数据
     */
    @PostMapping("/edit")
    public AjaxResult edit(@RequestBody BreedingYieldDataDTO dto) {
        return AjaxResult.success(breedingYieldDataService.updateBreedingYieldData(dto));
    }

    /**
     * 删除产量数据
     */
    @PostMapping("/delete")
    public AjaxResult remove(@RequestBody String[] ids) {
        return AjaxResult.success(breedingYieldDataService.deleteBreedingYieldDataByIds(ids));
    }

    /**
     * 提交审核
     */
    @PostMapping("/submitForReview")
    public AjaxResult submitForReview(@RequestBody BreedingYieldDataDTO dto) {
        return AjaxResult.success(breedingYieldDataService.submitForReview(dto.getId(), dto.getWorkflowStatus()));
    }

    /**
     * 作废产量数据
     */
    @PostMapping("/void")
    public AjaxResult voidData(@RequestBody BreedingYieldDataDTO dto) {
        return AjaxResult.success(breedingYieldDataService.voidYieldData(dto.getId(), dto.getRemark()));
    }

//    /**
//     * 审核通过（支持批量）
//     */
//    @PostMapping("/approve")
//    public AjaxResult approve(@RequestBody String[] ids) {
//        return AjaxResult.success(breedingYieldDataService.approve(ids, null));
//    }
//
//    /**
//     * 审核驳回（支持批量）
//     */
//    @PostMapping("/reject")
//    public AjaxResult reject(@RequestBody String[] ids) {
//        return AjaxResult.success(breedingYieldDataService.reject(ids, null));
//    }
}
