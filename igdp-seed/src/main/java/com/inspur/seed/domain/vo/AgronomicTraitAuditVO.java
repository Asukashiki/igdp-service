package com.inspur.seed.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date; // 关键：导入java.util.Date（和主表一致）

/**
 * 农艺性状审核返回VO
 */
@Data
public class AgronomicTraitAuditVO implements Serializable {

    private static final long serialVersionUID = 1L;

    // ===================== 审核表字段（保留 LocalDateTime 类型，无需修改） =====================
    private String id;
    private String recordId;
    private String traitId;
    private String batchId;
    private String auditNode;
    private Integer auditOrder;
    private String auditStatus;
    private String auditOpinion;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime auditTime; // 审核表字段，保持LocalDateTime

    private String auditorId;
    private String auditorName;
    private String auditorOrgCode;
    private String auditorOrgName;
    private Integer lockedFlag;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime submitTime; // 审核表字段，保持LocalDateTime

    private String submitterId;
    private String submitterName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createdTime; // 审核表字段，保持LocalDateTime

    private String traitRecordId;
    private String plotId;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date observationDate;

    private String growthStage;
    private String traitCode;
    private String traitName;
    private String traitValue;
    private String unit;
    private String observerId;
    private String trialId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date recordTime;

    private Integer plantHeightCm;
    private Integer tillerCount;
    private Integer spikeLengthCm;
    private Integer daysToEmergence;
    private Integer daysToTillering;
    private Integer daysToHeading;
    private String photoUrl;
    private String status;
    private String workflowStatus;
    private String remarks;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    private String createBy;
    private String updateBy;
    private String auditBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date auditTimeMain;

    private Integer isDeleted;
}
