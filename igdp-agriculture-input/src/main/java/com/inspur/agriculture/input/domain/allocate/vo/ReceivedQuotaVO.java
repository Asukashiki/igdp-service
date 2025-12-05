package com.inspur.agriculture.input.domain.allocate.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Received Quota VO
 * 接收配额VO (显示当前区划接收到的配额)
 */
@Data
public class ReceivedQuotaVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Allocation ID (the record where this division is the receiver)
     */
    private String allocationId;

    /**
     * Related state quota ID
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
     * From division ID (who allocated to us)
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
     * Total received quota
     */
    private BigDecimal receivedQuota;

    /**
     * Total allocated to children
     */
    private BigDecimal allocatedToChildren;

    /**
     * Remaining quota to allocate
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
     * Allocation progress percentage
     */
    private BigDecimal progressPercent;

    /**
     * Number of children allocated to
     */
    private Integer allocatedChildrenCount;

    /**
     * Receive time
     */
    private String receiveTime;

    /**
     * Current division level
     */
    private Integer currentDivisionLevel;
}
