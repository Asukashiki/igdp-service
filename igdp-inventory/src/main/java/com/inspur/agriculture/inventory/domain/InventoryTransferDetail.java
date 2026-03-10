package com.inspur.agriculture.inventory.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.inspur.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 库存调拨明细表
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("inventory_transfer_detail")
public class InventoryTransferDetail extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long transferId;

    private Long productId;

    private String productName;

    private Long inputId;

    private String inputCode;

    private String batchNo;

    private String supplier;

    private BigDecimal applyQty;

    private BigDecimal realQty;

    private String unit;
}
