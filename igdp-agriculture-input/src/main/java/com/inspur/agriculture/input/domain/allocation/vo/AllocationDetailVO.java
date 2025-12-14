package com.inspur.agriculture.input.domain.allocation.vo;

import com.inspur.agriculture.input.domain.allocation.entity.AllocationDemand;
import com.inspur.agriculture.input.domain.allocation.entity.AllocationQuota;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Zone Allocation Detail VO
 * 区域分配额度详情VO
 */
@Data
public class AllocationDetailVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Main allocation information
     */
    private AllocationVO main;

    /**
     * Demand list
     */
    private List<AllocationDemand> demandList;

    /**
     * Quota list
     */
    private List<AllocationQuota> quotaList;
}
