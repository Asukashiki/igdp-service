package com.inspur.seed.breeding.laboratoryTest.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 实验室测试数据采集VO
 *
 * @author igdp
 * @date 2025-11-26
 */
@Data
public class LaboratoryTestDataVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 数据ID
     */
    private String dataId;

    /**
     * 样本ID
     */
    private String sampleId;

    /**
     * 样本状态
     */
    private String sampleCondition;

    /**
     * 发芽率(百分比)
     */
    private BigDecimal germinationRate;

    /**
     * 纯度(百分比)
     */
    private BigDecimal purityPercent;

    /**
     * 含水量(百分比)
     */
    private BigDecimal moistureContentPercent;

    /**
     * 蛋白质(百分比)
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
     * 链路责任
     */
    private String traceabilityLink;

    /**
     * 实验室报告文件
     */
    private String labReportFile;

    /**
     * 实验室报告文件名称
     */
    private String labReportFileName;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 更新人
     */
    private String updateBy;

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
     * 审批人
     */
    private String approveBy;

    /**
     * 审批人姓名
     */
    private String approveByName;

    /**
     * 审批时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date approveTime;

    /**
     * 审核意见
     */
    private String auditOpinion;

    /**
     * 创建人姓名
     */
    private String createdByName;

    /**
     * 创建时间（格式化）
     */
    private String createdTime;

    /**
     * 更新时间（格式化）
     */
    private String updatedTime;

    /**
     * 退回人机构名称
     */
    private String rejectOrgName;
}
