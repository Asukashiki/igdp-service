package com.inspur.seed.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 实验室测试数据DTO
 *
 * @author igdp
 * @date 2025-11-29
 */
@Data
public class BreedingLabTestDTO {

    /**
     * 主键ID
     */
    @JsonProperty("dataId")
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
     * 样本编号
     */
    private String sampleId;

    /**
     * 样本状态 - 前端使用sampleCondition
     */
    @JsonProperty("sampleCondition")
    private String sampleStatus;

    /**
     * 发芽率(%)
     */
    private BigDecimal germinationRate;

    /**
     * 纯度(%)
     */
    private BigDecimal purityPercent;

    /**
     * 水分含量(%)
     */
    private BigDecimal moistureContentPercent;

    /**
     * 蛋白质含量(%)
     */
    private BigDecimal proteinPercent;

    /**
     * 毒素水平(PPM)
     */
    private BigDecimal toxinLevelPpm;

    /**
     * 种子健康发现
     */
    private String seedHealthFindings;

    /**
     * 链路责任 - 前端使用traceabilityLink
     */
    @JsonProperty("traceabilityLink")
    private String chainResponsibility;

    /**
     * 实验室报告文件路径
     */
    private String labReportFile;

    /**
     * 检测日期
     */
    private LocalDate testDate;

    /**
     * 检测机构
     */
    private String testOrganization;

    /**
     * 检测人员
     */
    private String testerName;

    /**
     * 备注
     */
    private String remark;

    // 查询条件字段
    /**
     * 检测日期开始
     */
    private LocalDate testDateStart;

    /**
     * 检测日期结束
     */
    private LocalDate testDateEnd;

    /**
     * 页码
     */
    private Integer pageNum;

    /**
     * 每页大小
     */
    private Integer pageSize;
}
