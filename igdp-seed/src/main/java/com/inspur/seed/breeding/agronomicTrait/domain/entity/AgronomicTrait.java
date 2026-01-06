package com.inspur.seed.breeding.agronomicTrait.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.inspur.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 农艺性状数据实体类
 *
 * @author inspur
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("agronomic_trait")
public class AgronomicTrait extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 性状记录ID(主键) - 格式: {plot_id}-T{record_no} */
    @TableId(value = "trait_id", type = IdType.INPUT)
    private String traitId;

    /** 性状记录编号 - 格式: {plot_id}-T{record_no} */
    @TableField("trait_record_id")
    private String traitRecordId;

    /** 地块ID */
    @TableField("plot_id")
    private String plotId;

    /** 试验ID */
    @TableField("trial_id")
    private String trialId;

    /** 育种批次ID */
    @TableField("batch_id")
    private String batchId;

    /** 观测日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @TableField("observation_date")
    private Date observationDate;

    /** 生长阶段 */
    @TableField("growth_stage")
    private String growthStage;

    /** 性状代码 */
    @TableField("trait_code")
    private String traitCode;

    /** 性状名称 */
    @TableField("trait_name")
    private String traitName;

    /** 性状值 */
    @TableField("trait_value")
    private BigDecimal traitValue;

    /** 单位 */
    @TableField("unit")
    private String unit;

    /** 观测员ID */
    @TableField("observer_id")
    private String observerId;

    /** 记录时间（保留以兼容旧数据） */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @TableField("record_time")
    private Date recordTime;

    /** 植物高度(CM) */
    @TableField("plant_height_cm")
    private BigDecimal plantHeightCm;

    /** 分蘖数 */
    @TableField("tiller_count")
    private Integer tillerCount;

    /** 穗长(CM) */
    @TableField("spike_length_cm")
    private BigDecimal spikeLengthCm;

    /** 出苗天数 */
    @TableField("days_to_emergence")
    private Integer daysToEmergence;

    /** 分蘖天数 */
    @TableField("days_to_tillering")
    private Integer daysToTillering;

    /** 抽穗期天数 */
    @TableField("days_to_heading")
    private Integer daysToHeading;

    /** 照片URL */
    @TableField("photo_url")
    private String photoUrl;

    /** 逻辑删除标识(0=未删除,1=已删除) */
    @TableLogic
    @TableField("is_deleted")
    private Integer isDeleted;

    /** 作物类型（关联字段，非数据库字段） */
    @TableField(exist = false)
    private String cropType;

    /** 品种名称（关联字段，非数据库字段） */
    @TableField(exist = false)
    private String varietyName;

    /** 查询起始时间（查询参数，非数据库字段） */
    @TableField(exist = false)
    private Date queryStartTime;

    /** 查询结束时间（查询参数，非数据库字段） */
    @TableField(exist = false)
    private Date queryEndTime;

    /** 批次名称（关联字段，非数据库字段） */
    @TableField(exist = false)
    private String batchName;

    /** 业务状态 submit/approve */
    @TableField("status")
    private String status;

    /** 流程审核状态（字典 flow_status） */
    @TableField("workflow_status")
    private String workflowStatus;

    /** 审核人 */
    @TableField("audit_by")
    private String auditBy;

    /** 审核时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("audit_time")
    private Date auditTime;

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
}
