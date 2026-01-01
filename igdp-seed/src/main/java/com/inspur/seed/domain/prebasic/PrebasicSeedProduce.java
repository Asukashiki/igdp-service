package com.inspur.seed.domain.prebasic;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Pre-basic Seed 生产主表
 *
 * @author igdp
 */
@Data
@TableName("prebasic_seed_produce")
public class PrebasicSeedProduce implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.INPUT)
    private String produceBatchId;

    private String produceBatchName;

    private String breederSeedBatchId;

    private String breederSeedBatchName;

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

    private String createBy;

    private String updateBy;
}
