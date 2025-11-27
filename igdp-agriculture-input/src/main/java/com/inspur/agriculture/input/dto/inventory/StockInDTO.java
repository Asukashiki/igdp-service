package com.inspur.agriculture.input.dto.inventory;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * 入库单 DTO
 *
 * @author inspur
 * @date 2025-11-26
 */
@Data
public class StockInDTO {

    /** 入库单号 (更新时使用) */
    private String stockInId;

    /** 入库仓库ID */
    @NotNull(message = "入库仓库不能为空")
    private Long warehouseId;

    /** 供应商ID */
    @NotNull(message = "供应商不能为空")
    private Long supplierId;

    /** 入库类型: 0-采购入库/1-退货入库 */
    @NotBlank(message = "入库类型不能为空")
    private String type;

    /** 经办人 */
    @NotBlank(message = "经办人不能为空")
    private String operator;

    /** 备注 */
    private String remarks;

    /** 入库商品明细 */
    @NotEmpty(message = "入库商品明细不能为空")
    @Valid
    private List<StockInItemDTO> items;

    /**
     * 入库商品明细 DTO
     */
    @Data
    public static class StockInItemDTO {
        /** 投入品ID */
        @NotNull(message = "投入品不能为空")
        private Long inputId;

        /** 入库数量 */
        @NotNull(message = "入库数量不能为空")
        private Integer quantity;

        /** 过期日期 */
        @NotNull(message = "过期日期不能为空")
        @JsonFormat(pattern = "yyyy-MM-dd")
        private Date expiryDate;

        /** 备注 */
        private String remarks;
    }
}
