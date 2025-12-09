package com.inspur.seed.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * C1繁殖批次检测记录实体类
 */
@Data
@TableName("c1_breeding_test")
public class C1BreedingTest implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    @TableField("test_id")
    private String testId;

    @TableField("batch_id")
    private String batchId;

    @TableField("tracking_id")
    private String trackingId;

    @TableField("crop_type")
    private String cropType;

    @TableField("test_date")
    private LocalDate testDate;

    @TableField("test_item")
    private String testItem;

    @TableField("test_value")
    private String testValue;

    @TableField("test_result")
    private String testResult;

    @TableField("test_desc")
    private String testDesc;

    @TableField("tester")
    private String tester;

    @TableField("test_org")
    private String testOrg;

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
