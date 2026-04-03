package com.inspur.seed.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("seed_detection_audit_record")
public class DetectionAuditRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    @TableField("record_type")
    private String recordType;

    @TableField("business_id")
    private String businessId;

    @TableField("batch_id")
    private String batchId;

    @TableField("audit_status")
    private String auditStatus;

    @TableField("submitter")
    private String submitter;

    @TableField("submitter_org_id")
    private String submitterOrgId;

    @TableField("submitter_org_name")
    private String submitterOrgName;

    @TableField("submit_time")
    private LocalDateTime submitTime;

    @TableField("auditor")
    private String auditor;

    @TableField("auditor_org_id")
    private String auditorOrgId;

    @TableField("auditor_org_name")
    private String auditorOrgName;

    @TableField("audit_time")
    private LocalDateTime auditTime;

    @TableField("audit_comment")
    private String auditComment;

    @TableField("deleted")
    @TableLogic
    private String deleted;

    @TableField("created_by")
    private String createdBy;

    @TableField("created_time")
    private LocalDateTime createdTime;

    @TableField("updated_by")
    private String updatedBy;

    @TableField("updated_time")
    private LocalDateTime updatedTime;
}
