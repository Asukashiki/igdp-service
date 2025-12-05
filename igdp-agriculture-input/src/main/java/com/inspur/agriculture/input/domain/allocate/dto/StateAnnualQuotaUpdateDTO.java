package com.inspur.agriculture.input.domain.allocate.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * State Annual Quota Update DTO
 * 州级年度配额修改DTO
 */
@Data
public class StateAnnualQuotaUpdateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Quota ID
     */
    private String quotaId;

    /**
     * Total quota amount
     */
    private BigDecimal totalQuota;

    /**
     * Modifier ID
     */
    private String modifierId;

    /**
     * Modifier division ID
     */
    private String modifierDivisionId;
}
