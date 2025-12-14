package com.inspur.agriculture.input.domain.allocation.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * Zone Allocation Delete DTO
 * 区域分配额度删除DTO
 */
@Data
public class AllocationDeleteDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Allocation ID
     */
    private String id;
}
