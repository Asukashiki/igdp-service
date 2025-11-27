package com.inspur.agriculture.input.vo.dashboard;

import lombok.Data;

/**
 * 库存状态分布VO
 *
 * @author igdp
 */
@Data
public class StockStatusDistributionVO {

    /** 库存状态: 0-正常/1-临期/2-过期 */
    private String stockStatus;

    /** 库存状态描述 */
    private String stockStatusDesc;

    /** 商品种类数 */
    private Long productCount;

    /** 库存总数量 */
    private Long totalQuantity;

    /** 占比(%) */
    private java.math.BigDecimal percentage;
}
