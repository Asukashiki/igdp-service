package com.inspur.seed.domain.dto;

import lombok.Data;

/**
 * Farmer Demand Page Query DTO
 *
 * @author igdp
 * @date 2025-12-04
 */
@Data
public class FarmerDemandPageDTO {

    /**
     * Current Page
     */
    private Integer pageNum = 1;

    /**
     * Page Size
     */
    private Integer pageSize = 10;

    /**
     * Batch ID
     */
    private String batchId;

    /**
     * Farmer Name (fuzzy query)
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
     * Zone
     */
    private String zone;

    /**
     * Village
     */
    private String village;

    /**
     * Status
     */
    private String status;

    /**
     * Current Audit Level
     */
    private String currentAuditLevel;

    /**
     * Input Category
     */
    private String inputCategory;

    /**
     * Created Time Start
     */
    private String createdTimeStart;

    /**
     * Created Time End
     */
    private String createdTimeEnd;

    /**
     * 排序字段
     */
    private String orderByColumn;

    /**
     * 排序方向 (asc/desc)
     */
    private String isAsc;
}
