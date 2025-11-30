package com.inspur.seed.controller;

import com.inspur.common.core.controller.BaseController;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.core.page.TableDataInfo;
import com.inspur.seed.domain.PlotInfo;
import com.inspur.seed.service.IPlotInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 地块及播种信息管理Controller
 *
 * @author inspur
 */
@RestController
@RequestMapping("/breeding/plot")
public class PlotInfoController extends BaseController {

    @Autowired
    private IPlotInfoService plotInfoService;

    /**
     * 分页查询地块及播种信息列表
     */
    @GetMapping("/list")
    public TableDataInfo list(PlotInfo plotInfo) {
        startPage();
        List<PlotInfo> list = plotInfoService.selectPlotInfoList(plotInfo);
        return getDataTable(list);
    }

    /**
     * 获取地块及播种信息详情
     */
    @GetMapping("/getInfo")
    public AjaxResult getInfo(@RequestParam("groundId") String groundId) {
        return AjaxResult.success(plotInfoService.selectPlotInfoById(groundId));
    }

    /**
     * 新增地块及播种信息
     */
    @PostMapping("/add")
    public AjaxResult add(@RequestBody PlotInfo plotInfo) {
        String groundId = plotInfoService.insertPlotInfo(plotInfo);
        return AjaxResult.success("新增成功", groundId);
    }

    /**
     * 修改地块及播种信息
     */
    @PostMapping("/edit")
    public AjaxResult edit(@RequestBody PlotInfo plotInfo) {
        return toAjax(plotInfoService.updatePlotInfo(plotInfo));
    }

    /**
     * 删除地块及播种信息
     */
    @GetMapping("/remove")
    public AjaxResult remove(@RequestParam("groundIds") String groundIds) {
        String[] ids = groundIds.split(",");
        return toAjax(plotInfoService.deletePlotInfoByIds(ids));
    }

    /**
     * 根据批次ID获取地块列表
     */
    @GetMapping("/listByBatch")
    public AjaxResult listByBatch(@RequestParam("batchId") String batchId) {
        return AjaxResult.success(plotInfoService.selectPlotsByBatchId(batchId));
    }

    /**
     * 获取地块下拉选项列表
     * 支持按批次ID和试验ID过滤
     */
    @GetMapping("/options")
    public AjaxResult getOptions(
            @RequestParam(value = "batchId", required = false) String batchId,
            @RequestParam(value = "trialId", required = false) String trialId) {
        return AjaxResult.success(plotInfoService.selectPlotOptions(batchId, trialId));
    }
}
