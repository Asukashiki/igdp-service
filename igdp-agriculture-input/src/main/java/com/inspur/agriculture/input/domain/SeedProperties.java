package com.inspur.agriculture.input.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 种子特性对象 agri_seed_properties
 *
 * @author igdp
 */
@Data
@TableName("agri_seed_properties")
public class SeedProperties implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 种子特性ID */
    @TableId(type = IdType.AUTO)
    private Long seedId;

    /** 关联产品ID */
    private Long inputId;

    /** 作物种类 */
    private String cropType;

    /** 品种名称 */
    private String varietyName;

    /** 品种审定编号 */
    private String varietyApprovalCode;

    /** 品种来源 */
    private String varietySource;

    /** 纯度(%) */
    private BigDecimal purity;

    /** 净度(%) */
    private BigDecimal cleanliness;

    /** 发芽率(%) */
    private BigDecimal germinationRate;

    /** 水分含量(%) */
    private BigDecimal moistureContent;
}
