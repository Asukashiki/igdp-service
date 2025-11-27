package com.inspur.seed.controller;

import com.inspur.common.core.domain.AjaxResult;
import com.inspur.seed.domain.dto.FarmerPlotDataDTO;
import com.inspur.seed.domain.vo.FarmerPlotDataVO;
import com.inspur.seed.service.IFarmerPlotDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 农民与地块属性数据采集Controller
 *
 * @author igdp
 * @date 2025-11-26
 */
@RestController
@RequestMapping("/seed/farmer/plot")
public class FarmerPlotDataController {

    @Autowired
    private IFarmerPlotDataService farmerPlotDataService;

    /**
     * 查询农民与地块属性数据列表
     */
    @PostMapping("/list")
    public AjaxResult list(@RequestBody FarmerPlotDataDTO dto) {
        List<FarmerPlotDataVO> list = farmerPlotDataService.selectFarmerPlotDataList(dto);
        return AjaxResult.success(list);
    }

    /**
     * 获取农民与地块属性数据详细信息
     */
    @GetMapping("/{dataId}")
    public AjaxResult getInfo(@PathVariable String dataId) {
        FarmerPlotDataVO vo = farmerPlotDataService.selectFarmerPlotDataById(dataId);
        return AjaxResult.success(vo);
    }

    /**
     * 新增农民与地块属性数据
     */
    @PostMapping("/add")
    public AjaxResult add(@RequestBody FarmerPlotDataDTO dto) {
        return AjaxResult.success(farmerPlotDataService.insertFarmerPlotData(dto));
    }

    /**
     * 修改农民与地块属性数据
     */
    @PostMapping("/edit")
    public AjaxResult edit(@RequestBody FarmerPlotDataDTO dto) {
        return AjaxResult.success(farmerPlotDataService.updateFarmerPlotData(dto));
    }

    /**
     * 删除农民与地块属性数据
     */
    @PostMapping("/delete")
    public AjaxResult remove(@RequestBody String[] dataIds) {
        return AjaxResult.success(farmerPlotDataService.deleteFarmerPlotDataByIds(dataIds));
    }
}
