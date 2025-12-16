package com.inspur.seed.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 产量数据实体类
 *
 * @author igdp
 * @date 2025-11-29
 */
@Data
@TableName("breeding_yield_data")
public class BreedingYieldData {

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_UUID)
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
     * 业务状态：submit/approve 等
     */
    private String status;

    /**
     * 创建人ID
     */
    private String createdBy;

    /**
     * 创建人姓名
     */
    private String createdByName;

    /**
     * 创建机构代码
     */
    private String createdOrgCode;

    /**
     * 创建机构名称
     */
    private String createdOrgName;

    /**
     * 创建时间
     */
    private LocalDateTime createdTime;

    /**
     * 更新人ID
     */
    private String updatedBy;

    /**
     * 更新时间
     */
    private LocalDateTime updatedTime;

    /**
     * 审核人
     */
    private String auditBy;

    /**
     * 审核时间
     */
    private LocalDateTime auditTime;

    /**
     * 流程审核状态（字典 flow_status）
     */
    private String workflowStatus;

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
     * 删除标记:0未删除1已删除
     */
    private String deleted;
}
