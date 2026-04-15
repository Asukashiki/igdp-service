package com.inspur.seed.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("seed_detection_certificate")
public class DetectionCertificate implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("certificate_id")
    private String certificateId;

    @TableField("batch_id")
    private String batchId;

    @TableField("seed_class")
    private String seedClass;

    @TableField("crop_type")
    private String cropType;

    @TableField("audit_status")
    private String auditStatus;

    @TableField("source_type")
    private String sourceType;

    @TableField("created_by")
    private String createdBy;

    @TableField("created_time")
    private LocalDateTime createdTime;

    @TableField("updated_by")
    private String updatedBy;

    @TableField("updated_time")
    private LocalDateTime updatedTime;
}
