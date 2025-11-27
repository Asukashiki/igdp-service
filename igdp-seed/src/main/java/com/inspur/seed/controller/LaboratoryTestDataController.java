package com.inspur.seed.controller;

import com.inspur.common.core.domain.AjaxResult;
import com.inspur.seed.domain.dto.LaboratoryTestDataDTO;
import com.inspur.seed.domain.vo.LaboratoryTestDataVO;
import com.inspur.seed.service.ILaboratoryTestDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 实验室测试数据采集Controller
 *
 * @author igdp
 * @date 2025-11-26
 */
@RestController
@RequestMapping("/seed/laboratory/test")
public class LaboratoryTestDataController {

    @Autowired
    private ILaboratoryTestDataService laboratoryTestDataService;

    /**
     * 查询实验室测试数据列表
     */
    @PostMapping("/list")
    public AjaxResult list(@RequestBody LaboratoryTestDataDTO dto) {
        List<LaboratoryTestDataVO> list = laboratoryTestDataService.selectLaboratoryTestDataList(dto);
        return AjaxResult.success(list);
    }

    /**
     * 获取实验室测试数据详细信息
     */
    @GetMapping("/{dataId}")
    public AjaxResult getInfo(@PathVariable String dataId) {
        LaboratoryTestDataVO vo = laboratoryTestDataService.selectLaboratoryTestDataById(dataId);
        return AjaxResult.success(vo);
    }

    /**
     * 新增实验室测试数据
     */
    @PostMapping("/add")
    public AjaxResult add(@RequestBody LaboratoryTestDataDTO dto) {
        return AjaxResult.success(laboratoryTestDataService.insertLaboratoryTestData(dto));
    }

    /**
     * 修改实验室测试数据
     */
    @PostMapping("/edit")
    public AjaxResult edit(@RequestBody LaboratoryTestDataDTO dto) {
        return AjaxResult.success(laboratoryTestDataService.updateLaboratoryTestData(dto));
    }

    /**
     * 删除实验室测试数据
     */
    @PostMapping("/delete")
    public AjaxResult remove(@RequestBody String[] dataIds) {
        return AjaxResult.success(laboratoryTestDataService.deleteLaboratoryTestDataByIds(dataIds));
    }
}
