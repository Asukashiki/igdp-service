package com.inspur.seed.multiplication.basic.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 繁殖批次信息新增DTO
 *
 * @author igdp
 * @date 2025-11-29
 */
@Data
public class BreedingBatchAddDTO {

    /**
     * 关联接收记录ID
     */
    private String receivedId;

    /**
     * 作物类型（枚举值）
     */
    @NotBlank(message = "作物类型不能为空")
    private String cropType;

    /**
     * 品种名称
     */
    @NotBlank(message = "品种名称不能为空")
    private String varietyName;

    /**
     * 繁殖级别（01/02/03）
     */
    @NotBlank(message = "繁殖级别不能为空")
    private String breedingLevel;

    /**
     * 亲本种子来源
     */
    @NotBlank(message = "亲本种子来源不能为空")
    private String parentSeedSource;

    /**
     * 育种目标
     */
    @NotBlank(message = "育种目标不能为空")
    private String objective;

    /**
     * 开始日期
     */
    @NotNull(message = "开始日期不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date startDate;

    /**
     * 预期产量（kg）
     */
    private BigDecimal expectedYield;

    /**
     * 操作机构ID
     */
    private String orgId;

    /**
     * 操作机构名称
     */
    private String orgName;

    /**
     * 备注
     */
    private String remark;
}
