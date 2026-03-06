package com.inspur.agriculture.inventory.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.inspur.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * SKU表
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("inventory_sku")
public class InventorySku extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private String skuCode;
    private String skuName;
    private Long productId;
    private String unit;
    private String spec;
    private String status;
}
