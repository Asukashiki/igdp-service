package com.inspur.seed.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 试验基础信息审核历史记录表
 *
 * @author system
 * @since 2025-01-30
 */
@Data
@TableName("trial_basic_audit_history")
public class TrialBasicAuditHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 历史记录ID(主键)
     */
    @TableId(value = "history_id", type = IdType.ASSIGN_UUID)
    private String historyId;

    /**
     * 审核记录ID
     */
    @TableField("audit_id")
    private String auditId;

    /**
     * 试验ID
     */
    @TableField("trial_id")
    private String trialId;

    /**
     * 操作类型(SUBMIT=提交,APPROVE=审批通过,REJECT=退回)
     */
    @TableField("operation_type")
    private String operationType;

    /**
     * 操作描述
     */
    @TableField("operation_desc")
    private String operationDesc;

    /**
     * 操作人ID
     */
    @TableField("operator_id")
    private String operatorId;

    /**
     * 操作人姓名
     */
    @TableField("operator_name")
    private String operatorName;

    /**
     * 操作时间
     */
    @TableField("operation_time")
    private LocalDateTime operationTime;

    /**
     * 操作前状态
     */
    @TableField("before_status")
    private String beforeStatus;

    /**
     * 操作后状态
     */
    @TableField("after_status")
    private String afterStatus;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 逻辑删除标识(0=未删除,1=已删除)
     */
    @TableField("deleted")
    private String deleted;
}
