package com.inspur.agriculture.input.vo.dashboard;

import lombok.Data;

/**
 * 仓库统计VO
 *
 * @author igdp
 */
@Data
public class WarehouseStatsVO {

    /** 仓库ID */
    private Long warehouseId;

    /** 仓库名称 */
    private String warehouseName;

    /** 仓库编号 */
    private String warehouseCode;

    /** 仓库位置 */
    private String location;

    /** 仓库容量 */
    private java.math.BigDecimal capacity;

    /** 当前库存数量 */
    private Long currentStock;

    /** 库存使用率(%) */
    private java.math.BigDecimal usageRate;

    /** 存储商品种类数 */
    private Long productTypeCount;

    /** 本月入库次数 */
    private Long monthStockInCount;

    /** 本月出库次数 */
    private Long monthStockOutCount;

    /** 超容量预警 */
    private Boolean capacityWarning;

    /** 拥有者 */
    private String belongs;

    /** 关联供应商 */
    private String supplierName;
}
