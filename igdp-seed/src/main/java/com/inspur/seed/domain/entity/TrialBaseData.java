package com.inspur.seed.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.inspur.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 试验基础数据采集实体类
 *
 * @author igdp
 * @date 2025-11-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("seed_trial_base_data")
public class TrialBaseData extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 试验ID(主键)
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String trialId;

    /**
     * 作物类型
     */
    private String cropType;

    /**
     * 品种名称
     */
    private String varietyName;

    /**
     * 研究中心ID
     */
    private String researchCenterId;

    /**
     * 程序ID
     */
    private String programId;

    /**
     * 子程序ID
     */
    private String subProgramId;

    /**
     * 主题研究领域ID
     */
    private String thematicResearchAreaId;

    /**
     * 地区
     */
    private String region;

    /**
     * 区域
     */
    private String zone;

    /**
     * 县
     */
    private String woreda;

    /**
     * 乡
     */
    private String kebele;

    /**
     * 农业生态区
     */
    private String agroEcologicalZone;

    /**
     * GPS位置
     */
    private String gpsLocation;

    /**
     * 开始日期
     */
    private Date startDate;

    /**
     * 活动代码
     */
    private String activityCode;

    /**
     * KPI代码
     */
    private String kpiCode;

    /**
     * 季节
     */
    private String season;

    /**
     * 删除标志(0正常 2删除)
     */
    private String delFlag;
}
