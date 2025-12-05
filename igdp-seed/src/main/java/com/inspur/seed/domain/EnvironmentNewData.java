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
 * 环境监测新数据实体类
 * Environment New Data Entity
 *
 * @author inspur
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("environment_new_data")
public class EnvironmentNewData extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 环境记录ID(主键) / Environment Record ID (PK) */
    @TableId(value = "env_record_id", type = IdType.ASSIGN_UUID)
    private String envRecordId;

    /** 试验ID(外键) / Trial ID (FK) */
    @TableField("trial_id")
    private String trialId;

    /** 育种批次ID(外键) / Batch ID (FK) */
    @TableField("batch_id")
    private String batchId;

    /** 地块ID(可选外键) / Plot ID (Optional FK) */
    @TableField("plot_id")
    private String plotId;

    /** 气象站ID / Weather Station ID */
    @TableField("station_id")
    private String stationId;

    /** 数据采集时间 / Data Collection Timestamp */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @TableField("timestamp")
    private Date timestamp;

    /** 参数代码 / Parameter Code (e.g., RAIN_DAILY, TMAX, TMIN) */
    @TableField("parameter_code")
    private String parameterCode;

    /** 测量值 / Measured Value */
    @TableField("value")
    private BigDecimal value;

    /** 测量单位 / Measurement Unit */
    @TableField("unit")
    private String unit;

    /** 数据来源 / Data Source */
    @TableField("source")
    private String source;

    /** 逻辑删除标识(0=未删除,1=已删除) / Logical Delete Flag */
    @TableLogic
    @TableField("is_deleted")
    private Integer isDeleted;

    /** 查询起始时间（查询参数，非数据库字段） */
    @TableField(exist = false)
    private String queryStartTime;

    /** 查询结束时间（查询参数，非数据库字段） */
    @TableField(exist = false)
    private String queryEndTime;

    /** 批次名称（关联字段，非数据库字段） */
    @TableField(exist = false)
    private String batchName;

    /** 试验名称（关联字段，非数据库字段） */
    @TableField(exist = false)
    private String trialName;

    /** 地块名称（关联字段，非数据库字段） */
    @TableField(exist = false)
    private String plotName;
}
