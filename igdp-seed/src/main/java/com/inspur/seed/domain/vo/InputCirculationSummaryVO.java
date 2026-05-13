package com.inspur.seed.domain.vo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * 投入品流转明细汇总视图
 */
@Getter
@Setter
public class InputCirculationSummaryVO {

    /** 投入品类型 */
    private String inputType;

    /** 投入品类别 */
    private String inputCategory;

    /** 计量单位 */
    private String unit;

    /** 汇总需求数量 */
    private BigDecimal required = BigDecimal.ZERO;

    /** 汇总分发数量 */
    private BigDecimal quantity = BigDecimal.ZERO;

    /** 涉及单据数量 */
    private Integer releaseCount = 0;
}
