package com.inspur.seed.dto.basic;

import lombok.Data;

/**
 * Basic Seed 生产结果查询DTO
 */
@Data
public class BasicSeedProduceResultQueryDTO {
    
    private String produceBatchId;
    
    private String produceBatchName;
    
    private String varietyName;
    
    private String startDate;
    
    private String endDate;
}
