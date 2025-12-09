package com.inspur.seed.domain.vo;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * C1繁殖批次视图对象
 * @author system
 * @since 2025-12-08
 */
@Data
public class C1BreedingBatchVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private String id;

    /**
     * 批次编号
     */
    private String batchId;

    /**
     * 关联的C1繁殖申请ID
     */
    private String propagationId;

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
     * 繁殖级别
     */
    private String breedingLevel;

    /**
     * 繁育方法
     */
    private String breedingMethod;

    /**
     * 亲本种子来源
     */
    private String parentSeedSource;

    /**
     * 开始日期
     */
    private String startDate;

    /**
     * 结束日期
     */
    private String endDate;

    /**
     * 预期产量(kg)
     */
    private BigDecimal expectedYield;

    /**
     * 实际产量(kg)
     */
    private BigDecimal actualYield;

    /**
     * 种植面积(公顷)
     */
    private BigDecimal plantingArea;

    /**
     * 批次状态
     */
    private String batchStatus;

    /**
     * 机构ID
     */
    private String orgId;

    /**
     * 机构名称
     */
    private String orgName;

    /**
     * 机构类型
     */
    private String orgType;

    /**
     * 繁殖地点
     */
    private String location;

    /**
     * 跟踪记录数
     */
    private Integer trackingCount;

    /**
     * 检测记录数
     */
    private Integer testCount;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建人
     */
    private String createdBy;

    /**
     * 创建时间
     */
    private String createdTime;

    /**
     * 更新人
     */
    private String updatedBy;

    /**
     * 更新时间
     */
    private String updatedTime;

    /**
     * 审核状态: pending-待审核, approved-已通过, rejected-已驳回
     */
    private String auditStatus;

    /**
     * 审核人
     */
    private String auditor;

    /**
     * 审核时间
     */
    private String auditTime;

    /**
     * 审核意见
     */
    private String auditComment;

    /**
     * 打印次数
     */
    private Integer printCount;

    /**
     * 最后打印时间
     */
    private String lastPrintTime;
}
