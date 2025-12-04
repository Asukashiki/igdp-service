package com.inspur.agriculture.input.domain.allocate.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * State Annual Quota Entity
 * 州级投入品年度配额实体类
 */
@Data
@TableName("t_state_annual_quota")
public class StateAnnualQuota implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Quota unique identifier (UUID)
     */
    @TableId(value = "quota_id", type = IdType.ASSIGN_UUID)
    private String quotaId;

    /**
     * Quota name (format: year_state_category)
     */
    @TableField("quota_name")
    private String quotaName;

    /**
     * Quota year
     */
    @TableField("year")
    private Integer year;

    /**
     * Input category ID
     */
    @TableField("category_id")
    private String categoryId;

    /**
     * Total quota amount
     */
    @TableField("total_quota")
    private BigDecimal totalQuota;

    /**
     * Operator ID
     */
    @TableField("operator_id")
    private String operatorId;

    /**
     * Operator division ID
     */
    @TableField("operator_division_id")
    private String operatorDivisionId;

    /**
     * Modifier ID
     */
    @TableField("modifier_id")
    private String modifierId;

    /**
     * Modifier division ID
     */
    @TableField("modifier_division_id")
    private String modifierDivisionId;

    /**
     * Operation time
     */
    @TableField("operate_time")
    private LocalDateTime operateTime;

    /**
     * Create time
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * Update time
     */
    @TableField("update_time")
    private LocalDateTime updateTime;
}
