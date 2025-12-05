package com.inspur.seed.domain.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 产量数据VO
 *
 * @author igdp
 * @date 2025-11-29
 */
@Data
public class BreedingYieldDataVO {

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

    /**
     * 创建人姓名
     */
    private String createdByName;

    /**
     * 创建时间
     */
    private LocalDateTime createdTime;
}
