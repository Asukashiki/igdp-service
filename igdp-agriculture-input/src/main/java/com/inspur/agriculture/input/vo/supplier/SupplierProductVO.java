package com.inspur.agriculture.input.vo.supplier;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 供应商投入品详情VO
 *
 * @author igdp
 */
@Data
public class SupplierProductVO {

    /** 供应关系ID */
    private Long supplierProductId;

    /** 供应商ID */
    private Long supplierId;

    /** 供应商名称 */
    private String supplierName;

    /** 投入品ID */
    private Long inputId;

    /** 投入品名称 */
    private String inputName;

    /** 投入品类型 */
    private String inputType;

    /** 投入品规格 */
    private String inputSku;

    /** 供应商产品编码 */
    private String supplierProductCode;

    /** 供应商产品名称 */
    private String supplierProductName;

    /** 质量评级 */
    private String qualityRating;

    /** 当前价格 */
    private String currentPrice;

    /** 认证状态 */
    private Integer certStatus;

    /** 认证状态描述 */
    private String certStatusDesc;

    /** 供应备注 */
    private String notes;

    /** 创建人 */
    private String createPeople;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 更新人 */
    private String updatePeople;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
