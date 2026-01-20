package com.inspur.seed.dto.prebasic;

import lombok.Data;

/**
 * Pre-basic Seed 生产查询DTO
 */
@Data
public class PrebasicSeedProduceQueryDTO {
    
    private String produceBatchId;
    
    private String produceBatchName;
    
    private String varietyName;
    
    private String cropType;
    
    private String produceStatus;
    
    private String startTime;
    
    private String endTime;
}
