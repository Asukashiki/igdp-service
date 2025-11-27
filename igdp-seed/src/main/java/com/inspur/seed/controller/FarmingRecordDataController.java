package com.inspur.seed.controller;

import com.inspur.common.core.domain.AjaxResult;
import com.inspur.seed.domain.dto.FarmingRecordDataDTO;
import com.inspur.seed.domain.vo.FarmingRecordDataVO;
import com.inspur.seed.service.IFarmingRecordDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 农事记录数据采集Controller
 *
 * @author igdp
 * @date 2025-11-26
 */
@RestController
@RequestMapping("/seed/farming/record")
public class FarmingRecordDataController {

    @Autowired
    private IFarmingRecordDataService farmingRecordDataService;

    /**
     * 查询农事记录数据列表
     */
    @PostMapping("/list")
    public AjaxResult list(@RequestBody FarmingRecordDataDTO dto) {
        List<FarmingRecordDataVO> list = farmingRecordDataService.selectFarmingRecordDataList(dto);
        return AjaxResult.success(list);
    }

    /**
     * 获取农事记录数据详细信息
     */
    @GetMapping("/{dataId}")
    public AjaxResult getInfo(@PathVariable String dataId) {
        FarmingRecordDataVO vo = farmingRecordDataService.selectFarmingRecordDataById(dataId);
        return AjaxResult.success(vo);
    }

    /**
     * 新增农事记录数据
     */
    @PostMapping("/add")
    public AjaxResult add(@RequestBody FarmingRecordDataDTO dto) {
        return AjaxResult.success(farmingRecordDataService.insertFarmingRecordData(dto));
    }

    /**
     * 修改农事记录数据
     */
    @PostMapping("/edit")
    public AjaxResult edit(@RequestBody FarmingRecordDataDTO dto) {
        return AjaxResult.success(farmingRecordDataService.updateFarmingRecordData(dto));
    }

    /**
     * 删除农事记录数据
     */
    @PostMapping("/delete")
    public AjaxResult remove(@RequestBody String[] dataIds) {
        return AjaxResult.success(farmingRecordDataService.deleteFarmingRecordDataByIds(dataIds));
    }
}
