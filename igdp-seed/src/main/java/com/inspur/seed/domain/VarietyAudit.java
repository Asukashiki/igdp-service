package com.inspur.seed.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.inspur.common.core.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 品种审核记录实体类
 *
 * @author system
 */
@TableName("variety_audit")
@Setter
@Getter
public class VarietyAudit extends BaseEntity {

    /**
     * 审核记录唯一标识（主键，系统生成）
     */
    @TableId
    private String auditId;

    /**
     * 关联登记申请唯一标识
     */
    private String registrationId;

    /**
     * 关联企业唯一标识
     */
    private String enterpriseId;

    /**
     * 品种名称（冗余）
     */
    private String varietyName;

    /**
     * 审核结果（0-待审核/1-通过/2-驳回）
     */
    private Integer auditResult;

    /**
     * 审核意见
     */
    private String auditOpinion;

    /**
     * 驳回原因（驳回时必填）
     */
    private String rejectReason;

    /**
     * 审核人
     */
    private String auditor;

    /**
     * 审核时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime auditTime;

    /**
     * 审核阶段（枚举：初审/复审/终审）
     */
    private String auditStage;
}
