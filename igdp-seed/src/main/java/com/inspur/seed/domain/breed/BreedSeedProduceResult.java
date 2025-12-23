package com.inspur.seed.domain.breed;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.inspur.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Seed Production Result Entity
 *
 * @author igdp
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("breed_seed_produce_result")
public class BreedSeedProduceResult extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * Result ID
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String resultId;

    /**
     * Production Batch ID
     */
    private String produceBatchId;

    /**
     * Produced Amount (kg)
     */
    private BigDecimal producedAmount;

    /**
     * Collection Date
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date collectionDate;

    /**
     * Operator Name
     */
    private String operator;
}
