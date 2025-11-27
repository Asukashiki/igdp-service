package com.inspur.agriculture.input.dto.inventory;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 出库单 DTO
 *
 * @author inspur
 * @date 2025-11-26
 */
@Data
public class StockOutDTO {

    /** 出库单号 (更新时使用) */
    private String stockOutId;

    /** 出库仓库ID */
    @NotNull(message = "出库仓库不能为空")
    private Long warehouseId;

    /** 出库类型: 0-销售出库 */
    @NotBlank(message = "出库类型不能为空")
    private String type;

    /** 经办人 */
    @NotBlank(message = "经办人不能为空")
    private String operator;

    /** 客户 */
    @NotBlank(message = "客户不能为空")
    private String customer;

    /** 备注 */
    private String remark;

    /** 出库商品明细 */
    @NotEmpty(message = "出库商品明细不能为空")
    @Valid
    private List<StockOutItemDTO> items;

    /**
     * 出库商品明细 DTO
     */
    @Data
    public static class StockOutItemDTO {
        /** 投入品ID */
        @NotNull(message = "投入品不能为空")
        private Long inputId;

        /** 批次号 */
        @NotBlank(message = "批次号不能为空")
        private String batchNo;

        /** 出库数量 */
        @NotNull(message = "出库数量不能为空")
        private Integer quantity;

        /** 备注 */
        private String remarks;
    }
}
