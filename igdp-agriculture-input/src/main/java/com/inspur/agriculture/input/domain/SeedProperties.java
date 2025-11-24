package com.inspur.agriculture.input.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(value = "SeedProperties", description = "种子特性")
public class SeedProperties implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    @ApiModelProperty("种子特性ID")
    private Long seedId;

    @ApiModelProperty("关联产品ID")
    private Long inputId;

    @ApiModelProperty("作物种类")
    private String cropType;

    @ApiModelProperty("品种名称")
    private String varietyName;

    @ApiModelProperty("品种审定编号")
    private String varietyApprovalCode;

    @ApiModelProperty("品种来源")
    private String varietySource;

    @ApiModelProperty("纯度(%)")
    private BigDecimal purity;

    @ApiModelProperty("净度(%)")
    private BigDecimal cleanliness;

    @ApiModelProperty("发芽率(%)")
    private BigDecimal germinationRate;

    @ApiModelProperty("水分含量(%)")
    private BigDecimal moistureContent;
}
