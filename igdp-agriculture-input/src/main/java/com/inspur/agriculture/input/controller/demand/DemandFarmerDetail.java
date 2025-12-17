package com.inspur.agriculture.input.controller.demand;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("demand_farmer_detail")
public class DemandFarmerDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Primary Key UUID
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * Batch ID
     */
    private String batchId;

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
     * Region
     */
    private String region;

    /**
     * Zone
     */
    private String zone;

    /**
     * Woreda
     */
    private String woreda;

    /**
     * Kebele
     */
    private String kebele;

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
     * Max Seed Quantity (kg)
     */
    private BigDecimal maxSeedQuantity;

    /**
     * Max Fertilizer Quantity (kg)
     */
    private BigDecimal maxFertilizerQuantity;

    /**
     * Status (draft/submitted/approved/rejected)
     */
    private String status;

    /**
     * Current Audit Level (village/town/district/state/ministry)
     */
    private String currentAuditLevel;

    /**
     * DA User ID
     */
    private String daUserId;

    /**
     * DA User Name
     */
    private String daUserName;

    /**
     * Submit Time
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date submitTime;

    /**
     * Created By User ID
     */
    private String createdBy;

    /**
     * Created Time
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;

    /**
     * Updated By User ID
     */
    private String updatedBy;

    /**
     * Updated Time
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedTime;

    /**
     * Is Deleted (0: No, 1: Yes)
     */
    @TableLogic
    private Integer isDeleted;

    /**
     * Version (Optimistic Lock)
     */
    @Version
    private Integer version;

    /**
     * Remark
     */
    private String remark;

    private String year;
}
