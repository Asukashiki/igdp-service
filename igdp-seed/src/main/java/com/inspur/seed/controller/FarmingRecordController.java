package com.inspur.seed.controller;

import com.inspur.common.core.controller.BaseController;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.core.page.TableDataInfo;
import com.inspur.seed.domain.FarmingRecord;
import com.inspur.seed.service.IFarmingRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 农事记录Controller
 *
 * @author inspur
 */
@RestController
@RequestMapping("/breeding/farming")
public class FarmingRecordController extends BaseController {

    @Autowired
    private IFarmingRecordService farmingRecordService;

    /**
     * 分页查询农事记录列表
     */
    @GetMapping("/list")
    public TableDataInfo list(FarmingRecord farmingRecord) {
        startPage();
        List<FarmingRecord> list = farmingRecordService.selectFarmingRecordList(farmingRecord);
        return getDataTable(list);
    }

    /**
     * 获取农事记录详情
     */
    @GetMapping("/getInfo")
    public AjaxResult getInfo(@RequestParam("farmingId") String farmingId) {
        return AjaxResult.success(farmingRecordService.selectFarmingRecordById(farmingId));
    }

    /**
     * 新增农事记录
     */
    @PostMapping("/add")
    public AjaxResult add(@RequestBody FarmingRecord farmingRecord) {
        String farmingId = farmingRecordService.insertFarmingRecord(farmingRecord);
        return AjaxResult.success("新增成功", farmingId);
    }

    /**
     * 修改农事记录
     */
    @PostMapping("/edit")
    public AjaxResult edit(@RequestBody FarmingRecord farmingRecord) {
        return toAjax(farmingRecordService.updateFarmingRecord(farmingRecord));
    }

    /**
     * 删除农事记录
     */
    @GetMapping("/remove")
    public AjaxResult remove(@RequestParam("farmingIds") String farmingIds) {
        String[] ids = farmingIds.split(",");
        return toAjax(farmingRecordService.deleteFarmingRecordByIds(ids));
    }

    /**
     * 统计每个地块的灌溉次数
     */
    @GetMapping("/irrigationCount")
    public AjaxResult getIrrigationCountByPlot() {
        return AjaxResult.success(farmingRecordService.getIrrigationCountByPlot());
    }
}
