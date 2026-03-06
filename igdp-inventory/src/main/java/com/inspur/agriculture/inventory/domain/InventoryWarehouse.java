package com.inspur.agriculture.inventory.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.inspur.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 仓库表
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("inventory_warehouse")
public class InventoryWarehouse extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private String warehouseCode;
    private String warehouseName;
    private String type;
    private String address;
    private String status;
}
