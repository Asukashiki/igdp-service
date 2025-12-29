package com.inspur.seed.breeding.breederSeed.domain.dto;

import lombok.Data;

/**
 * Seed Production Result Query DTO
 *
 * @author igdp
 */
@Data
public class BreedSeedProduceResultQueryDTO {

    /**
     * Production Batch Name
     */
    private String produceBatchName;

    /**
     * Variety Name
     */
    private String varietyName;

    /**
     * Operator
     */
    private String operator;
}
