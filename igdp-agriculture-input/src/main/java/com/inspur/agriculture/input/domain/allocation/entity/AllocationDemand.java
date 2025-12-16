package com.inspur.agriculture.input.domain.allocation.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Zone Allocation Demand Item Entity
 * 区域分配额度需求项实体类
 */
@Data
@TableName("t_zone_allocation_demand")
public class AllocationDemand implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Demand item unique identifier (UUID)
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * Zone allocation ID (foreign key)
     */
    @TableField("allocation_id")
    private String allocationId;

    /**
     * Input type
     */
    @TableField("input_type")
    private String inputType;

    /**
     * Input category
     */
    @TableField("input_category")
    private String inputCategory;

    /**
     * Total quantity
     */
    @TableField("total_quantity")
    private BigDecimal totalQuantity;


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
