package com.inspur.seed.domain;

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

    /** 性状记录ID(主键) */
    @TableId(value = "trait_id", type = IdType.ASSIGN_UUID)
    private String traitId;

    /** 育种批次ID */
    @TableField("batch_id")
    private String batchId;

    /** 试验ID */
    @TableField("trial_id")
    private String trialId;

    /** 记录时间 */
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
}
