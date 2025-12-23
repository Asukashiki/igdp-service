package com.inspur.seed.vo.breed;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.inspur.seed.domain.breed.BreedSeedProduceResult;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Seed Production Result VO
 *
 * @author igdp
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BreedSeedProduceResultVO extends BreedSeedProduceResult {

    /**
     * Production Batch Name
     */
    private String produceBatchName;

    /**
     * Variety Name
     */
    private String varietyName;

    /**
     * Breeding Batch Name
     */
    private String breedBatchName;


    /**
     * 育种批次ID
     */
    private String breedBatchId;



    /**
     * 品种ID
     */
    private String varietyId;


    /**
     * 实验批次ID
     */
    private String trialId;

    /**
     * 实验批次名称(自动带出)
     */
    private String trialName;

    /**
     * 作物类型
     */
    private String cropType;


    /**
     * 地块ID
     */
    private String landId;

    /**
     * 地块名称
     */
    private String landName;

    /**
     * 投入种子数量
     */
    private BigDecimal inputSeedQuantity;

    /**
     * 产出种子数量
     */
    private BigDecimal produceSeedQuantrity;

    /**
     * 种子等级来源
     */
    private String fromSeedLevel;

    /**
     * 种子等级去向
     */
    private String toSeedLevel;


}
