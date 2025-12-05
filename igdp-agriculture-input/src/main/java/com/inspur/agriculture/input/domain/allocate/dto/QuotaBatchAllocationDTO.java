package com.inspur.agriculture.input.domain.allocate.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * Quota Batch Allocation DTO
 * 批量配额分配DTO
 */
@Data
public class QuotaBatchAllocationDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * State quota ID (the source quota)
     */
    private String quotaId;

    /**
     * Allocation year
     */
    private Integer year;

    /**
     * Input category ID
     */
    private String categoryId;

    /**
     * From division ID (allocator)
     */
    private String fromDivisionId;

    /**
     * From division level (1=Region, 2=Zone, 3=Worede, 4=Kebele)
     */
    private Integer fromDivisionLevel;

    /**
     * Total received quota by the allocator
     */
    private BigDecimal totalReceivedQuota;

    /**
     * Operator ID
     */
    private String operatorId;

    /**
     * Operator division ID
     */
    private String operatorDivisionId;

    /**
     * Allocation items (list of receivers and amounts)
     */
    private List<AllocationItem> items;

    /**
     * Single allocation item
     */
    @Data
    public static class AllocationItem implements Serializable {
        private static final long serialVersionUID = 1L;

        /**
         * To division ID (for zone/worede/kebele level)
         */
        private String toDivisionId;

        /**
         * To division name
         */
        private String toDivisionName;

        /**
         * To farmer ID (for kebele allocating to farmers)
         */
        private String toFarmerId;

        /**
         * To farmer name
         */
        private String toFarmerName;

        /**
         * Allocated quota amount
         */
        private BigDecimal allocatedQuota;
    }
}
