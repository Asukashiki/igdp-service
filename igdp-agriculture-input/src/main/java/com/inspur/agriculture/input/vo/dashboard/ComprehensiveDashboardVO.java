package com.inspur.agriculture.input.vo.dashboard;

import lombok.Data;

import java.util.List;

/**
 * 大屏综合数据VO
 *
 * @author igdp
 */
@Data
public class ComprehensiveDashboardVO {

    /** 总览数据 */
    private DashboardOverviewVO overview;

    /** 投入品类型分布 */
    private List<InputTypeDistributionVO> inputTypeDistribution;

    /** 供应商TOP10 */
    private List<SupplierStatsVO> topSuppliers;

    /** 热门投入品TOP10 */
    private List<InputStatsVO> topInputs;

    /** 仓库使用率统计 */
    private List<WarehouseStatsVO> warehouseStats;

    /** 近7天入库出库趋势 */
    private List<StockTrendVO> stockTrend7Days;

    /** 近30天入库出库趋势 */
    private List<StockTrendVO> stockTrend30Days;

    /** 预警信息列表 */
    private List<WarningInfoVO> warnings;
}
