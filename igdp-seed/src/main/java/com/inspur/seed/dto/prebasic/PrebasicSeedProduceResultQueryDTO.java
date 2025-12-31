package com.inspur.seed.dto.prebasic;

import lombok.Data;

/**
 * Pre-basic Seed 生产结果查询DTO
 */
@Data
public class PrebasicSeedProduceResultQueryDTO {
    
    private String produceBatchId;
    
    private String produceBatchName;
    
    private String varietyName;
    
    private String startDate;
    
    private String endDate;
}
