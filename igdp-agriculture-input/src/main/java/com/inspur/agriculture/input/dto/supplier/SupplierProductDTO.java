package com.inspur.agriculture.input.dto.supplier;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 供应商投入品添加/编辑DTO
 *
 * @author igdp
 */
@Data
public class SupplierProductDTO {

    /** 供应关系ID（编辑时必填） */
    private Long supplierProductId;

    /** 供应商ID */
    @NotNull(message = "供应商ID不能为空")
    private Long supplierId;

    /** 投入品ID */
    @NotNull(message = "投入品ID不能为空")
    private Long inputId;

    /** 供应商产品编码 */
    private String supplierProductCode;

    /** 供应商产品名称 */
    private String supplierProductName;

    /** 质量评级(A/B/C/D) */
    private String qualityRating;

    /** 供应备注 */
    private String notes;
}
