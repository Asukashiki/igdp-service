package com.inspur.seed.vo.basic;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Basic Seed 生产结果VO
 */
@Data
public class BasicSeedProduceResultVO {
    private String resultId;
    private String produceBatchId;
    private String produceBatchName;
    private String varietyName;
    private String varietyId;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date collectionDate;
    
    private BigDecimal outputQuantity;
    private BigDecimal remainingQuantity;
    private String breedBatchName;
    private String breedBatchId;
    private String cropType;
    private String trialName;
    private String fromSeedLevel;
    private String toSeedLevel;
    private String parentalSeedSource;
    private String operator;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
