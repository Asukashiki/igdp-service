package com.inspur.agriculture.input.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 化肥特性对象 agri_fertilizer_properties
 *
 * @author igdp
 */
@Data
@TableName("agri_fertilizer_properties")
public class FertilizerProperties implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 化肥特性ID */
    @TableId(type = IdType.AUTO)
    private Long fertilizerId;

    /** 关联产品ID */
    private Long inputId;

    /** 肥料类型 */
    private String fertilizerType;

    /** 总养分含量 */
    private String totalNutrientContent;

    /** 氮含量 */
    private String nitrogenContent;

    /** 磷含量(P2O5) */
    private String phosphorusContent;

    /** 钾含量(K2O) */
    private String potassiumContent;

    /** 有机质含量 */
    private String organicMatterContent;

    /** 中微量元素 */
    private String mediumTraceElements;

    /** pH值 */
    private String phValue;

    /** 适用作物 */
    private String suitableCrops;

    /** 施用时期 */
    private String applicationPeriod;

    /** 施用方法 */
    private String applicationMethod;

    /** 建议用量 */
    private String recommendedDosage;
}
