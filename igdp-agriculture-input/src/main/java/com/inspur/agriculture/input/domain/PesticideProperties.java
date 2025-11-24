package com.inspur.agriculture.input.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 农药特性对象 agri_pesticide_properties
 *
 * @author igdp
 */
@Data
@TableName("agri_pesticide_properties")
@ApiModel(value = "PesticideProperties", description = "农药特性")
public class PesticideProperties implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    @ApiModelProperty("农药特性ID")
    private Long pesticideId;

    @ApiModelProperty("关联产品ID")
    private Long inputId;

    @ApiModelProperty("总有效成分含量")
    private String totalIngredientContent;

    @ApiModelProperty("毒性等级(微毒/低毒/中等毒/高毒/剧毒)")
    private String toxicityLevel;

    @ApiModelProperty("适用作物")
    private String targetCrops;

    @ApiModelProperty("防治对象")
    private String controlTargets;

    @ApiModelProperty("施用方法")
    private String applicationMethod;

    @ApiModelProperty("使用剂量")
    private String dosage;

    @ApiModelProperty("稀释倍数")
    private String dilutionRatio;

    @ApiModelProperty("安全间隔期(天)")
    private Integer safetyInterval;

    @ApiModelProperty("注意事项")
    private String precautions;

    @ApiModelProperty("中毒急救措施")
    private String firstAid;

    @ApiModelProperty("储存要求")
    private String storageRequirements;
}
