package com.inspur.seed.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Demand Audit Record Entity
 *
 * @author igdp
 * @date 2025-12-04
 */
@Data
@TableName("demand_audit_record")
public class DemandAuditRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Primary Key ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * Batch ID (关联demand_collection_batch)
     */
    private String batchId;

    /**
     * Demand ID (for single audit)
     */
    private String demandId;

    /**
     * Summary ID (for batch audit)
     */
    private String summaryId;

    /**
     * Audit Type (single/batch)
     */
    private String auditType;

    /**
     * Audit Level (village/town/district/state/ministry)
     */
    private String auditLevel;

    /**
     * Admin Code
     */
    private String adminCode;

    /**
     * Admin Name
     */
    private String adminName;

    /**
     * Audit User ID
     */
    private String auditUserId;

    /**
     * Audit User Name
     */
    private String auditUserName;

    /**
     * Audit Time
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date auditTime;

    /**
     * Audit Action (submit/approve/reject)
     */
    private String auditAction;

    /**
     * Audit Result (passed/rejected)
     */
    private String auditResult;

    /**
     * Audit Opinion (required when rejected)
     */
    private String auditOpinion;

    /**
     * Created By User ID
     */
    private String createdBy;

    /**
     * Created Time
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;

    /**
     * Is Deleted (0: No, 1: Yes)
     */
    @TableLogic
    private Integer isDeleted;

    /**
     * Remark
     */
    private String remark;
}
