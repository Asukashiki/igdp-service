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
 * Quota Allocation Entity
 * 投入品配额逐级分配实体类
 */
@Data
@TableName("t_quota_allocation")
public class QuotaAllocation implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Allocation unique identifier (UUID)
     */
    @TableId(value = "allocation_id", type = IdType.ASSIGN_UUID)
    private String allocationId;

    /**
     * Allocation name
     */
    @TableField("allocation_name")
    private String allocationName;

    /**
     * Allocation year
     */
    @TableField("year")
    private Integer year;

    /**
     * Input category ID
     */
    @TableField("category_id")
    private String categoryId;

    /**
     * From division ID
     */
    @TableField("from_division_id")
    private String fromDivisionId;

    /**
     * From division level (1=region, 2=zone, 3=worede, 4=kebele)
     */
    @TableField("from_division_level")
    private Integer fromDivisionLevel;

    /**
     * From parent division ID
     */
    @TableField("from_parent_division_id")
    private String fromParentDivisionId;

    /**
     * To division ID
     */
    @TableField("to_division_id")
    private String toDivisionId;

    /**
     * To farmer ID
     */
    @TableField("to_farmer_id")
    private String toFarmerId;

    /**
     * Allocated quota amount
     */
    @TableField("allocated_quota")
    private BigDecimal allocatedQuota;

    /**
     * Related state quota ID
     */
    @TableField("quota_id")
    private String quotaId;

    /**
     * Total received quota
     */
    @TableField("total_received_quota")
    private BigDecimal totalReceivedQuota;

    /**
     * Total allocated quota
     */
    @TableField("total_allocated_quota")
    private BigDecimal totalAllocatedQuota;

    /**
     * Remaining quota
     */
    @TableField("remaining_quota")
    private BigDecimal remainingQuota;

    /**
     * Allocation status (0=not allocated, 1=partially allocated, 2=completed)
     */
    @TableField("allocation_status")
    private Integer allocationStatus;

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
     * Progress update time
     */
    @TableField("progress_update_time")
    private LocalDateTime progressUpdateTime;

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
