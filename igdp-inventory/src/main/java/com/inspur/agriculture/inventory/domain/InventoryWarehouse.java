package com.inspur.agriculture.inventory.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import com.inspur.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.List;

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
    private String storeType;
    private String orgName;
    private String adminLevel;
    private Long parentId;
    private String location;
    private BigDecimal capacity;
    private BigDecimal maxStock;
    private String address;
    private String status;
    private String remark;

    @TableField(exist = false)
    private String parentWarehouseName;

    @TableField(exist = false)
    private List<Long> ids;
}
