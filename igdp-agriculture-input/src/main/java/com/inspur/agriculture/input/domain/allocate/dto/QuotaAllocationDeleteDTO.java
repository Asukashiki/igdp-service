package com.inspur.agriculture.input.domain.allocate.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * Quota Allocation Delete DTO
 * 配额分配删除DTO
 */
@Data
public class QuotaAllocationDeleteDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Allocation ID
     */
    private String allocationId;

    /**
     * Operator ID
     */
    private String operatorId;

    /**
     * Operator division ID
     */
    private String operatorDivisionId;
}
