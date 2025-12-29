package com.inspur.seed.breeding.laboratoryTest.domain.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 实验室测试数据采集DTO
 *
 * @author igdp
 * @date 2025-11-26
 */
@Data
public class LaboratoryTestDataDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 数据ID
     */
    private String dataId;

    /**
     * 样本ID(必填)
     */
    private String sampleId;

    /**
     * 样本状态(必填)
     */
    private String sampleCondition;

    /**
     * 发芽率(必填)
     */
    private BigDecimal germinationRate;

    /**
     * 纯度百分比(必填)
     */
    private BigDecimal purityPercent;

    /**
     * 含水量百分比(必填)
     */
    private BigDecimal moistureContentPercent;

    /**
     * 蛋白质百分比(必填)
     */
    private BigDecimal proteinPercent;

    /**
     * 毒素水平PPM
     */
    private BigDecimal toxinLevelPpm;

    /**
     * 种子健康发现(必填)
     */
    private String seedHealthFindings;

    /**
     * 链路责任(必填)
     */
    private String traceabilityLink;

    /**
     * 实验室报告文件(文件路径)
     */
    private String labReportFile;

    /**
     * 工作流状态
     */
    private String workflowStatus;

    /**
     * 审核记录作废标记
     */
    private Integer auditCanceled;

    /**
     * 批次ID
     */
    private String batchId;

    /**
     * 试验ID
     */
    private String trialId;

    /**
     * 测试日期
     */
    private String testDate;

    /**
     * 样本类型
     */
    private String sampleType;

    /**
     * 实验室参数
     */
    private String labParameter;

    /**
     * 结果值
     */
    private String resultValue;

    /**
     * 通过/失败标志
     */
    private String passFailFlag;

    /**
     * 检测机构
     */
    private String testOrganization;

    /**
     * 检测人员
     */
    private String testerName;

    /**
     * 审核意见
     */
    private String auditOpinion;

    /**
     * 分页参数
     */
    private Integer pageNum;

    /**
     * 分页参数
     */
    private Integer pageSize;
}
