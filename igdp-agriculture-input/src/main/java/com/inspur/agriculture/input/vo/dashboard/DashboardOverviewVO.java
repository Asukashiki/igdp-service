package com.inspur.agriculture.input.vo.dashboard;

import lombok.Data;

/**
 * 大屏总览数据VO
 *
 * @author igdp
 */
@Data
public class DashboardOverviewVO {

    /** 供应商总数 */
    private Long totalSuppliers;

    /** 已认证供应商数 */
    private Long certifiedSuppliers;

    /** 待审核供应商数 */
    private Long pendingSuppliers;

    /** 未通过供应商数 */
    private Long rejectedSuppliers;

    /** 投入品总数 */
    private Long totalInputs;

    /** 种子类投入品数 */
    private Long seedInputs;

    /** 化肥类投入品数 */
    private Long fertilizerInputs;

    /** 农药类投入品数 */
    private Long pesticideInputs;

    /** 仓库总数 */
    private Long totalWarehouses;

    /** 总库存容量 */
    private java.math.BigDecimal totalCapacity;

    /** 已用库存容量 */
    private java.math.BigDecimal usedCapacity;

    /** 库存使用率(%) */
    private java.math.BigDecimal capacityUsageRate;

    /** 当前总库存数量 */
    private Long totalStockQuantity;

    /** 正常库存商品数 */
    private Long normalStockCount;

    /** 临期库存商品数 */
    private Long nearExpiryStockCount;

    /** 过期库存商品数 */
    private Long expiredStockCount;

    /** 今日入库单数 */
    private Long todayStockInCount;

    /** 今日出库单数 */
    private Long todayStockOutCount;

    /** 本月入库总量 */
    private Long monthStockInQuantity;

    /** 本月出库总量 */
    private Long monthStockOutQuantity;

    /** 待处理预警数 */
    private Long pendingWarnings;
}
