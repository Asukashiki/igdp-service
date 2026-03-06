package com.inspur.agriculture.inventory.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.inspur.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 盘点单明细表
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("inventory_check_order_detail")
public class InventoryCheckOrderDetail extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long checkId;
    private Long productId;
    private Long skuId;
    private String mainCategory;
    private String subCategory;
    private String batchNo;
    private BigDecimal bookQty;
    private BigDecimal realQty;
    private String diffType;
    private BigDecimal diffQty;
    private String unit;
}
