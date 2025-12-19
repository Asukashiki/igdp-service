package com.inspur.seed.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * C1种子繁殖申请视图对象
 *
 * @author system
 * @since 2025-12-08
 */
@Data
public class C1SeedPropagationVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private String id;

    /**
     * 授权ID
     */
    private String authId;

    /**
     * 申请机构类型
     */
    private String applicantOrgType;

    /**
     * 申请机构名称
     */
    private String applicantOrgName;

    /**
     * 申请机构ID
     */
    private String applicantOrgId;

    /**
     * 繁育批次ID
     */
    private String propagationBatchId;

    /**
     * 作物种类
     */
    private String cropType;

    /**
     * 品种名称
     */
    private String varietyName;

    /**
     * 品种代码
     */
    private String varietyCode;

    /**
     * 物种
     */
    private String species;

    /**
     * 申请日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate applyDate;

    /**
     * 申请描述
     */
    private String applyDescription;

    /**
     * 申请状态
     */
    private String applyStatus;

    /**
     * 审核结果
     */
    private String auditResult;

    /**
     * 审核意见
     */
    private String auditOpinion;

    /**
     * 审核人
     */
    private String auditor;

    /**
     * 审核机构
     */
    private String auditOrg;

    /**
     * 审核时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime auditTime;

    /**
     * 操作人
     */
    private String operator;

    /**
     * 操作机构
     */
    private String operationOrg;

    /**
     * 操作时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime operationTime;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTime;

    /**
     * 需求数量
     */
    private Integer demandQuantity;

    /**
     * 从种子类型
     */
    private String fromSeedType;

    /**
     * 到种子类型
     */
    private String toSeedType;
}
