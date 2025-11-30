package com.inspur.seed.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 繁殖种子认证申请VO
 *
 * @author igdp
 * @date 2025-11-29
 */
@Data
public class BreedingSeedCertificationVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 数据ID
     */
    private String dataId;

    /**
     * 繁育批次ID
     */
    private String breedingBatchId;

    /**
     * 认证ID
     */
    private String authId;

    /**
     * 申请机构名称
     */
    private String applyOrgName;

    /**
     * 申请机构ID
     */
    private String applyOrgId;

    /**
     * 备案日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date recordDate;

    /**
     * 作物类型
     */
    private String cropType;

    /**
     * 品种名称
     */
    private String varietyName;

    /**
     * 备案状态
     */
    private String recordStatus;

    /**
     * 审核结果
     */
    private String auditResult;

    /**
     * 审核意见
     */
    private String auditComment;

    /**
     * 审核时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date auditTime;

    /**
     * 审核人
     */
    private String auditor;

    /**
     * 审核机构ID
     */
    private String auditorOrgId;

    /**
     * 审核机构名称
     */
    private String auditorOrgName;

    /**
     * 打印次数
     */
    private Integer printCount;

    /**
     * 最后打印时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastPrintTime;

    /**
     * 品种信息
     */
    private BreedingSeedVarietyInfoVO varietyInfo;

    /**
     * 技术性状信息
     */
    private BreedingSeedTechnicalTraitVO technicalTrait;

    /**
     * 试验与性能信息
     */
    private BreedingSeedTrialPerformanceVO trialPerformance;

    /**
     * 监管信息
     */
    private BreedingSeedSupervisionVO supervision;

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
