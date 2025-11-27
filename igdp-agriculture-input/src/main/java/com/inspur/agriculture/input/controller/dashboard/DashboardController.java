package com.inspur.agriculture.input.controller.dashboard;

import com.inspur.agriculture.input.service.dashboard.IDashboardService;
import com.inspur.agriculture.input.vo.dashboard.*;
import com.inspur.common.core.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 大屏数据Controller
 *
 * @author igdp
 */
@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private IDashboardService dashboardService;

    /**
     * 获取综合大屏数据(一次性获取所有数据)
     *
     * @return 综合大屏数据
     */
//    @GetMapping("/comprehensive")
//    public AjaxResult getComprehensiveDashboard() {
//        try {
//            ComprehensiveDashboardVO dashboard = dashboardService.getComprehensiveDashboard();
//            return AjaxResult.success(dashboard);
//        } catch (Exception e) {
//            return AjaxResult.error("获取大屏数据失败: " + e.getMessage());
//        }
//    }

    /**
     * 获取总览数据
     *
     * @return 总览数据
     */
    @GetMapping("/overview")
    public AjaxResult getOverview() {
        try {
            DashboardOverviewVO overview = dashboardService.getOverviewData();
            return AjaxResult.success(overview);
        } catch (Exception e) {
            return AjaxResult.error("获取总览数据失败: " + e.getMessage());
        }
    }

    /**
     * 获取供应商TOP榜单
     *
     * @param limit 数量限制(默认10)
     * @return 供应商TOP榜单
     */
    @GetMapping("/top-suppliers")
    public AjaxResult getTopSuppliers(@RequestParam(defaultValue = "10") Integer limit) {
        try {
            List<SupplierStatsVO> topSuppliers = dashboardService.getTopSuppliers(limit);
            return AjaxResult.success(topSuppliers);
        } catch (Exception e) {
            return AjaxResult.error("获取供应商TOP榜单失败: " + e.getMessage());
        }
    }

    /**
     * 获取热门投入品TOP榜单
     *
     * @param limit 数量限制(默认10)
     * @return 热门投入品TOP榜单
     */
//    @GetMapping("/top-inputs")
//    public AjaxResult getTopInputs(@RequestParam(defaultValue = "10") Integer limit) {
//        try {
//            List<InputStatsVO> topInputs = dashboardService.getTopInputs(limit);
//            return AjaxResult.success(topInputs);
//        } catch (Exception e) {
//            return AjaxResult.error("获取热门投入品TOP榜单失败: " + e.getMessage());
//        }
//    }

    /**
     * 获取仓库统计数据
     *
     * @return 仓库统计数据
     */
    @GetMapping("/warehouse-stats")
    public AjaxResult getWarehouseStats() {
        try {
            List<WarehouseStatsVO> warehouseStats = dashboardService.getWarehouseStats();
            return AjaxResult.success(warehouseStats);
        } catch (Exception e) {
            return AjaxResult.error("获取仓库统计数据失败: " + e.getMessage());
        }
    }

    /**
     * 获取入库出库趋势
     *
     * @param days 天数(7或30,默认7)
     * @return 入库出库趋势数据
     */
    @GetMapping("/stock-trend")
    public AjaxResult getStockTrend(@RequestParam(defaultValue = "7") Integer days) {
        try {
            List<StockTrendVO> stockTrend = dashboardService.getStockTrend(days);
            return AjaxResult.success(stockTrend);
        } catch (Exception e) {
            return AjaxResult.error("获取入库出库趋势失败: " + e.getMessage());
        }
    }

    /**
     * 获取投入品类型分布
     *
     * @return 投入品类型分布数据
     */
    @GetMapping("/input-type-distribution")
    public AjaxResult getInputTypeDistribution() {
        try {
            List<InputTypeDistributionVO> distribution = dashboardService.getInputTypeDistribution();
            return AjaxResult.success(distribution);
        } catch (Exception e) {
            return AjaxResult.error("获取投入品类型分布失败: " + e.getMessage());
        }
    }

    /**
     * 获取预警信息列表
     *
     * @param limit 数量限制(默认20)
     * @return 预警信息列表
     */
    @GetMapping("/warnings")
    public AjaxResult getWarnings(@RequestParam(defaultValue = "20") Integer limit) {
        try {
            List<WarningInfoVO> warnings = dashboardService.getWarningList(limit);
            return AjaxResult.success(warnings);
        } catch (Exception e) {
            return AjaxResult.error("获取预警信息失败: " + e.getMessage());
        }
    }
}
