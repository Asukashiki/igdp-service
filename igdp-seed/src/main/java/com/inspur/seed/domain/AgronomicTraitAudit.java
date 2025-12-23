package com.inspur.seed.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 农艺性状审核记录表
 */
@Data
@TableName("agronomic_trait_audit")
public class AgronomicTraitAudit implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    @TableField("trait_id")
    private String traitId;

    @TableField("batch_id")
    private String batchId;

    @TableField("audit_node")
    private String auditNode;

    @TableField("audit_order")
    private Integer auditOrder;

    @TableField("audit_status")
    private String auditStatus;

    @TableField("audit_opinion")
    private String auditOpinion;

    @TableField("audit_time")
    private LocalDateTime auditTime;

    @TableField("auditor_id")
    private String auditorId;

    @TableField("auditor_name")
    private String auditorName;

    @TableField("auditor_org_code")
    private String auditorOrgCode;

    @TableField("auditor_org_name")
    private String auditorOrgName;

    @TableField("locked_flag")
    private Integer lockedFlag;

    @TableField("submit_time")
    private LocalDateTime submitTime;

    @TableField("submitter_id")
    private String submitterId;

    @TableField("submitter_name")
    private String submitterName;

    @TableField("status")
    private String status;

    @TableField("created_by")
    private String createdBy;

    @TableField("created_time")
    private LocalDateTime createdTime;

    @TableField("updated_by")
    private String updatedBy;

    @TableField("updated_time")
    private LocalDateTime updatedTime;

    @TableField("remark")
    private String remark;

    @TableField("deleted")
    private String deleted;
}