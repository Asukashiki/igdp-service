package com.inspur.agriculture.input.domain.allocate.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * State Annual Quota Add DTO
 * 州级年度配额新增DTO
 */
@Data
public class StateAnnualQuotaAddDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Quota year
     */
    private Integer year;

    /**
     * Input category ID
     */
    private String categoryId;

    /**
     * Total quota amount
     */
    private BigDecimal totalQuota;

    /**
     * Operator ID
     */
    private String operatorId;

    /**
     * Operator division ID
     */
    private String operatorDivisionId;
}
