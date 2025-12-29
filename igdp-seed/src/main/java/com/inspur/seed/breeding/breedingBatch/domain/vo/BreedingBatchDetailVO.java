package com.inspur.seed.breeding.breedingBatch.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.inspur.seed.breeding.breedingBatch.domain.entity.ApprovalComment;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 繁殖批次详细信息VO（包含审批意见）
 *
 * @author igdp
 * @date 2025-12-15
 */
@Data
public class BreedingBatchDetailVO implements Serializable {

    /** 数据标识(主键) */
    private String dataId;

    /** 计划名称 */
    private String batchName;

    /** 育种批次ID */
    private String batchId;

    /** 作物类型 */
    private String cropType;

    /** 作物类型名称（中文） */
    private String cropTypeName;

    /** 品种编码 */
    private String varietyCode;

    /** 品种名称 */
    private String varietyName;

    /** 繁育方法 */
    private String breedingMethod;

    /** 繁育方法名称（中文） */
    private String breedingMethodName;

    /** 亲本来源 */
    private String germplasmSource;
    /** 亲本种子来源 */
    private String parentalSeedSource;

    /** 育种目标 */
    private String objective;

    /** 开展年份 */
    private Integer year;

    private String status;

    /**
     * 批次状态
     * 使用BreedingBatchStatusEnum枚举定义的状态码：
     * S0: 草稿
     * S1: 待审核
     * S2: 审核通过
     * S3: 审核驳回
     * S9: 已归档
     * S10: 已作废
     */
    private String workflowStatus;

    /** 备注 - 映射到数据库remarks字段 */
    private String remarks;


    /**
     * 创建者
     */
    private String createBy;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 更新者
     */
    private String updateBy;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;


    /**
     * 审批意见列表
     */
    private List<ApprovalComment> approvalComments;


}
