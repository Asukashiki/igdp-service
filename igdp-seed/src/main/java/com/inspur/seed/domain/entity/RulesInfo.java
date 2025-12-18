package com.inspur.seed.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 规则信息实体类
 */
@TableName("rules_info")
@Data
public class RulesInfo implements Serializable {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
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
