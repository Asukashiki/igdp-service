package com.inspur.agriculture.input.domain.allocation.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Zone Allocation Entity
 * 区域分配额度实体类
 */
@Data
@TableName("t_zone_allocation")
public class Allocation implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Allocation unique identifier (UUID)
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * Allocation name
     */
    @TableField("allocation_name")
    private String allocationName;

    /**
     * Allocation year
     */
    @TableField("year")
    private String year;

    /**
     * Zone code
     */
    @TableField("zone")
    private String zone;

    /**
     * Zone name
     */
    @TableField("zone_name")
    private String zoneName;


    @TableField("level")
    private String level;

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
