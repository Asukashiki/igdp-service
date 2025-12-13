package com.inspur.seed.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 试验基础信息审核DTO
 *
 * @author system
 * @since 2025-01-30
 */
@Data
public class TrialBasicAuditDTO implements Serializable {

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
     * 审核状态(S1=待审核,S2=已通过,S3=已退回)
     */
    private String auditStatus;

    /**
     * 审核意见
     */
    private String auditOpinion;

    /**
     * 退回原因
     */
    private String rejectReason;

    /**
     * 页码
     */
    private Integer pageNum = 1;

    /**
     * 每页大小
     */
    private Integer pageSize = 10;
}
