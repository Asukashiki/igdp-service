package com.inspur.seed.breeding.breederSeed.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Breeder Seed 生产数据DTO
 *
 * @author igdp
 */
@Data
public class BreedSeedProduceDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 批次名称
     */
    private String produceBatchName;
    /**
     * 育种批次ID
     */
    @NotBlank(message = "育种批次ID不能为空")
    private String breedBatchId;

    private String breedBatchName;

    /**
     * 品种ID
     */
    @NotBlank(message = "品种ID不能为空")
    private String varietyId;

    private String varietyName;

    /**
     * 实验批次ID
     */
    private String trialId;

    /**
     * 实验批次名称(自动带出)
     */
    private String trialName;

    private String cropType;

    /**
     * 生产时间
     */
    @NotNull(message = "生产时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date time;

    /**
     * 地块ID
     */
    @NotBlank(message = "地块ID不能为空")
    private String landId;


    private String landName;

    /**
     * 投入种子数量
     */
    @NotNull(message = "投入种子数量不能为空")
    @DecimalMin(value = "0", message = "投入种子数量必须大于等于0")
    private BigDecimal inputSeedQuantity;

    /**
     * 产出种子数量
     */
    private BigDecimal produceSeedQuantrity;

    /**
     * 种子等级来源
     */
    private String fromSeedLevel;

    /**
     * 种子等级去向
     */
    private String toSeedLevel;
}
