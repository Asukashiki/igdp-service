package com.inspur.agriculture.input.vo.inventory;

import lombok.Data;

import java.util.List;

/**
 * 入库确认响应 VO
 *
 * @author inspur
 * @date 2025-12-03
 */
@Data
public class InboundConfirmResponseVO {

    /** 入库单ID */
    private String inboundOrderId;

    /** 更新后的库存信息列表 */
    private List<UpdatedStockInfo> updatedStock;

    /**
     * 更新后的库存信息
     */
    @Data
    public static class UpdatedStockInfo {

        /** 投入品ID */
        private String materialId;

        /** 仓库ID */
        private String warehouseId;

        /** 新的库存数量 */
        private Integer newQuantity;

        /** 批次ID */
        private String batchId;
    }
}
