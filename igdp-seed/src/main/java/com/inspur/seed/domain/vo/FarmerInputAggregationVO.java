package com.inspur.seed.domain.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Farmer Input Aggregation VO
 * Used for aggregated statistics of farmer input items
 *
 * @author igdp
 * @date 2025-12-09
 */
@Data
public class FarmerInputAggregationVO {

    /**
     * Input Category (seed/fertilizer/pesticide)
     */
    private String inputCategory;

    /**
     * Input Type
     */
    private String inputType;

    private String variety;

    /**
     * Total Count of Items
     */
    private Integer totalCount;

    /**
     * Total Quantity
     */
    private BigDecimal totalQuantity;

    private String farmerName;
}
