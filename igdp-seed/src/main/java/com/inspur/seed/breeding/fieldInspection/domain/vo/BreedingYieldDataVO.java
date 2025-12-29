package com.inspur.seed.breeding.fieldInspection.domain.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 产量数据VO
 *
 * @author igdp
 * @date 2025-11-29
 */
@Data
public class BreedingYieldDataVO {

    /**
     * 主键ID
     */
    private String id;

    /**
     * 育种批次ID
     */
    private String batchId;

    /**
     * 试验ID
     */
    private String trialId;

    /**
     * 地块编号
     */
    private String plotId;

    /**
     * 地块面积(m²)
     */
    private BigDecimal plotAreaM2;

    /**
     * 谷物重量(kg)
     */
    private BigDecimal grainWeightKg;

    /**
     * 产量(公担/公顷)
     */
    private BigDecimal yieldQtPerHa;

    /**
     * 含水量(%)
     */
    private BigDecimal moistureContent;

    /**
     * 收获日期
     */
    private LocalDate harvestDate;

    /**
     * 记录人员
     */
    private String recorderName;

    /**
     * 备注
     */
    private String remark;

    /**
     * 检验日期
     */
    private LocalDate inspectionDate;

    /**
     * 检验类型
     */
    private String inspectionType;

    /**
     * 评分代码
     */
    private String scoreCode;

    /**
     * 评分值
     */
    private String scoreValue;

    /**
     * 业务状态：submit/approve
     */
    private String status;

    /**
     * 流程审核状态（字典 flow_status）
     */
    private String workflowStatus;

    /**
     * 创建人ID
     */
    private String createdBy;

    /**
     * 创建时间
     */
    private LocalDateTime createdTime;

    /**
     * 修改人ID
     */
    private String updateBy;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;

    /**
     * 审核人
     */
    private String auditBy;

    /**
     * 审核时间
     */
    private LocalDateTime auditTime;

    /**
     * 审核意见
     */
    private String auditRemark;
}
