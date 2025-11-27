package com.inspur.seed.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.inspur.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 农艺性状数据采集实体类
 *
 * @author igdp
 * @date 2025-11-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("seed_agronomic_trait_data")
public class AgronomicTraitData extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 数据ID(主键)
     */
    @TableId(type = IdType.ASSIGN_UUID)
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
     * 疾病评分(JSON格式)
     */
    private String diseaseScore;

    /**
     * 压力指标(JSON格式)
     */
    private String stressIndicators;

    /**
     * 害虫观察
     */
    private String pestObservation;

    /**
     * 照片证据(文件路径)
     */
    private String photoEvidence;

    /**
     * 删除标志(0正常 2删除)
     */
    private String delFlag;
}
