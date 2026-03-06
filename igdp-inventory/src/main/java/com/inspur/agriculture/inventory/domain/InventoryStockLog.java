package com.inspur.agriculture.inventory.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.inspur.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 库存流水表
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("inventory_stock_log")
public class InventoryStockLog extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long skuId;
    private Long warehouseId;
    private String batchNo;
    private String changeType;
    private BigDecimal changeQty;
    private BigDecimal beforeQty;
    private BigDecimal afterQty;
    private String bizType;
    private Long bizId;
    private String bizNo;
}
