package com.inspur.seed.breeding.trialBasic.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 试验基础信息审核记录表
 *
 * @author system
 * @since 2025-01-30
 */
@Data
@TableName("trial_basic_audit")
public class TrialBasicAudit implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 审核记录ID(主键)
     */
    @TableId(value = "audit_id", type = IdType.ASSIGN_UUID)
    private String auditId;

    /**
     * 试验ID(关联trial_basic)
     */
    @TableField("trial_id")
    private String trialId;

    /**
     * 育种批次ID
     */
    @TableField("batch_id")
    private String batchId;

    /**
     * 试验名称
     */
    @TableField("trial_name")
    private String trialName;

    /**
     * 审核节点
     */
    @TableField("audit_node")
    private String auditNode;

    /**
     * 审核顺序
     */
    @TableField("audit_order")
    private Integer auditOrder;

    /**
     * 审核/流程状态(S1=待审核,S2=已通过,S3=已退回)
     */
    @TableField("workflow_status")
    private String auditStatus;

    /**
     * 审核意见
     */
    @TableField("audit_opinion")
    private String auditOpinion;

    /**
     * 退回原因
     */
    @TableField("reject_reason")
    private String rejectReason;

    /**
     * 审核人ID
     */
    @TableField("auditor_id")
    private String auditorId;

    /**
     * 审核人姓名
     */
    @TableField("auditor_name")
    private String auditorName;

    /**
     * 审核时间
     */
    @TableField("audit_time")
    private LocalDateTime auditTime;

    /**
     * 提交人ID
     */
    @TableField("submitter_id")
    private String submitterId;

    /**
     * 提交人姓名
     */
    @TableField("submitter_name")
    private String submitterName;

    /**
     * 提交时间
     */
    @TableField("submit_time")
    private LocalDateTime submitTime;

    /**
     * 试验数据快照(JSON格式)
     */
    @TableField("trial_data_snapshot")
    private String trialDataSnapshot;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;

    /**
     * 创建者
     */
    @TableField("create_by")
    private String createBy;

    /**
     * 更新者
     */
    @TableField("update_by")
    private String updateBy;

    /**
     * 逻辑删除标识(0=未删除,1=已删除)
     */
    @TableField("deleted")
    private String deleted;
}
