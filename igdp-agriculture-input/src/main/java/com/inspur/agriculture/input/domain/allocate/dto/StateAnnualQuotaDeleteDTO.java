package com.inspur.agriculture.input.domain.allocate.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * State Annual Quota Delete DTO
 * 州级年度配额删除DTO
 */
@Data
public class StateAnnualQuotaDeleteDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Quota ID
     */
    private String quotaId;

    /**
     * Operator ID
     */
    private String operatorId;

    /**
     * Operator division ID
     */
    private String operatorDivisionId;
}
