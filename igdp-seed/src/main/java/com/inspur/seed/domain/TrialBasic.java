package com.inspur.seed.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.inspur.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 试验基础信息实体类
 *
 * @author inspur
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("trial_basic")
public class TrialBasic extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 试验ID(主键) */
    @TableId(value = "trial_id", type = IdType.ASSIGN_UUID)
    private String trialId;

    /** 育种批次ID */
    @TableField("batch_id")
    private String batchId;

    /** 试验名称 */
    @TableField("trial_name")
    private String trialName;

    /** 研究中心ID */
    @TableField("location_id")
    private String locationId;

    /** GPS位置 */
    @TableField("gps_location")
    private String gpsLocation;

    /** 季节 */
    @TableField("season")
    private String season;

    /** 年份 */
    @TableField("year")
    private Integer year;

    /** 试验设计类型 */
    @TableField("design_type")
    private String designType;

    /** 重复次数 */
    @TableField("replications")
    private Integer replications;

    /** 作物类型 */
    @TableField("crop_type")
    private String cropType;

    /** 品种编码 */
    @TableField("variety_code")
    private String varietyCode;

    /** 品种名称 */
    @TableField("variety_name")
    private String varietyName;

    /** 逻辑删除标识(0=未删除,1=已删除) */
    @TableLogic
    @TableField("is_deleted")
    private Integer isDeleted;

    /** 试验状态(01=Ongoing进行中,02=Finished已完成) */
    @TableField("status")
    private String status;

    /** 审核状态(S0=草稿,S1=待审批,S2=已审批,S3=已退回,S9=已归档,S10=作废) */
    @TableField("workflow_status")
    private String trialStatus;

    /**
     * 为前端提供统一的字段名：workflowStatus
     * 不改变原有属性名以保证兼容性
     */
    @com.fasterxml.jackson.annotation.JsonProperty("workflowStatus")
    public String getWorkflowStatus() {
        return this.trialStatus;
    }

    /** 审核状态(用于查询筛选) */
    @TableField(exist = false)
    private String auditStatus;

    /** 创建人ID */
    @TableField("created_by")
    private String createdBy;

    /** 创建人姓名 */
    @TableField("created_name")
    private String createdName;

    /** 修改人ID */
    @TableField("modified_by")
    private String modifiedBy;

    /** 修改人姓名 */
    @TableField("modified_name")
    private String modifiedName;

    /** 审批人ID */
    @TableField("approved_by")
    private String approvedBy;

    /** 审批人姓名 */
    @TableField("approved_name")
    private String approvedName;

    /** 审批时间 */
    @TableField("approved_time")
    private java.time.LocalDateTime approvedTime;

    /** 提交人ID */
    @TableField("submitted_by")
    private String submittedBy;

    /** 提交人姓名 */
    @TableField("submitted_name")
    private String submittedName;

    /** 提交时间 */
    @TableField("submitted_time")
    private java.time.LocalDateTime submittedTime;

    /** 退回人ID */
    @TableField("rejected_by")
    private String rejectedBy;

    /** 退回人姓名 */
    @TableField("rejected_name")
    private String rejectedName;

    /** 退回时间 */
    @TableField("rejected_time")
    private java.time.LocalDateTime rejectedTime;

    /** 归档人ID */
    @TableField("archived_by")
    private String archivedBy;

    /** 归档人姓名 */
    @TableField("archived_name")
    private String archivedName;

    /** 归档时间 */
    @TableField("archived_time")
    private java.time.LocalDateTime archivedTime;

    /** 作废人ID */
    @TableField("cancelled_by")
    private String cancelledBy;

    /** 作废人姓名 */
    @TableField("cancelled_name")
    private String cancelledName;

    /** 作废时间 */
    @TableField("cancelled_time")
    private java.time.LocalDateTime cancelledTime;

    /** 作废原因 */
    @TableField("cancel_reason")
    private String cancelReason;

    /** 退回原因 / Reject Reason */
    @TableField("reject_reason")
    private String rejectReason;
}
