package com.inspur.seed.domain.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 品种评估数据采集DTO
 *
 * @author igdp
 * @date 2025-11-26
 */
@Data
public class VarietyEvaluationDataDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 数据ID
     */
    private String dataId;

    /**
     * 地块ID(必填)
     */
    private String plotId;

    /**
     * 地块面积平方米(必填)
     */
    private BigDecimal plotAreaM2;

    /**
     * 籽粒重量KG(必填)
     */
    private BigDecimal grainWeightKg;

    /**
     * 产量公担每公顷(必填)
     */
    private BigDecimal yieldQtPerHa;

    /**
     * 含水量(必填)
     */
    private BigDecimal moistureContent;
}
