package com.inspur.agriculture.inventory.dto;

import com.inspur.agriculture.inventory.domain.InventoryTransferDetail;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 调拨单DTO
 */
@Data
public class TransferDTO {
    private Long id;

    private String transferNo;

    private String transferType;

    private Date applyDate;

    private Date expectedDate;

    private String applicant;

    private String department;

    private Long outWarehouseId;

    private String outWarehouseName;

    private Long inWarehouseId;

    private String inWarehouseName;

    private String status;

    private String remark;

    private Date startDate;

    private Date endDate;

    private List<InventoryTransferDetail> detailList;
}
