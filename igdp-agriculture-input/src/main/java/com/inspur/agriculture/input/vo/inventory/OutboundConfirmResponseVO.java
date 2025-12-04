package com.inspur.agriculture.input.vo.inventory;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 出库确认响应 VO
 *
 * @author inspur
 * @date 2025-12-03
 */
@Data
public class OutboundConfirmResponseVO {

    /** 出库单ID */
    private String outboundOrderId;

    /** 更新后的库存信息列表 */
    private List<UpdatedStockInfo> updatedStock;

    /** 批次拆分信息列表 */
    private List<BatchSplitInfo> batchSplits;

    /**
     * 更新后的库存信息
     */
    @Data
    public static class UpdatedStockInfo {

        /** 投入品ID */
        private String materialId;

        /** 仓库ID */
        private String warehouseId;

        /** 批次ID */
        private String batchId;

        /** 出库数量 */
        private BigDecimal outboundQuantity;

        /** 剩余数量 */
        private BigDecimal remainingQuantity;
    }

    /**
     * 批次拆分信息
     */
    @Data
    public static class BatchSplitInfo {

        /** 原入库批次号 */
        private String inboundBatchId;

        /** 拆分出库数量 */
        private BigDecimal splitQuantity;

        /** 该批次剩余库存 */
        private BigDecimal remainingQuantity;
    }
}
