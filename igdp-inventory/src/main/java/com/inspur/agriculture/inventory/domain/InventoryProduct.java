package com.inspur.agriculture.inventory.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.inspur.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

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
    private String mainCategory;
    private String subCategory;
    private String brand;
    private String model;
    private String unit;
    private BigDecimal price;
    private String licenseNo;
    private String status;
    private String remark;
    @TableField(exist = false)
    private String keyword;
}
