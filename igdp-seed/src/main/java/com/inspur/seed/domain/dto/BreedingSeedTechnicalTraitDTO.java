package com.inspur.seed.domain.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 繁殖种子技术性状信息DTO
 *
 * @author igdp
 * @date 2025-11-29
 */
@Data
public class BreedingSeedTechnicalTraitDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 数据ID
     */
    private String dataId;

    /**
     * 繁育批次ID(必填)
     */
    private String breedingBatchId;

    /**
     * 认证ID(必填)
     */
    private String authId;

    /**
     * 最低产量潜力(必填)
     */
    private BigDecimal minYieldPotential;

    /**
     * 最高产量潜力(必填)
     */
    private BigDecimal maxYieldPotential;

    /**
     * 抗病性(必填)
     */
    private String diseaseResistance;

    /**
     * 抗逆性(必填)
     */
    private String stressResistance;

    /**
     * 成熟期(必填)
     */
    private Integer maturityPeriod;

    /**
     * 株高(必填)
     */
    private BigDecimal plantHeight;

    /**
     * 谷物质量性状(必填)
     */
    private String grainQualityTrait;
}
