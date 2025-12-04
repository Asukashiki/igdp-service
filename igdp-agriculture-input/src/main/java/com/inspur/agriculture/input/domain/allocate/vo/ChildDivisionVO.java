package com.inspur.agriculture.input.domain.allocate.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Child Division VO
 * 下级区划VO (用于分配目标选择)
 */
@Data
public class ChildDivisionVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Division ID
     */
    private String divisionId;

    /**
     * Division name
     */
    private String divisionName;

    /**
     * Division code
     */
    private String divisionCode;

    /**
     * Division level (2=Zone, 3=Worede, 4=Kebele)
     */
    private Integer divisionLevel;

    /**
     * Parent division ID
     */
    private String parentDivisionId;

    /**
     * Already allocated quota to this division
     */
    private BigDecimal allocatedQuota;

    /**
     * Has allocation record (for display in UI)
     */
    private Boolean hasAllocation;

    /**
     * Existing allocation ID (if any)
     */
    private String existingAllocationId;
}
