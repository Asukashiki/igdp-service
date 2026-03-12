package com.inspur.agriculture.inventory.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.inspur.agriculture.inventory.domain.InventoryTransferDetail;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 调拨单DTO
 */
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TransferDTO {
    private Long id;
    private String transferNo;
    private String transferType;
    private Date applyDate;
    private Date expectedDate;
    private String applicant;
    private String department;
    private String outWarehouseCode;
    private String outWarehouseName;
    private String inWarehouseCode;
    private String inWarehouseName;
    private String status;
    private String remark;
    private Date startDate;
    private Date endDate;
    private List<InventoryTransferDetail> detailList;
}
