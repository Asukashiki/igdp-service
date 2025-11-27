package com.inspur.seed.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 农艺性状数据采集VO
 *
 * @author igdp
 * @date 2025-11-26
 */
@Data
public class AgronomicTraitDataVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 数据ID
     */
    private String dataId;

    /**
     * 植物高度(CM)
     */
    private BigDecimal plantHeightCm;

    /**
     * 分蘖数
     */
    private Integer tillerCount;

    /**
     * 穗长(CM)
     */
    private BigDecimal spikeLengthCm;

    /**
     * 天数至出苗期
     */
    private Integer daysToEmergence;

    /**
     * 天数至分蘖期
     */
    private Integer daysToTillering;

    /**
     * 天数至抽穗期
     */
    private Integer daysToHeading;

    /**
     * 天数至开花期
     */
    private Integer daysToFlowering;

    /**
     * 天数至灌浆期
     */
    private Integer daysToGrainFilling;

    /**
     * 天数至成熟期
     */
    private Integer daysToMaturity;

    /**
     * 倒伏评分
     */
    private Integer lodgingScore;

    /**
     * 生物量重量(KG)
     */
    private BigDecimal biomassWeightKg;

    /**
     * 穗密度
     */
    private BigDecimal spikeDensity;

    /**
     * 每穗粒重
     */
    private BigDecimal grainWeightPerSpike;

    /**
     * 疾病评分
     */
    private String diseaseScore;

    /**
     * 压力指标
     */
    private String stressIndicators;

    /**
     * 害虫观察
     */
    private String pestObservation;

    /**
     * 照片证据
     */
    private String photoEvidence;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 更新人
     */
    private String updateBy;
}
