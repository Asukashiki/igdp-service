package com.inspur.agriculture.inventory.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.inspur.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 出库单主表
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("inventory_outbound")
public class InventoryOutbound extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long warehouseId;
    private String type;
    private String status;

    /** 明细列表 */
    @com.baomidou.mybatisplus.annotation.TableField(exist = false)
    private List<InventoryOutboundDetail> detailList;
}
