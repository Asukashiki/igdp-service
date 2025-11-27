package com.inspur.seed.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.inspur.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 品种评估数据采集实体类
 *
 * @author igdp
 * @date 2025-11-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("seed_variety_evaluation_data")
public class VarietyEvaluationData extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 数据ID(主键)
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String dataId;

    /**
     * 地块ID
     */
    private String plotId;

    /**
     * 地块面积(平方米)
     */
    private BigDecimal plotAreaM2;

    /**
     * 籽粒重量(KG)
     */
    private BigDecimal grainWeightKg;

    /**
     * 产量(公担/公顷)
     */
    private BigDecimal yieldQtPerHa;

    /**
     * 含水量(百分比)
     */
    private BigDecimal moistureContent;

    /**
     * 删除标志(0正常 2删除)
     */
    private String delFlag;
}
