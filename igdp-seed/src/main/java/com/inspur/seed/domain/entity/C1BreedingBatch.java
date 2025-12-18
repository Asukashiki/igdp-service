package com.inspur.seed.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * C1繁殖批次实体类
 * @author system
 * @since 2025-12-08
 */
@Data
@TableName("c1_breeding_batch")
public class C1BreedingBatch implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 批次编号
     */
    @TableField("batch_id")
    private String batchId;

    /**
     * 关联的C1繁殖申请ID
     */
    @TableField("propagation_id")
    private String propagationId;

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
     * 繁殖级别
     */
    @TableField("breeding_level")
    private String breedingLevel;

    /**
     * 繁育方法
     */
    @TableField("breeding_method")
    private String breedingMethod;

    /**
     * 亲本种子来源
     */
    @TableField("parent_seed_source")
    private String parentSeedSource;

    /**
     * 开始日期
     */
    @TableField("start_date")
    private LocalDate startDate;

    /**
     * 结束日期
     */
    @TableField("end_date")
    private LocalDate endDate;

    /**
     * 预期产量(kg)
     */
    @TableField("expected_yield")
    private BigDecimal expectedYield;

    /**
     * 实际产量(kg)
     */
    @TableField("actual_yield")
    private BigDecimal actualYield;

    /**
     * 种植面积(公顷)
     */
    @TableField("planting_area")
    private BigDecimal plantingArea;

    /**
     * 批次状态
     */
    @TableField("batch_status")
    private String batchStatus;

    /**
     * 机构ID
     */
    @TableField("org_id")
    private String orgId;

    /**
     * 机构名称
     */
    @TableField("org_name")
    private String orgName;

    /**
     * 机构类型
     */
    @TableField("org_type")
    private String orgType;

    /**
     * 繁殖地点
     */
    @TableField("location")
    private String location;

    /**
     * 跟踪记录数
     */
    @TableField("tracking_count")
    private Integer trackingCount;

    /**
     * 检测记录数
     */
    @TableField("test_count")
    private Integer testCount;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

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
     * 审核状态: pending-待审核, approved-已通过, rejected-已驳回
     */
    @TableField("audit_status")
    private String auditStatus;

    /**
     * 审核人
     */
    @TableField("auditor")
    private String auditor;

    /**
     * 审核人组织ID
     */
    @TableField("auditor_org_id")
    private String auditorOrgId;

    /**
     * 审核人组织名称
     */
    @TableField("auditor_org_name")
    private String auditorOrgName;

    /**
     * 审核时间
     */
    @TableField("audit_time")
    private LocalDateTime auditTime;

    /**
     * 审核意见
     */
    @TableField("audit_comment")
    private String auditComment;

    /**
     * 打印次数
     */
    @TableField("print_count")
    private Integer printCount;

    /**
     * 最后打印时间
     */
    @TableField("last_print_time")
    private LocalDateTime lastPrintTime;

    /**
     * 删除标记
     */
    @TableField("deleted")
    @TableLogic
    private String deleted;
}
