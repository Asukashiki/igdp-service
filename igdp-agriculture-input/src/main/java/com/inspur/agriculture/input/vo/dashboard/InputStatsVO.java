package com.inspur.agriculture.input.vo.dashboard;

import lombok.Data;

/**
 * 投入品统计VO
 *
 * @author igdp
 */
@Data
public class InputStatsVO {

    /** 投入品ID */
    private Long inputId;

    /** 投入品名称 */
    private String inputName;

    /** 投入品类型 */
    private String type;

    /** 投入品类型描述 */
    private String typeDesc;

    /** SKU */
    private String inputSku;

    /** 当前库存数量 */
    private Long currentStock;

    /** 本月入库数量 */
    private Long monthStockIn;

    /** 本月出库数量 */
    private Long monthStockOut;

    /** 周转率 */
    private java.math.BigDecimal turnoverRate;

    /** 供应商数量 */
    private Long supplierCount;

    /** 库存状态(0-正常/1-临期/2-过期) */
    private String stockStatus;

    /** 库存状态描述 */
    private String stockStatusDesc;

    /** 预警标识 */
    private Boolean warningFlag;
}
