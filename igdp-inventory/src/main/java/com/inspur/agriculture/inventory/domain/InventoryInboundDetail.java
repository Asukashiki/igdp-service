package com.inspur.agriculture.inventory.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
    private String mainCategory;
    private String subCategory;
    private String batchNo;
    private String supplier;
    private BigDecimal qty;
    private String unit;
    private Date expireDate;
    
    /** 质量等级（非数据库字段，用于业务传递） */
    @TableField(exist = false)
    private String qualityGrade;
    
    /** 库存状态（非数据库字段，用于业务传递） */
    @TableField(exist = false)
    private String stockStatus;
}
