package com.inspur.agriculture.inventory.vo;

import com.inspur.agriculture.inventory.domain.InventoryTransferDetail;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 调拨单VO
 */
@Data
public class TransferVO {
    private Long id;

    private String transferNo;

    private String transferType;

    private Date applyDate;

    private Date expectedDate;

    private String applicant;

    private String department;

    private String outWarehouseCode;

    private String outWarehouseName;

    private Date outTime;

    private String inWarehouseCode;

    private String inWarehouseName;

    private Date inTime;

    private String status;

    private String remark;

    private String auditBy;

    private Date auditTime;

    private String auditComment;

    private Long relatedOutboundId;

    private Long relatedInboundId;

    private Date createTime;

    private List<InventoryTransferDetail> detailList;
}
