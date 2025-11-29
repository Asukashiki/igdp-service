package com.inspur.seed.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.inspur.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 繁殖种子技术性状信息实体类
 *
 * @author igdp
 * @date 2025-11-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("seed_breeding_seed_technical_trait")
public class BreedingSeedTechnicalTrait extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 数据ID(主键)
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String dataId;

    /**
     * 繁育批次ID
     */
    private String breedingBatchId;

    /**
     * 认证ID
     */
    private String authId;

    /**
     * 最低产量潜力
     */
    private BigDecimal minYieldPotential;

    /**
     * 最高产量潜力
     */
    private BigDecimal maxYieldPotential;

    /**
     * 抗病性
     */
    private String diseaseResistance;

    /**
     * 抗逆性
     */
    private String stressResistance;

    /**
     * 成熟期
     */
    private Integer maturityPeriod;

    /**
     * 株高
     */
    private BigDecimal plantHeight;

    /**
     * 谷物质量性状
     */
    private String grainQualityTrait;

    /**
     * 删除标志(0正常 2删除)
     */
    private String delFlag;
}
