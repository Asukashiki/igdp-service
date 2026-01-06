package com.inspur.agriculture.input.vo.demand;

import lombok.Data;

import java.math.BigDecimal;


@Data
public class SummaryVo {
    /**
     * Input Category (seed/fertilizer/pesticide)
     */
    private String inputCategory;

    /**
     * Input Type
     */
    private String inputType;

    /**
     * Total Count of Items
     */
    private Integer totalCount;

    /**
     * Total Quantity
     */
    private BigDecimal totalQuantity;
}
