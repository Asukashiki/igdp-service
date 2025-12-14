package com.inspur.agriculture.input.domain.allocation.dto;

import com.inspur.agriculture.input.domain.allocation.entity.AllocationDemand;
import com.inspur.agriculture.input.domain.allocation.entity.AllocationQuota;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Zone Allocation Add DTO
 * 区域分配额度新增DTO
 */
@Data
public class AllocationAddDTO implements Serializable {

    private static final long serialVersionUID = 1L;

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
     * 区域级别
     */
    private String level;
    /**
     * Demand list
     */
    private List<AllocationDemand> demandList;

    /**
     * Quota list
     */
    private List<AllocationQuota> quotaList;
}
