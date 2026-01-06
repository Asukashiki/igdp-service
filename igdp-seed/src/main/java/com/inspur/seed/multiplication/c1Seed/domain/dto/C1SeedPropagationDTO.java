package com.inspur.seed.multiplication.c1Seed.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * C1种子繁殖申请数据录入DTO
 *
 * @author system
 * @since 2025-12-08
 */
@Data
public class C1SeedPropagationDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID（编辑时必填）
     */
    private String id;

    /**
     * 授权ID
     */
    private String authId;

    /**
     * 申请机构类型
     */
    @NotBlank(message = "申请机构类型不能为空")
    private String applicantOrgType;

    /**
     * 申请机构名称
     */
    @NotBlank(message = "申请机构名称不能为空")
    private String applicantOrgName;

    /**
     * 申请机构ID
     */
    @NotBlank(message = "申请机构ID不能为空")
    private String applicantOrgId;

    /**
     * 繁育批次ID
     */
    @NotBlank(message = "繁育批次ID不能为空")
    private String propagationBatchId;

    /**
     * 作物种类
     */
    @NotBlank(message = "作物种类不能为空")
    private String cropType;

    /**
     * 品种名称
     */
    @NotBlank(message = "品种名称不能为空")
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
    private String applyDate;

    /**
     * 申请描述
     */
    private String applyDescription;

    /**
     * 申请状态
     */
    private String applyStatus;

    // ============ 审核信息（审核时使用） ============

    /**
     * 审核结果：approved-通过，rejected-拒绝
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
