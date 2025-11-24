package com.inspur.agriculture.input.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 农药特性对象 agri_pesticide_properties
 *
 * @author igdp
 */
@Data
@TableName("agri_pesticide_properties")
public class PesticideProperties implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 农药特性ID */
    @TableId(type = IdType.AUTO)
    private Long pesticideId;

    /** 关联产品ID */
    private Long inputId;

    /** 总有效成分含量 */
    private String totalIngredientContent;

    /** 毒性等级(微毒/低毒/中等毒/高毒/剧毒) */
    private String toxicityLevel;

    /** 适用作物 */
    private String targetCrops;

    /** 防治对象 */
    private String controlTargets;

    /** 施用方法 */
    private String applicationMethod;

    /** 使用剂量 */
    private String dosage;

    /** 稀释倍数 */
    private String dilutionRatio;

    /** 安全间隔期(天) */
    private Integer safetyInterval;

    /** 注意事项 */
    private String precautions;

    /** 中毒急救措施 */
    private String firstAid;

    /** 储存要求 */
    private String storageRequirements;
}
