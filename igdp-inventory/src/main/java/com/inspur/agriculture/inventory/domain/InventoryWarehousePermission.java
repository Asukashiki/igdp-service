package com.inspur.agriculture.inventory.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.inspur.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 仓库使用权表（部门权限）
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("inventory_warehouse_permission")
public class InventoryWarehousePermission extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long warehouseId;
    private String deptId;
    private String deptName;
    private String allowInbound;
    private String allowOutbound;
    private String allowTransfer;
    private String allowAdjust;
    private String allowView;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    private String status;
    private Long assignerId;
    private String assignerName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime assignTime;

    private String assignReason;
    private String remark;
}
