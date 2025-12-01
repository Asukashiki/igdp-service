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

    /** 试验名称 */
    @TableField("trial_name")
    private String trialName;

    /** 育种批次ID */
    @TableField("batch_id")
    private String batchId;

    /** 作物类型 */
    @TableField("crop_type")
    private String cropType;

    /** 品种名称 */
    @TableField("variety_name")
    private String varietyName;

    /** 研究中心ID */
    @TableField("research_center_id")
    private String researchCenterId;

    /** 项目ID */
    @TableField("project_id")
    private String projectId;

    /** 子项目ID */
    @TableField("sub_project_id")
    private String subProjectId;

    /** 主题领域ID */
    @TableField("theme_field_id")
    private String themeFieldId;

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

    /** 活动代码 */
    @TableField("activity_code")
    private String activityCode;

    /** KPI代码 */
    @TableField("kpi_code")
    private String kpiCode;

    /** 季节 */
    @TableField("season")
    private String season;

    /** 逻辑删除标识(0=未删除,1=已删除) */
    @TableLogic
    @TableField("is_deleted")
    private Integer isDeleted;

    /** 关联地块ID列表（非数据库字段） */
    @TableField(exist = false)
    private List<String> plotIds;

    /** 关联地块信息列表（非数据库字段） */
    @TableField(exist = false)
    private List<PlotInfo> plotList;

    /** 关联地块数量（非数据库字段） */
    @TableField(exist = false)
    private Integer plotCount;
}
