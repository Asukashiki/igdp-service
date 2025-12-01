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

import java.util.Date;
import java.util.List;

/**
 * 地块信息实体类
 *
 * @author inspur
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("plot_info")
public class PlotInfo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 地块ID(主键) */
    @TableId(value = "ground_id", type = IdType.ASSIGN_UUID)
    private String groundId;

    /** 育种批次ID */
    @TableField("batch_id")
    private String batchId;

    /** 试验ID */
    @TableField("trial_id")
    private String trialId;

    /** 试验田名称 */
    @TableField("trial_field_name")
    private String trialFieldName;

    /** 研究中心ID */
    @TableField("research_center_id")
    private String researchCenterId;

    /** 程序ID */
    @TableField("program_id")
    private String programId;

    /** 子程序ID */
    @TableField("sub_program_id")
    private String subProgramId;

    /** 主题研究领域ID */
    @TableField("research_field_id")
    private String researchFieldId;

    /** 地区 */
    @TableField("region")
    private String region;

    /** 区域 */
    @TableField("zone")
    private String zone;

    /** 县 */
    @TableField("woreda")
    private String woreda;

    /** 乡 */
    @TableField("kebele")
    private String kebele;

    /** 农业生态区 */
    @TableField("agricultural_eco_zone")
    private String agriculturalEcoZone;

    /** GPS位置 */
    @TableField("gps_location")
    private String gpsLocation;

    /** 开始日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @TableField("start_date")
    private Date startDate;

    /** 季节 */
    @TableField("season")
    private String season;

    /** 活动代码 */
    @TableField("activity_code")
    private String activityCode;

    /** KPI代码 */
    @TableField("kpi_code")
    private String kpiCode;

    /** 逻辑删除标识(0=未删除,1=已删除) */
    @TableLogic
    @TableField("is_deleted")
    private Integer isDeleted;

    /** 播种信息列表（非数据库字段） */
    @TableField(exist = false)
    private List<SowingInfo> sowingList;

    /** 作物类型（关联字段，非数据库字段） */
    @TableField(exist = false)
    private String cropType;

    /** 品种名称（关联字段，非数据库字段） */
    @TableField(exist = false)
    private String varietyName;

    /** 播种记录数量（关联字段，非数据库字段） */
    @TableField(exist = false)
    private Integer sowingCount;

    /** 批次名称（关联字段，非数据库字段） */
    @TableField(exist = false)
    private String batchName;
}
