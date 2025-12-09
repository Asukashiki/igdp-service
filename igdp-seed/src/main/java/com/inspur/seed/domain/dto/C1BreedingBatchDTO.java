package com.inspur.seed.domain.dto;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * C1繁殖批次请求DTO
 * @author system
 * @since 2025-12-08
 */
@Data
public class C1BreedingBatchDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID（编辑时需要）
     */
    private String id;

    /**
     * 关联的C1繁殖申请ID
     */
    private String propagationId;

    /**
     * 作物种类
     */
    private String cropType;

    /**
     * 品种名称
     */
    private String varietyName;

    /**
     * 品种代码
     */
    private String varietyCode;

    /**
     * 繁殖级别
     */
    private String breedingLevel;

    /**
     * 繁育方法
     */
    private String breedingMethod;

    /**
     * 亲本种子来源
     */
    private String parentSeedSource;

    /**
     * 开始日期
     */
    private String startDate;

    /**
     * 结束日期
     */
    private String endDate;

    /**
     * 预期产量(kg)
     */
    private BigDecimal expectedYield;

    /**
     * 实际产量(kg)
     */
    private BigDecimal actualYield;

    /**
     * 种植面积(公顷)
     */
    private BigDecimal plantingArea;

    /**
     * 批次状态
     */
    private String batchStatus;

    /**
     * 机构ID
     */
    private String orgId;

    /**
     * 机构名称
     */
    private String orgName;

    /**
     * 机构类型
     */
    private String orgType;

    /**
     * 繁殖地点
     */
    private String location;

    /**
     * 备注
     */
    private String remark;
}
