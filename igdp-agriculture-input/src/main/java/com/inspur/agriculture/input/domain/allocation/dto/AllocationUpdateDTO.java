package com.inspur.agriculture.input.domain.allocation.dto;

import com.inspur.agriculture.input.domain.allocation.entity.AllocationDemand;
import com.inspur.agriculture.input.domain.allocation.entity.AllocationQuota;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Zone Allocation Update DTO
 * 区域分配额度更新DTO
 */
@Data
public class AllocationUpdateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Allocation ID
     */
    private String id;

    /**
     * Allocation name
     */
    private String allocationName;

    /**
     * Allocation year
     */
    private String year;

    /**
 * Zone code
 */
private String zone;

/**
 * Zone name
 */
private String zoneName;

/**
 * Demand list
 */
private List<AllocationDemand> demandList;

    /**
     * Quota list
     */
    private List<AllocationQuota> quotaList;
}
