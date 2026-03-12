package com.inspur.agriculture.inventory.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class WarehouseInventoryItemVO {
    private String productId;
    private String productName;
    private String categoryMajor;
    private String categoryMinor;
    private String batchNo;
    private String batchId;
    private String unit;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date expiryDate;

    private String qualityStatus;
    private BigDecimal currentQty;
}
