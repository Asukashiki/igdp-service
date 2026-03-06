package com.inspur.agriculture.inventory.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.inspur.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 入库单明细表
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("inventory_inbound_detail")
public class InventoryInboundDetail extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long inboundId;
    private Long skuId;
    private String batchNo;
    private BigDecimal planQty;
    private BigDecimal realQty;
}
