package com.inspur.agriculture.input.vo.inventory;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 仓库容量 VO
 *
 * @author inspur
 * @date 2025-12-03
 */
@Data
public class WarehouseCapacityVO {

    /** 仓库ID */
    private String warehouseId;

    /** 仓库名称 */
    private String warehouseName;

    /** 总容量 */
    private BigDecimal totalCapacity;

    /** 当前使用量 */
    private BigDecimal currentUsage;

    /** 剩余容量 */
    private BigDecimal remainingCapacity;

    /** 使用率百分比 */
    private BigDecimal usagePercentage;

    /** 状态：normal-正常 warning-预警 full-已满 */
    private String status;
}
