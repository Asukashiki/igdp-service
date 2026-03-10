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
import java.util.List;

/**
 * 库存调拨主表
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("inventory_transfer")
public class InventoryTransfer extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private String transferNo;

    private String transferType;

    private Date applyDate;

    private Date expectedDate;

    private String applicant;

    private String department;

    private Long outWarehouseId;

    private String outWarehouseName;

    private Date outTime;

    private Long inWarehouseId;

    private String inWarehouseName;

    private Date inTime;

    private String status;

    private String remark;

    private String auditBy;

    private Date auditTime;

    private String auditComment;

    private Long relatedOutboundId;

    private Long relatedInboundId;

    @TableField(exist = false)
    private List<InventoryTransferDetail> detailList;
}
