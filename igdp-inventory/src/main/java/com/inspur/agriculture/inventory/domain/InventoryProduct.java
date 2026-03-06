package com.inspur.agriculture.inventory.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.inspur.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 商品表
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("inventory_product")
public class InventoryProduct extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private String productCode;
    private String productName;
    private Long categoryId;
    private String categoryName;
    private String status;
}
