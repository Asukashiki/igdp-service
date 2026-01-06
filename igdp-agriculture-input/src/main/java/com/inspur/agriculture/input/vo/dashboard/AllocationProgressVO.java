package com.inspur.agriculture.input.vo.dashboard;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 分配进度VO
 *
 * @author igdp
 */
@Data
public class AllocationProgressVO {

    /** Zone级分配总数 */
    private Long zoneAllocationCount;

    /** Zone级分配完成数 */
    private Long zoneCompletedCount;

    /** Zone级完成率 */
    private BigDecimal zoneCompletionRate;

    /** Woreda级分配总数 */
    private Long woredaAllocationCount;

    /** Woreda级分配完成数 */
    private Long woredaCompletedCount;

    /** Woreda级完成率 */
    private BigDecimal woredaCompletionRate;

    /** Kebele级分配总数 */
    private Long kebeleAllocationCount;

    /** Kebele级分配完成数 */
    private Long kebeleCompletedCount;

    /** Kebele级完成率 */
    private BigDecimal kebeleCompletionRate;

    /** Farmer级分配总数 */
    private Long farmerAllocationCount;

    /** Farmer级分配完成数 */
    private Long farmerCompletedCount;

    /** Farmer级完成率 */
    private BigDecimal farmerCompletionRate;

    /** 总体完成率 */
    private BigDecimal overallCompletionRate;
}
