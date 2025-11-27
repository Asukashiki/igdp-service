package com.inspur.agriculture.input.vo.dashboard;

import lombok.Data;

/**
 * 投入品类型分布VO
 *
 * @author igdp
 */
@Data
public class InputTypeDistributionVO {

    /** 投入品类型 */
    private String type;

    /** 投入品类型名称 */
    private String typeName;

    /** 数量 */
    private Long count;

    /** 库存数量 */
    private Long stockQuantity;

    /** 占比(%) */
    private java.math.BigDecimal percentage;
}
