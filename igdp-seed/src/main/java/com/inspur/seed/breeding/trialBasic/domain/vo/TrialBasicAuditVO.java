package com.inspur.seed.breeding.trialBasic.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 试验基础信息审核VO
 *
 * @author system
 * @since 2025-01-30
 */
@Data
public class TrialBasicAuditVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 审核记录ID
     */
    private String auditId;

    /**
     * 试验ID
     */
    private String trialId;

    /**
     * 育种批次ID
     */
    private String batchId;

    /**
     * 试验名称
     */
    private String trialName;

    /**
     * 审核状态(S1=待审核,S2=已通过,S3=已退回)
     */
    private String auditStatus;

    /** 为前端提供统一的字段名：workflowStatus（不移除 auditStatus 以保证兼容） */
    @com.fasterxml.jackson.annotation.JsonProperty("workflowStatus")
    public String getWorkflowStatus() {
        return this.auditStatus;
    }

    /**
     * 审核状态描述
     */
    private String auditStatusDesc;

    /**
     * 审核意见
     */
    private String auditOpinion;

    /**
     * 退回原因
     */
    private String rejectReason;

    /**
     * 审核人ID
     */
    private String auditorId;

    /**
     * 审核人姓名
     */
    private String auditorName;

    /**
     * 审核时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime auditTime;

    /**
     * 提交人ID
     */
    private String submitterId;

    /**
     * 提交人姓名
     */
    private String submitterName;

    /**
     * 提交时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime submitTime;

    /**
     * 试验数据快照(JSON格式)
     */
    private String trialDataSnapshot;

    /**
     * 作物类型
     */
    private String cropType;

    /**
     * 品种名称
     */
    private String varietyName;

    /**
     * 创建人姓名
     */
    private String createdName;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 修改人姓名
     */
    private String modifiedName;

    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modifiedTime;
}
