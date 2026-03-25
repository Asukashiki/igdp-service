package com.inspur.agriculture.input.vo.demand;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 农资汇聚统计汇总 VO
 * 用于二次汇聚统计（从汇聚统计表中再次汇总）
 *
 * @author inspur
 * @date 2025-12-10
 */
@Data
public class InputAggregationSummaryVO {

    /**
     * 季节
     */
    private String season;

    /**
     * 农资分类
     */
    private String inputCategory;

    private String variety;

    /**
     * 农资类型
     */
    private String inputType;

    /**
     * 总项目数
     */
    private Integer totalCount;

    /**
     * 总数量
     */
    private BigDecimal totalQuantity;
}
