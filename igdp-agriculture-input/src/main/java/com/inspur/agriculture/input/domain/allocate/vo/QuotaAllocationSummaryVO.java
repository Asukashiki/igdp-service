package com.inspur.agriculture.input.domain.allocate.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * Quota Allocation Summary VO
 * 配额分配汇总VO
 */
@Data
public class QuotaAllocationSummaryVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * State quota ID
     */
    private String quotaId;

    /**
     * Quota name
     */
    private String quotaName;

    /**
     * Year
     */
    private Integer year;

    /**
     * Category ID
     */
    private String categoryId;

    /**
     * Category name
     */
    private String categoryName;

    /**
     * Total quota amount
     */
    private BigDecimal totalQuota;

    /**
     * Total allocated amount
     */
    private BigDecimal totalAllocated;

    /**
     * Remaining amount
     */
    private BigDecimal remainingQuota;

    /**
     * Allocation status (0=Not Allocated, 1=Partially Allocated, 2=Completed)
     */
    private Integer allocationStatus;

    /**
     * Allocation status name
     */
    private String allocationStatusName;

    /**
     * Allocation progress percentage (0-100)
     */
    private BigDecimal progressPercent;

    /**
     * Number of children allocated to
     */
    private Integer allocatedChildrenCount;

    /**
     * Total number of children
     */
    private Integer totalChildrenCount;

    /**
     * List of allocation details
     */
    private List<AllocationDetail> allocations;

    /**
     * Allocation detail
     */
    @Data
    public static class AllocationDetail implements Serializable {
        private static final long serialVersionUID = 1L;

        /**
         * Allocation ID
         */
        private String allocationId;

        /**
         * To division ID
         */
        private String toDivisionId;

        /**
         * To division name
         */
        private String toDivisionName;

        /**
         * To farmer ID
         */
        private String toFarmerId;

        /**
         * To farmer name
         */
        private String toFarmerName;

        /**
         * Allocated quota
         */
        private BigDecimal allocatedQuota;

        /**
         * Sub-allocation progress (how much the receiver has allocated to its children)
         */
        private BigDecimal subAllocationProgress;

        /**
         * Allocation time
         */
        private String operateTime;
    }
}
