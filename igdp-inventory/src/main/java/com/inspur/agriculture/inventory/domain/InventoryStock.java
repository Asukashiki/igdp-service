package com.inspur.agriculture.inventory.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.inspur.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 库存总表
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("inventory_stock")
public class InventoryStock extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long productId;
    private Long warehouseId;
//    private String mainCategory;
//    private String subCategory;
    private BigDecimal availableQty;
    private BigDecimal lockedQty;
    private String qualityGrade;
    private String stockStatus;
    private String remark;

    @Version
    private Long version;

    @com.baomidou.mybatisplus.annotation.TableField(exist = false)
    private String productName;
    @com.baomidou.mybatisplus.annotation.TableField(exist = false)
    private String warehouseName;
    @com.baomidou.mybatisplus.annotation.TableField(exist = false)
    private String productCode;
    @com.baomidou.mybatisplus.annotation.TableField(exist = false)
    private String skuCode;
    @com.baomidou.mybatisplus.annotation.TableField(exist = false)
    private String unit;
    @com.baomidou.mybatisplus.annotation.TableField(exist = false)
    private Long batchId;
    @com.baomidou.mybatisplus.annotation.TableField(exist = false)
    private String batchNo;
    private String mainCategory;
    @com.baomidou.mybatisplus.annotation.TableField(exist = false)
    private String subCategory;
}
