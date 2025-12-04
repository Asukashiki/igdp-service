package com.inspur.seed.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Demand Pending Audit Page VO
 *
 * @author igdp
 * @date 2025-12-04
 */
@Data
public class DemandPendingPageVO {

    /**
     * Demand ID
     */
    private String id;

    /**
     * Batch Number
     */
    private String batchNo;

    /**
     * Farmer Name
     */
    private String farmerName;

    /**
     * Farmer ID Number
     */
    private String farmerIdNumber;

    /**
     * Kebele
     */
    private String kebele;

    /**
     * Woreda
     */
    private String woreda;

    /**
     * Village
     */
    private String village;

    /**
     * Land Area (hectare)
     */
    private BigDecimal landArea;

    /**
     * Current Audit Level
     */
    private String currentAuditLevel;

    /**
     * Submit Time
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date submitTime;
}
