package com.inspur.seed.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 试验基础数据采集VO
 *
 * @author igdp
 * @date 2025-11-26
 */
@Data
public class TrialBaseDataVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 试验ID
     */
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
    @JsonFormat(pattern = "yyyy-MM-dd")
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
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 更新人
     */
    private String updateBy;
}
