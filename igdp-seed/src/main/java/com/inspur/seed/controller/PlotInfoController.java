package com.inspur.seed.controller;

import com.inspur.common.core.controller.BaseController;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.core.page.TableDataInfo;
import com.inspur.seed.domain.PlotInfo;
import com.inspur.seed.domain.PlotAuditRecord;
import com.inspur.seed.service.IPlotInfoService;
import com.inspur.seed.service.IPlotAuditRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    @Autowired
    private IPlotAuditRecordService plotAuditRecordService;

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

    /**
     * 提交审核
     */
    @PostMapping("/submitAudit")
    public AjaxResult submitAudit(@RequestBody Map<String, String> params) {
        String plotId = params.get("plotId");
        return toAjax(plotInfoService.submitAudit(plotId));
    }

    /**
     * 审核通过
     */
    @PostMapping("/approve")
    public AjaxResult approve(@RequestBody Map<String, String> params) {
        String plotId = params.get("plotId");
        String auditOpinion = params.get("auditOpinion");
        return toAjax(plotInfoService.approve(plotId, auditOpinion));
    }

    /**
     * 审核退回
     */
    @PostMapping("/reject")
    public AjaxResult reject(@RequestBody Map<String, String> params) {
        String plotId = params.get("plotId");
        String auditOpinion = params.get("auditOpinion");
        return toAjax(plotInfoService.reject(plotId, auditOpinion));
    }

    /**
     * 归档
     */
    @PostMapping("/archive")
    public AjaxResult archive(@RequestBody Map<String, String> params) {
        String plotId = params.get("plotId");
        return toAjax(plotInfoService.archive(plotId));
    }

    /**
     * 作废
     */
    @PostMapping("/cancel")
    public AjaxResult cancel(@RequestBody Map<String, String> params) {
        String plotId = params.get("plotId");
        return toAjax(plotInfoService.cancel(plotId));
    }

    /**
     * 获取审核历史
     */
    @GetMapping("/audit/history")
    public AjaxResult getAuditHistory(@RequestParam("plotId") String plotId) {
        List<PlotAuditRecord> history = plotAuditRecordService.selectAuditHistory(plotId);
        return AjaxResult.success(history);
    }

    /**
     * 作废审核记录（只作废审核记录，不修改地块数据）
     */
    @PostMapping("/cancelAuditRecord")
    public AjaxResult cancelAuditRecord(@RequestBody Map<String, String> params) {
        String plotId = params.get("plotId");
        return toAjax(plotInfoService.cancelAuditRecord(plotId));
    }
}
