package com.inspur.agriculture.input.domain.supplier;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 供应商投入品关系对象 supplier_product
 *
 * @author igdp
 */
@Data
@TableName("supplier_product")
public class SupplierProduct implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 供应关系ID */
    @TableId(type = IdType.AUTO)
    private Long supplierProductId;

    /** 供应商ID */
    private Long supplierId;

    /** 投入品ID */
    private Long inputId;

    /** 供应商产品编码 */
    private String supplierProductCode;

    /** 供应商产品名称 */
    private String supplierProductName;

    /** 质量评级(A/B/C/D) */
    private String qualityRating;

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

    /** 删除标志(0-正常/2-删除) */
    private String delFlag;

    /** 供应商名称（关联查询） */
    @TableField(exist = false)
    private String supplierName;

    /** 投入品名称（关联查询） */
    @TableField(exist = false)
    private String inputName;

    /** 投入品类型（关联查询） */
    @TableField(exist = false)
    private String inputType;

    /** 投入品规格（关联查询） */
    @TableField(exist = false)
    private String inputSku;

    /** 当前价格（可扩展字段） */
    @TableField(exist = false)
    private String currentPrice;

    /** 认证状态（关联供应商认证状态） */
    @TableField(exist = false)
    private Integer certStatus;
}
