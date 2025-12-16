package com.inspur.seed.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.inspur.seed.domain.entity.ApprovalComment;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 育种批次信息DTO
 *
 * @author AI Assistant
 * @date 2025-12-15
 */
@Data
public class BreedingBatchDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 数据标识(主键)
     */
    private String dataId;

    /**
     * 计划名称
     */
    private String batchName;

    /**
     * 育种批次ID
     */
    private String batchId;

    /**
     * 作物类型
     */
    private String cropType;

    /**
     * 作物类型名称（中文）
     */
    private String cropTypeName;

    /**
     * 品种编码
     */
    private String varietyCode;

    /**
     * 品种名称
     */
    private String varietyName;

    /**
     * 繁育方法
     */
    private String breedingMethod;

    /**
     * 繁育方法名称（中文）
     */
    private String breedingMethodName;

    /**
     * 亲本来源
     */
    private String germplasmSource;
    /**
     * 亲本种子来源
     */
    private String parentalSeedSource;

    /**
     * 育种目标
     */
    private String objective;

    /**
     * 开展年份
     */
    private Integer year;


    private String status;
    /**
     * 工作流状态
     */
    private String workflowStatus;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 修改人
     */
    private String updateBy;

    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 审批意见
     */
    private ApprovalComment approvalComment;

}
