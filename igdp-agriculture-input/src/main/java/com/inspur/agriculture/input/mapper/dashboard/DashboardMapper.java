package com.inspur.agriculture.input.mapper.dashboard;

import com.inspur.agriculture.input.vo.dashboard.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 大屏数据Mapper接口
 *
 * @author igdp
 */
public interface DashboardMapper {

    /**
     * 获取总览数据
     */
    DashboardOverviewVO getOverviewData();

    /**
     * 获取供应商统计TOP N
     */
    List<SupplierStatsVO> getTopSuppliers(@Param("limit") Integer limit);

    /**
     * 获取投入品统计TOP N
     */
    List<InputStatsVO> getTopInputs(@Param("limit") Integer limit);

    /**
     * 获取仓库统计数据
     */
    List<WarehouseStatsVO> getWarehouseStats();

    /**
     * 获取入库出库趋势(近N天)
     */
    List<StockTrendVO> getStockTrend(@Param("days") Integer days);

    /**
     * 获取投入品类型分布
     */
    List<InputTypeDistributionVO> getInputTypeDistribution();

    /**
     * 获取预警信息列表
     */
    List<WarningInfoVO> getWarningList(@Param("limit") Integer limit);

    /**
     * 获取供应商数量统计
     */
    Long getTotalSuppliers();

    /**
     * 获取已认证供应商数
     */
    Long getCertifiedSuppliers();

    /**
     * 获取待审核供应商数
     */
    Long getPendingSuppliers();

    /**
     * 获取未通过供应商数
     */
    Long getRejectedSuppliers();

    /**
     * 获取今日入库单数
     */
    Long getTodayStockInCount();

    /**
     * 获取今日出库单数
     */
    Long getTodayStockOutCount();

    /**
     * 获取本月入库总量
     */
    Long getMonthStockInQuantity();

    /**
     * 获取本月出库总量
     */
    Long getMonthStockOutQuantity();

    /**
     * 获取今日出入库列表
     */
    List<TodayStockVO> getTodayStockList();

    /**
     * 获取即将过期列表
     */
    List<ExpiringSoonVO> getExpiringSoonList(@Param("limit") Integer limit);

    /**
     * 获取库存状态分布
     */
    List<StockStatusDistributionVO> getStockStatusDistribution();
}
