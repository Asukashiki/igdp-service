package com.inspur.seed.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 育种数据集审核VO
 *
 * @author system
 * @since 2025-01-30
 */
@Data
public class BreedingDatasetAuditVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private String id;

    /**
     * 数据集ID
     */
    private String datasetId;

    /**
     * 数据集编号
     */
    private String datasetCode;

    /**
     * 育种批次ID
     */
    private String batchId;

    /**
     * 育种批次名称
     */
    private String batchName;

    /**
     * 作物类型
     */
    private String cropType;

    /**
     * 品种名称
     */
    private String varietyName;

    /**
     * 试验ID
     */
    private String trialId;

    /**
     * 版本号
     */
    private String versionNo;

    /**
     * 编制人
     */
    private String compiledBy;

    /**
     * 编制人姓名
     */
    private String compiledByName;

    /**
     * 编制时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime compiledAt;

    /**
     * 记录数量
     */
    private Integer recordCount;

    /**
     * 数据集状态
     */
    private String datasetStatus;

    /**
     * 试验记录数
     */
    private Integer trialCount;

    /**
     * 田间数据记录数
     */
    private Integer fieldDataCount;

    /**
     * 环境数据记录数
     */
    private Integer envDataCount;

    /**
     * 实验室检测记录数
     */
    private Integer labTestCount;

    /**
     * 产量数据记录数
     */
    private Integer yieldDataCount;

    /**
     * 审核节点
     */
    private String auditNode;

    /**
     * 审核顺序
     */
    private Integer auditOrder;

    /**
     * 审核状态:pending/approved/rejected
     */
    private String auditStatus;

    /**
     * 审核意见
     */
    private String auditOpinion;

    /**
     * 审核时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime auditTime;

    /**
     * 审核人ID
     */
    private String auditorId;

    /**
     * 审核人姓名
     */
    private String auditorName;

    /**
     * 审核人机构代码
     */
    private String auditorOrgCode;

    /**
     * 审核人机构名称
     */
    private String auditorOrgName;

    /**
     * 锁定标记:0未锁定1已锁定(锁定后不可修改)
     */
    private Integer lockedFlag;

    /**
     * 提交时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime submitTime;

    /**
     * 提交人ID
     */
    private String submitterId;

    /**
     * 提交人姓名
     */
    private String submitterName;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createdTime;
}
