package com.inspur.seed.multiplication.c1Seed.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * C1种子繁殖申请实体类
 *
 * @author system
 * @since 2025-12-08
 */
@Data
@TableName("c1_seed_propagation")
public class C1SeedPropagation implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 授权ID
     */
    @TableField("auth_id")
    private String authId;

    /**
     * 申请机构类型
     */
    @TableField("applicant_org_type")
    private String applicantOrgType;

    /**
     * 申请机构名称
     */
    @TableField("applicant_org_name")
    private String applicantOrgName;

    /**
     * 申请机构ID
     */
    @TableField("applicant_org_id")
    private String applicantOrgId;

    /**
     * 繁育批次ID
     */
    @TableField("propagation_batch_id")
    private String propagationBatchId;

    /**
     * 作物种类
     */
    @TableField("crop_type")
    private String cropType;

    /**
     * 品种名称
     */
    @TableField("variety_name")
    private String varietyName;

    /**
     * 品种代码
     */
    @TableField("variety_code")
    private String varietyCode;

    /**
     * 物种
     */
    @TableField("species")
    private String species;

    /**
     * 申请日期
     */
    @TableField("apply_date")
    private LocalDate applyDate;

    /**
     * 申请描述
     */
    @TableField("apply_description")
    private String applyDescription;

    /**
     * 申请状态：pending-待审核，approved-通过，rejected-拒绝
     */
    @TableField("apply_status")
    private String applyStatus;

    /**
     * 审核结果：approved-通过，rejected-拒绝
     */
    @TableField("audit_result")
    private String auditResult;

    /**
     * 审核意见
     */
    @TableField("audit_opinion")
    private String auditOpinion;

    /**
     * 审核人
     */
    @TableField("auditor")
    private String auditor;

    /**
     * 审核机构
     */
    @TableField("audit_org")
    private String auditOrg;

    /**
     * 审核时间
     */
    @TableField("audit_time")
    private LocalDateTime auditTime;

    /**
     * 操作人
     */
    @TableField("operator")
    private String operator;

    /**
     * 操作机构
     */
    @TableField("operation_org")
    private String operationOrg;

    /**
     * 操作时间
     */
    @TableField("operation_time")
    private LocalDateTime operationTime;

    /**
     * 创建人
     */
    @TableField("created_by")
    private String createdBy;

    /**
     * 创建时间
     */
    @TableField("created_time")
    private LocalDateTime createdTime;

    /**
     * 更新人
     */
    @TableField("updated_by")
    private String updatedBy;

    /**
     * 更新时间
     */
    @TableField("updated_time")
    private LocalDateTime updatedTime;

    /**
     * 需求数量
     */
    @TableField("demand_quantity")
    private Integer demandQuantity;

    /**
     * 从种子类型
     */
    @TableField("from_seed_type")
    private String fromSeedType;

    /**
     * 到种子类型
     */
    @TableField("to_seed_type")
    private String toSeedType;

    /**
     * 删除标记：0-未删除，1-已删除
     */
    @TableField("deleted")
    private String deleted;
}
