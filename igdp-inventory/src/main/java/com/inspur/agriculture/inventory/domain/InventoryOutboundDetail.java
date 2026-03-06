package com.inspur.agriculture.inventory.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.inspur.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 出库单明细表
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("inventory_outbound_detail")
public class InventoryOutboundDetail extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long outboundId;
    private Long skuId;
    private String batchNo;
    private BigDecimal applyQty;
    private BigDecimal realQty;
}
