package com.inspur.agriculture.input.service.dashboard;

import com.inspur.agriculture.input.vo.dashboard.*;

import java.util.List;

/**
 * 大屏数据Service接口
 *
 * @author igdp
 */
public interface IDashboardService {

    /**
     * 获取综合大屏数据
     */
    ComprehensiveDashboardVO getComprehensiveDashboard();

    /**
     * 获取总览数据
     */
    DashboardOverviewVO getOverviewData();

    /**
     * 获取供应商统计TOP N
     */
    List<SupplierStatsVO> getTopSuppliers(Integer limit);

    /**
     * 获取投入品统计TOP N
     */
    List<InputStatsVO> getTopInputs(Integer limit);

    /**
     * 获取仓库统计数据
     */
    List<WarehouseStatsVO> getWarehouseStats();

    /**
     * 获取入库出库趋势
     *
     * @param days 天数(7或30)
     */
    List<StockTrendVO> getStockTrend(Integer days);

    /**
     * 获取投入品类型分布
     */
    List<InputTypeDistributionVO> getInputTypeDistribution();

    /**
     * 获取预警信息列表
     */
    List<WarningInfoVO> getWarningList(Integer limit);

    /**
     * 获取今日出入库列表
     */
    List<TodayStockVO> getTodayStockList();

    /**
     * 获取即将过期列表
     *
     * @param limit 数量限制
     */
    List<ExpiringSoonVO> getExpiringSoonList(Integer limit);

    /**
     * 获取库存状态分布
     */
    List<StockStatusDistributionVO> getStockStatusDistribution();
}
