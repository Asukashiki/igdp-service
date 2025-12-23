package com.inspur.seed.vo.breed;

import com.inspur.seed.domain.breed.BreedSeedProduceResult;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
}
