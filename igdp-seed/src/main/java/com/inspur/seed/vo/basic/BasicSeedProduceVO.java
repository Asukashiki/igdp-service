package com.inspur.seed.vo.basic;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Basic Seed 生产VO
 */
@Data
public class BasicSeedProduceVO {
    private String produceBatchId;
    private String produceBatchName;
    private String prebasicSeedBatchId;
    private String prebasicSeedBatchName;
    private String varietyId;
    private String varietyName;
    private String cropType;
    private String breedBatchId;
    private String breedBatchName;
    private String trialId;
    private String trialName;
    private String landId;
    private String landName;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date time;
    
    private BigDecimal inputSeedQuantity;
    private BigDecimal produceSeedQuantity;
    private String fromSeedLevel;
    private String toSeedLevel;
    private String operatorId;
    private String operatorName;
    private String produceStatus;
    private String flowStatus;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
