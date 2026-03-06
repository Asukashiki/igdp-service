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
 * 入库单明细表
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("inventory_inbound_order_detail")
public class InventoryInboundDetail extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long inboundId;
    private Long productId;
    private Long skuId;
    private String mainCategory;
    private String subCategory;
    private String batchNo;
    private String supplier;
    private BigDecimal planQty;
    private BigDecimal realQty;
    private String unit;
    private Date expireDate;
    
    // 冗余字段用于传递业务信息，非数据库字段
    @com.baomidou.mybatisplus.annotation.TableField(exist = false)
    private String qualityGrade;
    @com.baomidou.mybatisplus.annotation.TableField(exist = false)
    private String stockStatus;
}
