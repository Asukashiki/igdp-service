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
 * 地块信息管理Controller
 *
 * @author inspur
 */
@RestController
@RequestMapping("/breeding/plot")
public class PlotInfoController extends BaseController {

    @Autowired
    private IPlotInfoService plotInfoService;

    /**
     * 分页查询地块信息列表
     */
    @GetMapping("/list")
    public TableDataInfo list(PlotInfo plotInfo) {
        startPage();
        List<PlotInfo> list = plotInfoService.selectPlotInfoList(plotInfo);
        return getDataTable(list);
    }

    /**
     * 获取地块信息详情
     */
    @GetMapping("/getInfo")
    public AjaxResult getInfo(@RequestParam("plotId") String plotId) {
        return AjaxResult.success(plotInfoService.selectPlotInfoById(plotId));
    }

    /**
     * 新增地块信息
     */
    @PostMapping("/add")
    public AjaxResult add(@RequestBody PlotInfo plotInfo) {
        String plotId = plotInfoService.insertPlotInfo(plotInfo);
        return AjaxResult.success("新增成功", plotId);
    }

    /**
     * 修改地块信息
     */
    @PostMapping("/edit")
    public AjaxResult edit(@RequestBody PlotInfo plotInfo) {
        return toAjax(plotInfoService.updatePlotInfo(plotInfo));
    }

    /**
     * 删除地块信息
     */
    @GetMapping("/remove")
    public AjaxResult remove(@RequestParam("plotIds") String plotIds) {
        String[] ids = plotIds.split(",");
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
