package com.inspur.agriculture.input.domain.allocate.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Quota Allocation VO
 * 配额分配VO
 */
@Data
public class QuotaAllocationVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Allocation ID
     */
    private String allocationId;

    /**
     * Allocation name
     */
    private String allocationName;

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
     * From division ID
     */
    private String fromDivisionId;

    /**
     * From division name
     */
    private String fromDivisionName;

    /**
     * From division level
     */
    private Integer fromDivisionLevel;

    /**
     * From parent division ID
     */
    private String fromParentDivisionId;

    /**
     * From parent division name
     */
    private String fromParentDivisionName;

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
     * Quota ID
     */
    private String quotaId;

    /**
     * Quota name
     */
    private String quotaName;

    /**
     * Total received quota
     */
    private BigDecimal totalReceivedQuota;

    /**
     * Total allocated quota
     */
    private BigDecimal totalAllocatedQuota;

    /**
     * Remaining quota
     */
    private BigDecimal remainingQuota;

    /**
     * Allocation status
     */
    private Integer allocationStatus;

    /**
     * Allocation status name
     */
    private String allocationStatusName;

    /**
     * Operator ID
     */
    private String operatorId;

    /**
     * Operator name
     */
    private String operatorName;

    /**
     * Operator division ID
     */
    private String operatorDivisionId;

    /**
     * Operator division name
     */
    private String operatorDivisionName;

    /**
     * Modifier ID
     */
    private String modifierId;

    /**
     * Modifier name
     */
    private String modifierName;

    /**
     * Modifier division name
     */
    private String modifierDivisionName;

    /**
     * Operate time
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime operateTime;

    /**
     * Progress update time
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime progressUpdateTime;

    /**
     * Create time
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * Update time
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    /**
     * Operate time string (formatted)
     */
    private String operateTimeStr;
}
