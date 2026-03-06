package com.inspur.agriculture.inventory.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.inspur.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 溯源信息表
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("inventory_trace_info")
public class InventoryTraceInfo extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private String traceCode;
    private Long productId;
    private String productName;
    private String batchNo;
    private String supplierInfo;
    private String flowType;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date flowTime;
    private Long bizId;
    private Long inboundPartyId;
    private Long inboundWarehouseId;
    private Long outboundPartyId;
    private Long outboundWarehouseId;
}
