package com.inspur.agriculture.input.vo.dashboard;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 需求汇聚统计VO
 *
 * @author igdp
 */
@Data
public class DemandSummaryVO {

    /** 投入品类型: seed/fertilizer/pesticide */
    private String inputType;

    /** 投入品类型描述 */
    private String inputTypeDesc;

    /** 投入品类别 */
    private String inputCategory;

    /** 投入品类别描述 */
    private String inputCategoryDesc;

    /** 需求数量 */
    private BigDecimal demandQuantity;

    /** 需求单位 */
    private String unit;

    /** 需求笔数 */
    private Long demandCount;

    /** 已分配数量 */
    private BigDecimal allocatedQuantity;

    /** 分配率(%) */
    private BigDecimal allocationRate;

    /** 地区编码 */
    private String regionCode;

    /** 地区名称 */
    private String regionName;
}
