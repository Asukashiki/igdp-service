package com.inspur.seed.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.inspur.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Multiplier Report Entity
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("multiplier_report")
public class MultiplierReport extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date reportDate;

    private String multiplierId;

    private String distributionId;

    private String certificateId;

    private String seedClassReceived;

    private String farmId;

    private String cropType;

    private String varietyName;

    private BigDecimal areaPlantedHa;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date plantingDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date harvestDate;

    private BigDecimal producedSeedQuantity;

    private BigDecimal rejectedQuantity;

    private BigDecimal germinationRate;

    private BigDecimal moistureContent;

    private String status;

    private String remark;

    @TableField(exist = false)
    private String keyword;
}
