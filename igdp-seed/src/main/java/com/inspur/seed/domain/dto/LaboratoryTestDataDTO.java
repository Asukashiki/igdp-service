package com.inspur.seed.domain.dto;

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
}
