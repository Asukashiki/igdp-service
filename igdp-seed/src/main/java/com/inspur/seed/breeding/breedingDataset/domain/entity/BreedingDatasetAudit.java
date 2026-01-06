package com.inspur.seed.breeding.breedingDataset.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 育种数据集审核记录表
 *
 * @author system
 * @since 2025-01-30
 */
@Data
@TableName("breeding_dataset_audit")
public class BreedingDatasetAudit implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 数据集ID
     */
    @TableField("dataset_id")
    private String datasetId;

    /**
     * 育种批次ID
     */
    @TableField("batch_id")
    private String batchId;

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
     * 审核状态:pending/approved/rejected
     */
    @TableField("audit_status")
    private String auditStatus;

    /**
     * 审核意见
     */
    @TableField("audit_opinion")
    private String auditOpinion;

    /**
     * 审核时间
     */
    @TableField("audit_time")
    private LocalDateTime auditTime;

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
     * 审核人机构代码
     */
    @TableField("auditor_org_code")
    private String auditorOrgCode;

    /**
     * 审核人机构名称
     */
    @TableField("auditor_org_name")
    private String auditorOrgName;

    /**
     * 锁定标记:0未锁定1已锁定(锁定后不可修改)
     */
    @TableField("locked_flag")
    private Integer lockedFlag;

    /**
     * 提交时间
     */
    @TableField("submit_time")
    private LocalDateTime submitTime;

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
     * 状态:1有效0无效
     */
    @TableField("status")
    private String status;

    /**
     * 创建人ID
     */
    @TableField("created_by")
    private String createdBy;

    /**
     * 创建时间
     */
    @TableField("created_time")
    private LocalDateTime createdTime;

    /**
     * 更新人ID
     */
    @TableField("updated_by")
    private String updatedBy;

    /**
     * 更新时间
     */
    @TableField("updated_time")
    private LocalDateTime updatedTime;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 删除标记:0未删除1已删除
     */
    @TableField("deleted")
    private String deleted;
}
