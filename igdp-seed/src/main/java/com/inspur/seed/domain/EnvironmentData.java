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
 * 环境属性数据实体类
 *
 * @author inspur
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("environment_data")
public class EnvironmentData extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 环境数据ID(主键) */
    @TableId(value = "env_id", type = IdType.ASSIGN_UUID)
    private String envId;

    /** 育种批次ID */
    @TableField("batch_id")
    private String batchId;

    /** 试验ID */
    @TableField("trial_id")
    private String trialId;

    /** 地块ID */
    @TableField("ground_id")
    private String groundId;

    /** 数据类型 */
    @TableField("data_type")
    private String dataType;

    /** 采集时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @TableField("collect_time")
    private Date collectTime;

    /** 土壤pH值(土壤数据必填) */
    @TableField("soil_ph")
    private BigDecimal soilPh;

    /** 土壤温度(℃，土壤数据必填) */
    @TableField("soil_temperature")
    private BigDecimal soilTemperature;

    /** 土壤湿度(%)，土壤数据必填) */
    @TableField("soil_moisture")
    private BigDecimal soilMoisture;

    /** 空气温度(℃，气候数据必填) */
    @TableField("air_temperature")
    private BigDecimal airTemperature;

    /** 空气湿度(%)，气候数据必填) */
    @TableField("air_humidity")
    private BigDecimal airHumidity;

    /** 降雨量(mm，气候数据必填) */
    @TableField("rainfall")
    private BigDecimal rainfall;

    /** 水体pH值(水文数据必填) */
    @TableField("water_ph")
    private BigDecimal waterPh;

    /** 数据来源 */
    @TableField("data_source")
    private String dataSource;

    /** 逻辑删除标识(0=未删除,1=已删除) */
    @TableLogic
    @TableField("is_deleted")
    private Integer isDeleted;

    /** 查询起始时间（查询参数，非数据库字段） */
    @TableField(exist = false)
    private String queryStartTime;

    /** 查询结束时间（查询参数，非数据库字段） */
    @TableField(exist = false)
    private String queryEndTime;
}
