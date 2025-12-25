package com.inspur.agriculture.input.controller.dashboard;

import com.inspur.agriculture.input.service.dashboard.IInputDashboardService;
import com.inspur.agriculture.input.vo.dashboard.*;
import com.inspur.common.core.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 农业投入品大屏数据Controller
 *
 * @author igdp
 */
@RestController
@RequestMapping("/input/dashboard")
public class InputDashboardController {

    @Autowired
    private IInputDashboardService inputDashboardService;

    /**
     * 获取概览统计数据
     *
     * @param year 年份（可选，默认当前年）
     * @return 概览数据
     */
    @GetMapping("/overview")
    public AjaxResult getOverview(@RequestParam(required = false) String year) {
        try {
            DashboardOverviewVO overview = inputDashboardService.getOverviewData(year);
            return AjaxResult.success(overview);
        } catch (Exception e) {
            return AjaxResult.error("获取概览数据失败: " + e.getMessage());
        }
    }

    /**
     * 获取需求汇聚统计
     *
     * @param year 年份（可选）
     * @param groupBy 分组方式: type/region，默认type
     * @return 需求汇聚数据
     */
    @GetMapping("/demand-summary")
    public AjaxResult getDemandSummary(
            @RequestParam(required = false) String year,
            @RequestParam(defaultValue = "type") String groupBy) {
        try {
            List<DemandSummaryVO> summary;
            if ("region".equals(groupBy)) {
                summary = inputDashboardService.getDemandSummaryByRegion(year);
            } else {
                summary = inputDashboardService.getDemandSummaryByType(year);
            }
            return AjaxResult.success(summary);
        } catch (Exception e) {
            return AjaxResult.error("获取需求汇聚数据失败: " + e.getMessage());
        }
    }

    /**
     * 获取分配进度数据
     *
     * @param year 年份（可选）
     * @return 分配进度
     */
    @GetMapping("/allocation-progress")
    public AjaxResult getAllocationProgress(@RequestParam(required = false) String year) {
        try {
            AllocationProgressVO progress = inputDashboardService.getAllocationProgress(year);
            return AjaxResult.success(progress);
        } catch (Exception e) {
            return AjaxResult.error("获取分配进度失败: " + e.getMessage());
        }
    }

    /**
     * 获取流通状态数据
     *
     * @param year 年份（可选）
     * @return 流通状态
     */
    @GetMapping("/circulation-status")
    public AjaxResult getCirculationStatus(@RequestParam(required = false) String year) {
        try {
            CirculationStatusVO status = inputDashboardService.getCirculationStatus(year);
            return AjaxResult.success(status);
        } catch (Exception e) {
            return AjaxResult.error("获取流通状态失败: " + e.getMessage());
        }
    }

    /**
     * 获取供应商排行
     *
     * @param limit 数量限制（默认10）
     * @return 供应商排行列表
     */
    @GetMapping("/top-suppliers")
    public AjaxResult getTopSuppliers(@RequestParam(defaultValue = "10") Integer limit) {
        try {
            List<SupplierRankVO> suppliers = inputDashboardService.getTopSuppliers(limit);
            return AjaxResult.success(suppliers);
        } catch (Exception e) {
            return AjaxResult.error("获取供应商排行失败: " + e.getMessage());
        }
    }

    /**
     * 获取最新动态
     *
     * @param limit 数量限制（默认20）
     * @return 最新动态列表
     */
    @GetMapping("/recent-activities")
    public AjaxResult getRecentActivities(@RequestParam(defaultValue = "20") Integer limit) {
        try {
            List<RecentActivityVO> activities = inputDashboardService.getRecentActivities(limit);
            return AjaxResult.success(activities);
        } catch (Exception e) {
            return AjaxResult.error("获取最新动态失败: " + e.getMessage());
        }
    }

    /**
     * 一次性获取所有大屏数据
     *
     * @param year 年份（可选）
     * @return 综合大屏数据
     */
    @GetMapping("/all")
    public AjaxResult getAllDashboardData(@RequestParam(required = false) String year) {
        try {
            Map<String, Object> result = new HashMap<>();
            result.put("overview", inputDashboardService.getOverviewData(year));
            result.put("demandSummaryByType", inputDashboardService.getDemandSummaryByType(year));
            result.put("demandSummaryByRegion", inputDashboardService.getDemandSummaryByRegion(year));
            result.put("allocationProgress", inputDashboardService.getAllocationProgress(year));
            result.put("circulationStatus", inputDashboardService.getCirculationStatus(year));
            result.put("topSuppliers", inputDashboardService.getTopSuppliers(10));
            result.put("recentActivities", inputDashboardService.getRecentActivities(20));
            return AjaxResult.success(result);
        } catch (Exception e) {
            return AjaxResult.error("获取大屏数据失败: " + e.getMessage());
        }
    }
}
