package com.inspur.seed.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * C1繁殖批次跟踪记录实体类
 */
@Data
@TableName("c1_breeding_tracking")
public class C1BreedingTracking implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    @TableField("tracking_id")
    private String trackingId;

    @TableField("batch_id")
    private String batchId;

    @TableField("stage_name")
    private String stageName;

    @TableField("location")
    private String location;

    @TableField("start_date")
    private LocalDate startDate;

    @TableField("end_date")
    private LocalDate endDate;

    @TableField("tracking_result")
    private String trackingResult;

    @TableField("tracking_desc")
    private String trackingDesc;

    @TableField("test_count")
    private Integer testCount;

    @TableField("operator")
    private String operator;

    @TableField("created_by")
    private String createdBy;

    @TableField("created_time")
    private LocalDateTime createdTime;

    @TableField("updated_by")
    private String updatedBy;

    @TableField("updated_time")
    private LocalDateTime updatedTime;

    @TableField("deleted")
    @TableLogic
    private String deleted;
}
