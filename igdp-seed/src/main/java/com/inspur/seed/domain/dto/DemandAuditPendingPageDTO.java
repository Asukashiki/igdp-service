package com.inspur.seed.domain.dto;

import lombok.Data;

/**
 * Demand Audit Pending Page Query DTO
 *
 * @author igdp
 * @date 2025-12-04
 */
@Data
public class DemandAuditPendingPageDTO {

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
}
