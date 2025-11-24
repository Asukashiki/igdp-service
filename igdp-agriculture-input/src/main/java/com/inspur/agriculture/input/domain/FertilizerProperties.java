package com.inspur.agriculture.input.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 化肥特性对象 agri_fertilizer_properties
 *
 * @author igdp
 */
@Data
@TableName("agri_fertilizer_properties")
@ApiModel(value = "FertilizerProperties", description = "化肥特性")
public class FertilizerProperties implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    @ApiModelProperty("化肥特性ID")
    private Long fertilizerId;

    @ApiModelProperty("关联产品ID")
    private Long inputId;

    @ApiModelProperty("肥料类型")
    private String fertilizerType;

    @ApiModelProperty("总养分含量")
    private String totalNutrientContent;

    @ApiModelProperty("氮含量")
    private String nitrogenContent;

    @ApiModelProperty("磷含量(P2O5)")
    private String phosphorusContent;

    @ApiModelProperty("钾含量(K2O)")
    private String potassiumContent;

    @ApiModelProperty("有机质含量")
    private String organicMatterContent;

    @ApiModelProperty("中微量元素")
    private String mediumTraceElements;

    @ApiModelProperty("pH值")
    private String phValue;

    @ApiModelProperty("适用作物")
    private String suitableCrops;

    @ApiModelProperty("施用时期")
    private String applicationPeriod;

    @ApiModelProperty("施用方法")
    private String applicationMethod;

    @ApiModelProperty("建议用量")
    private String recommendedDosage;
}
