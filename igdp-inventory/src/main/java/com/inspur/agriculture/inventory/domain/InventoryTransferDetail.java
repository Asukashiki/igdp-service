package com.inspur.agriculture.inventory.domain;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.inspur.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 库存调拨明细表
 */
@Data
@EqualsAndHashCode(callSuper = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@TableName("inventory_transfer_detail")
public class InventoryTransferDetail extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL)
    private Long transferId;

    @TableField(insertStrategy = FieldStrategy.IGNORED)
    private Long productId;

    private String productName;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL)
    private String mainCategory;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL)
    private String subCategory;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL)
    private String batchNo;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL)
    private String supplier;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL)
    private BigDecimal qty;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL)
    private String unit;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL)
    private Date expireDate;
}
