package com.inspur.seed.dto.prebasic;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Pre-basic Seed 生产新增DTO
 */
@Data
public class PrebasicSeedProduceDTO {
    
    @NotBlank(message = "Batch name cannot be empty")
    private String produceBatchName;
    
    @NotBlank(message = "Source batch ID cannot be empty")
    private String breederSeedBatchId;
    
    @NotBlank(message = "Land ID cannot be empty")
    private String landId;
    
    private String landName; // 地块名称（用于显示）
    
    @NotNull(message = "Production time cannot be empty")
    private Date time;
    
    @NotNull(message = "Input quantity cannot be empty")
    private BigDecimal inputSeedQuantity;
    
    @NotBlank(message = "Operator ID cannot be empty")
    private String operatorId;
}
