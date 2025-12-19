package com.inspur.seed.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.inspur.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

/**
 * 农艺性状采集主记录实体类
 * 
 * @author inspur
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("agronomic_trait_record")
public class AgronomicTraitRecord extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 记录ID - {plot_id}-TR{序号} */
    @TableId(value = "record_id", type = IdType.INPUT)
    private String recordId;

    // ===== 显式映射BaseEntity字段 =====
    @TableField("create_by")
    private String createBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("create_time")
    private java.time.LocalDateTime createTime;

    @TableField("update_by")
    private String updateBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("update_time")
    private java.time.LocalDateTime updateTime;

    /** 地块ID */
    @TableField("plot_id")
    private String plotId;

    /** 试验ID */
    @TableField("trial_id")
    private String trialId;

    /** 育种批次ID */
    @TableField("batch_id")
    private String batchId;

    /** 观测日期时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("observation_date")
    private Date observationDate;

    /** 生长阶段 */
    @TableField("growth_stage")
    private String growthStage;

    /** 观测员ID */
    @TableField("observer_id")
    private String observerId;

    /** 照片URL/文件ID */
    @TableField("photo_url")
    private String photoUrl;

    /** 备注 */
    @TableField("remarks")
    private String remarks;

    /** 业务状态: draft/submit/approve */
    @TableField("status")
    private String status;

    /** 流程审核状态（字典flow_status） */
    @TableField("workflow_status")
    private String workflowStatus;

    /** 审核人 */
    @TableField("audit_by")
    private String auditBy;

    /** 审核时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("audit_time")
    private Date auditTime;

    /** 逻辑删除(0=未删除,1=已删除) */
    @TableLogic
    @TableField("is_deleted")
    private Integer isDeleted;

    /** 性状明细列表（非数据库字段） */
    @TableField(exist = false)
    private List<AgronomicTraitDetail> detailList;

    /** 性状数量统计（非数据库字段） */
    @TableField(exist = false)
    private Integer traitCount;

    // ===== 查询辅助字段（非数据库字段）=====
    @TableField(exist = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTimeBegin;

    @TableField(exist = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTimeEnd;

    @TableField(exist = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTimeBegin;

    @TableField(exist = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTimeEnd;

    @TableField(exist = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date auditTimeBegin;

    @TableField(exist = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date auditTimeEnd;

    @TableField(exist = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date observationDateBegin;

    @TableField(exist = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date observationDateEnd;
}
