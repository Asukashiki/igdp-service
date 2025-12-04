package com.inspur.agriculture.input.domain.allocate.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Quota Allocation Update DTO
 * 配额分配修改DTO
 */
@Data
public class QuotaAllocationUpdateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Allocation ID
     */
    private String allocationId;

    /**
     * New allocated quota amount
     */
    private BigDecimal newAllocatedQuota;

    /**
     * Modifier ID
     */
    private String modifierId;

    /**
     * Modifier division ID
     */
    private String modifierDivisionId;
}
