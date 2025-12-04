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

    /** 品种名称 */
    @TableField("variety_name")
    private String varietyName;

    /** 逻辑删除标识(0=未删除,1=已删除) */
    @TableLogic
    @TableField("is_deleted")
    private Integer isDeleted;
}
