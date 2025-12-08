package com.inspur.seed.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 育种数据集VO
 *
 * @author system
 * @date 2025-01-30
 */
@Data
public class BreedingDatasetVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private String id;

    /**
     * 数据集编号
     */
    private String datasetCode;

    /**
     * 试验ID
     */
    private String trialId;

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
     * 版本号
     */
    private Integer versionNo;

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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime compiledAt;

    /**
     * 记录数量
     */
    private Integer recordCount;

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
     * 数据集状态
     */
    private String datasetStatus;

    /**
     * 提交时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime submitTime;

    /**
     * 提交人ID
     */
    private String submitBy;

    /**
     * 提交人姓名
     */
    private String submitByName;

    /**
     * 提交机构代码
     */
    private String submitOrgCode;

    /**
     * 提交机构名称
     */
    private String submitOrgName;

    /**
     * 创建人ID
     */
    private String createdBy;

    /**
     * 创建人姓名
     */
    private String createdByName;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedTime;

    /**
     * 备注
     */
    private String remark;
}
