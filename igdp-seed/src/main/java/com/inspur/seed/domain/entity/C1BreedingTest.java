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

    /**
     * 种子级别(Pre-Basic/Basic/C1等)
     */
    @TableField("seed_class")
    private String seedClass;

    /**
     * 批次号
     */
    @TableField("lot_id")
    private String lotId;

    /**
     * 测试类型(GERMINATION/PURITY等)
     */
    @TableField("test_type")
    private String testType;

    /**
     * 单位
     */
    @TableField("unit")
    private String unit;

    /**
     * 是否通过(TRUE/FALSE)
     */
    @TableField("pass_status")
    private String passStatus;

    @TableField("audit_status")
    private String auditStatus;

    @TableField("submit_time")
    private LocalDateTime submitTime;

    @TableField("current_audit_id")
    private String currentAuditId;

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
