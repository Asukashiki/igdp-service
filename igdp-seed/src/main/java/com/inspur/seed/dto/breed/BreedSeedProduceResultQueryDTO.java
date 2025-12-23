package com.inspur.seed.dto.breed;

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
