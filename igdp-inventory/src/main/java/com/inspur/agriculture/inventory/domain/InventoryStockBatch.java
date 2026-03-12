package com.inspur.agriculture.inventory.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.inspur.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 批次库存表
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("inventory_stock_batch")
public class InventoryStockBatch extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long stockId;
    private Long productId;
    private Long warehouseId;
    private String batchNo;
    private String mainCategory;
    private String subCategory;
    private BigDecimal qty;
    private String unit;
    private Date productionDate;
    private Date expireDate;
    private String qualityGrade;
    private String stockStatus;
}
