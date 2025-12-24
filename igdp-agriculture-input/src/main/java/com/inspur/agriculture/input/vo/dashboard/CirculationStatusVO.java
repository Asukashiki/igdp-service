package com.inspur.agriculture.input.vo.dashboard;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 流通状态VO
 *
 * @author igdp
 */
@Data
public class CirculationStatusVO {

    /** OSE分发总量 */
    private BigDecimal oseDistributedQuantity;

    /** OSE分发单数 */
    private Long oseDistributedCount;

    /** Union已接收量 */
    private BigDecimal unionReceivedQuantity;

    /** Union接收率(%) */
    private BigDecimal unionReceiveRate;

    /** Woreda已接收量 */
    private BigDecimal woredaReceivedQuantity;

    /** Woreda接收率(%) */
    private BigDecimal woredaReceiveRate;

    /** 农民已领用量 */
    private BigDecimal farmerReceivedQuantity;

    /** 农民领用率(%) */
    private BigDecimal farmerReceiveRate;

    /** 待Union接收量 */
    private BigDecimal pendingUnionReceive;

    /** 待Woreda接收量 */
    private BigDecimal pendingWoredaReceive;

    /** 待农民领用量 */
    private BigDecimal pendingFarmerReceive;
}
