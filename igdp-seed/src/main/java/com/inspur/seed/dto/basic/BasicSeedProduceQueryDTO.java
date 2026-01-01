package com.inspur.seed.dto.basic;

import lombok.Data;

/**
 * Basic Seed 生产查询DTO
 */
@Data
public class BasicSeedProduceQueryDTO {
    
    private String produceBatchId;
    
    private String produceBatchName;
    
    private String varietyName;
    
    private String cropType;
    
    private String produceStatus;
    
    private String startTime;
    
    private String endTime;
}
