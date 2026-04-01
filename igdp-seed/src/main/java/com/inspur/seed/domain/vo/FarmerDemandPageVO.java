package com.inspur.seed.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Farmer Demand Page VO
 *
 * @author igdp
 * @date 2025-12-04
 */
@Data
public class FarmerDemandPageVO {

    /**
     * Demand ID
     */
    private String id;

    /**
     * Batch Number
     */
    private String batchNo;

    /**
     * Farmer ID
     */
    private String farmerId;

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
     * Zone Name
     */
    private String zoneName;

    /**
     * Woreda Name
     */
    private String woredaName;

    /**
     * Kebele Name
     */
    private String kebeleName;

    /**
     * Village
     */
    private String village;

    /**
     * Land Area (hectare)
     */
    private BigDecimal landArea;

    /**
     * Demand Entry Type (WHOLE_DEMAND/BY_FARMERS)
     */
    private String demandEntryType;

    /**
     * Status
     */
    private String status;

    /**
     * Status Name
     */
    private String statusName;

    /**
     * Current Audit Level
     */
    private String currentAuditLevel;

    /**
     * DA User Name
     */
    private String daUserName;

    /**
     * Created Time
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;

    private String year;
}
