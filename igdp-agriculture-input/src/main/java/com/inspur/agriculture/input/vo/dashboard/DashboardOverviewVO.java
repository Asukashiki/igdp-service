package com.inspur.agriculture.input.vo.dashboard;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 大屏概览统计VO
 *
 * @author igdp
 */
@Data
public class DashboardOverviewVO {

    /** 需求申报总数 */
    private Long totalDemands;

    /** 已分配需求数 */
    private Long allocatedDemands;

    /** 需求满足率(%) */
    private BigDecimal demandSatisfactionRate;

    /** 分配完成率(%) Zone级分配完成率 */
    private BigDecimal allocationCompletionRate;

    /** 总分发量 */
    private BigDecimal totalDistributed;

    /** 农民已领用量 */
    private BigDecimal farmerReceived;

    /** 流通到达率(%) 农民领用/总分发 */
    private BigDecimal circulationArrivalRate;

    /** 认证供应商数 */
    private Long certifiedSuppliers;

    /** 待审核供应商数 */
    private Long pendingSuppliers;

    /** 活跃仓库数 */
    private Long activeWarehouses;

    /** 本年度需求总量 */
    private BigDecimal yearlyDemandQuantity;

    /** 本年度分配总量 */
    private BigDecimal yearlyAllocationQuantity;
}
