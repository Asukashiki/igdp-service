package com.inspur.seed.domain.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 农艺性状数据采集DTO
 *
 * @author igdp
 * @date 2025-11-26
 */
@Data
public class AgronomicTraitDataDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 数据ID
     */
    private String dataId;

    /**
     * 植物高度CM(必填)
     */
    private BigDecimal plantHeightCm;

    /**
     * 分蘖数(必填)
     */
    private Integer tillerCount;

    /**
     * 穗长CM(必填)
     */
    private BigDecimal spikeLengthCm;

    /**
     * 天数至出苗期(必填)
     */
    private Integer daysToEmergence;

    /**
     * 天数至分蘖期(必填)
     */
    private Integer daysToTillering;

    /**
     * 天数至抽穗期(必填)
     */
    private Integer daysToHeading;

    /**
     * 天数至开花期(必填)
     */
    private Integer daysToFlowering;

    /**
     * 天数至灌浆期(必填)
     */
    private Integer daysToGrainFilling;

    /**
     * 天数至成熟期(必填)
     */
    private Integer daysToMaturity;

    /**
     * 倒伏评分(必填)
     */
    private Integer lodgingScore;

    /**
     * 生物量重量KG(必填)
     */
    private BigDecimal biomassWeightKg;

    /**
     * 穗密度(必填)
     */
    private BigDecimal spikeDensity;

    /**
     * 每穗粒重(必填)
     */
    private BigDecimal grainWeightPerSpike;

    /**
     * 疾病评分(必填,JSON格式)
     */
    private String diseaseScore;

    /**
     * 压力指标(必填,JSON格式)
     */
    private String stressIndicators;

    /**
     * 害虫观察(必填)
     */
    private String pestObservation;

    /**
     * 照片证据(文件路径)
     */
    private String photoEvidence;
}
