package com.inspur.seed.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 物种特性表
 *
 * @author system
 * @since 2025-01-30
 */
@Data
@TableName("breeding_variety_traits")
public class BreedingVarietyTraits implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 许可ID
     */
    @TableField("license_id")
    private String licenseId;

    /**
     * 育种批次ID
     */
    @TableField("batch_id")
    private String batchId;

    /**
     * 最小产量潜力
     */
    @TableField("min_yield_potential")
    private BigDecimal minYieldPotential;

    /**
     * 最大产量潜力
     */
    @TableField("max_yield_potential")
    private BigDecimal maxYieldPotential;

    /**
     * 抗病性(JSON)
     */
    @TableField("disease_resistance")
    private String diseaseResistance;

    /**
     * 压力耐受性(JSON)
     */
    @TableField("stress_tolerance")
    private String stressTolerance;

    /**
     * 成熟期(天)
     */
    @TableField("maturity_days")
    private Integer maturityDays;

    /**
     * 株高(cm)
     */
    @TableField("plant_height")
    private BigDecimal plantHeight;

    /**
     * 谷物品质性状
     */
    @TableField("grain_quality_traits")
    private String grainQualityTraits;

    /**
     * 其他特性(JSON)
     */
    @TableField("other_traits")
    private String otherTraits;

    /**
     * 状态:1有效0无效
     */
    @TableField("status")
    private String status;

    /**
     * 创建人ID
     */
    @TableField("created_by")
    private String createdBy;

    /**
     * 创建时间
     */
    @TableField("created_time")
    private LocalDateTime createdTime;

    /**
     * 更新人ID
     */
    @TableField("updated_by")
    private String updatedBy;

    /**
     * 更新时间
     */
    @TableField("updated_time")
    private LocalDateTime updatedTime;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 删除标记:0未删除1已删除
     */
    @TableField("deleted")
    private String deleted;
}
