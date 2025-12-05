package com.inspur.seed.domain.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 产量数据DTO
 *
 * @author igdp
 * @date 2025-11-29
 */
@Data
public class BreedingYieldDataDTO {

    /**
     * 主键ID
     */
    private String id;

    /**
     * 育种批次ID
     */
    private String batchId;

    /**
     * 试验ID
     */
    private String trialId;

    /**
     * 地块编号
     */
    private String plotId;

    /**
     * 地块面积(m²)
     */
    private BigDecimal plotAreaM2;

    /**
     * 谷物重量(kg)
     */
    private BigDecimal grainWeightKg;

    /**
     * 产量(公担/公顷)
     */
    private BigDecimal yieldQtPerHa;

    /**
     * 含水量(%)
     */
    private BigDecimal moistureContent;

    /**
     * 收获日期
     */
    private LocalDate harvestDate;

    /**
     * 记录人员
     */
    private String recorderName;

    /**
     * 备注
     */
    private String remark;

    /**
     * 检验日期
     */
    private LocalDate inspectionDate;

    /**
     * 检验类型
     */
    private String inspectionType;

    /**
     * 评分代码
     */
    private String scoreCode;

    /**
     * 评分值
     */
    private String scoreValue;

    // 查询条件字段
    /**
     * 收获日期开始
     */
    private LocalDate harvestDateStart;

    /**
     * 收获日期结束
     */
    private LocalDate harvestDateEnd;

}
