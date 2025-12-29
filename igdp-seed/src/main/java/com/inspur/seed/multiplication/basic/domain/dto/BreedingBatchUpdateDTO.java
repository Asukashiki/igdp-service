package com.inspur.seed.multiplication.basic.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 繁殖批次信息修改DTO
 *
 * @author igdp
 * @date 2025-11-29
 */
@Data
public class BreedingBatchUpdateDTO {

    /**
     * 主键ID
     */
    @NotBlank(message = "主键ID不能为空")
    private String id;

    /**
     * 作物类型
     */
    private String cropType;

    /**
     * 品种名称
     */
    private String varietyName;

    /**
     * 繁殖级别
     */
    private String breedingLevel;

    /**
     * 亲本种子来源
     */
    private String parentSeedSource;

    /**
     * 育种目标
     */
    private String objective;

    /**
     * 开始日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date startDate;

    /**
     * 结束日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date endDate;

    /**
     * 批次状态
     */
    private String batchStatus;

    /**
     * 预期产量
     */
    private BigDecimal expectedYield;

    /**
     * 实际产量
     */
    private BigDecimal actualYield;

    /**
     * 备注
     */
    private String remark;

    /**
     * 待扩繁数量
     */
    private BigDecimal toMultiplyQuantity;

    /**
     * 组织ID
     */
    private String orgId;


    /**
     * 组织名称
     */
    private String orgName;
}
