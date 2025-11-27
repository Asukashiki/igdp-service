package com.inspur.agriculture.input.vo.dashboard;

import lombok.Data;

/**
 * 入库出库趋势VO
 *
 * @author igdp
 */
@Data
public class StockTrendVO {

    /** 日期 */
    private String date;

    /** 入库数量 */
    private Long stockInQuantity;

    /** 入库单数 */
    private Long stockInCount;

    /** 出库数量 */
    private Long stockOutQuantity;

    /** 出库单数 */
    private Long stockOutCount;

    /** 净增库存 */
    private Long netStockChange;
}
