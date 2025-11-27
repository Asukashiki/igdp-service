package com.inspur.seed.domain.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 试验基础数据采集DTO
 *
 * @author igdp
 * @date 2025-11-26
 */
@Data
public class TrialBaseDataDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 试验ID
     */
    private String trialId;

    /**
     * 作物类型(必填)
     */
    private String cropType;

    /**
     * 品种名称(必填)
     */
    private String varietyName;

    /**
     * 研究中心ID(必填)
     */
    private String researchCenterId;

    /**
     * 程序ID(必填)
     */
    private String programId;

    /**
     * 子程序ID(必填)
     */
    private String subProgramId;

    /**
     * 主题研究领域ID(必填)
     */
    private String thematicResearchAreaId;

    /**
     * 地区(必填)
     */
    private String region;

    /**
     * 区域(必填)
     */
    private String zone;

    /**
     * 县(必填)
     */
    private String woreda;

    /**
     * 乡(必填)
     */
    private String kebele;

    /**
     * 农业生态区
     */
    private String agroEcologicalZone;

    /**
     * GPS位置(必填)
     */
    private String gpsLocation;

    /**
     * 开始日期(必填)
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
     * 季节(必填)
     */
    private String season;
}
