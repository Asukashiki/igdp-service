package com.inspur.seed.domain.basic;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.inspur.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Basic Seed 生产结果表
 *
 * @author igdp
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("basic_seed_produce_result")
public class BasicSeedProduceResult extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_UUID)
    private String resultId;

    @NotBlank(message = "Production batch ID cannot be empty")
    private String produceBatchId;

    private String produceBatchName;

    private String varietyName;

    @NotNull(message = "Collection date cannot be empty")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date collectionDate;

    @NotNull(message = "Output quantity cannot be empty")
    private BigDecimal outputQuantity;

    private BigDecimal remainingQuantity;

    private String breedBatchName;

    private String trialName;

    private String fromSeedLevel;

    private String toSeedLevel;

    private String operator;

    private String breedBatchId;

    private String varietyId;

    private String cropType;
}
