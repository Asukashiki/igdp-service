package com.inspur.seed.vo.prebasic;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Pre-basic Seed 生产结果VO
 */
@Data
public class PrebasicSeedProduceResultVO {
    private String resultId;
    private String produceBatchId;
    private String produceBatchName;
    private String varietyName;
    private String varietyId;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date collectionDate;
    
    private BigDecimal outputQuantity;
    private BigDecimal remainingQuantity;
    private String breedBatchId;
    private String breedBatchName;
    private String trialName;
    private String fromSeedLevel;
    private String toSeedLevel;
    private String cropType;
    private String operator;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
