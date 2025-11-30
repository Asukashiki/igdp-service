package com.inspur.seed.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 繁殖种子认证申请DTO
 *
 * @author igdp
 * @date 2025-11-29
 */
@Data
public class BreedingSeedCertificationDTO implements Serializable {

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
     * 认证ID(必填)
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
     * 作物类型(必填)
     */
    private String cropType;

    /**
     * 品种名称(必填)
     */
    private String varietyName;

    /**
     * 备案状态(必填)
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
     * 查询开始日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    /**
     * 查询结束日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endDate;

    /**
     * 品种信息
     */
    private BreedingSeedVarietyInfoDTO varietyInfo;

    /**
     * 技术性状信息
     */
    private BreedingSeedTechnicalTraitDTO technicalTrait;

    /**
     * 试验与性能信息
     */
    private BreedingSeedTrialPerformanceDTO trialPerformance;

    /**
     * 监管信息
     */
    private BreedingSeedSupervisionDTO supervision;
}
