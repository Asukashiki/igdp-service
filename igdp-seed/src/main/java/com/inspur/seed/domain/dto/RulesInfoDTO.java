package com.inspur.seed.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 规则信息DTO
 *
 * @author igdp
 * @date 2025-12-18
 */
@Data
public class RulesInfoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 字典编码
     */
    private String dictCode;

    /**
     * 检测类型
     */
    private String inspectionType;

    /**
     * 分数编码
     */
    private String scoreCodes;

    /**
     * 条件类型 (value_range, threshold)
     */
    private String conditionType;

    /**
     * 操作符 (> >= < <= = between outside)
     */
    private String operator;

    /**
     * 最小值
     */
    private BigDecimal minValue;

    /**
     * 最大值
     */
    private BigDecimal maxValue;

    /**
     * 参考值
     */
    private BigDecimal referenceValue;

    /**
     * 单位
     */
    private String unit;
}