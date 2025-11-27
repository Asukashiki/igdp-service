package com.inspur.agriculture.input.dto.supplier;

import lombok.Data;

/**
 * 供应商投入品查询DTO
 *
 * @author igdp
 */
@Data
public class SupplierProductQueryDTO {

    /** 供应商ID */
    private Long supplierId;

    /** 投入品类型(pesticide-农药/fertilizer-化肥/seed-种子/other-其他) */
    private String inputType;

    /** 投入品名称（模糊查询） */
    private String inputName;

    /** 投入品编码（精确查询） */
    private String inputSku;

    /** 供应商产品编码（模糊查询） */
    private String supplierProductCode;

    /** 质量评级 */
    private String qualityRating;

    /** 关键词搜索（投入品名称/供应商产品名称/供应商产品编码） */
    private String keyword;
}
