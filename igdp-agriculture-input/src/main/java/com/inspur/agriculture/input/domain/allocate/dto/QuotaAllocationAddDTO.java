package com.inspur.agriculture.input.domain.allocate.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Quota Allocation Add DTO
 * 配额分配新增DTO
 */
@Data
public class QuotaAllocationAddDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Allocation year
     */
    private Integer year;

    /**
     * Input category ID
     */
    private String categoryId;

    /**
     * From division ID
     */
    private String fromDivisionId;

    /**
     * From division level
     */
    private Integer fromDivisionLevel;

    /**
     * To division ID
     */
    private String toDivisionId;

    /**
     * To farmer ID
     */
    private String toFarmerId;

    /**
     * Allocated quota amount
     */
    private BigDecimal allocatedQuota;

    /**
     * Related state quota ID
     */
    private String quotaId;

    /**
     * Total received quota
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
}
