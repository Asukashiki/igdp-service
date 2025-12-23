package com.inspur.seed.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.inspur.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 实验室测试数据采集实体类
 *
 * @author igdp
 * @date 2025-11-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("seed_laboratory_test_data")
public class LaboratoryTestData extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 数据ID(主键)
     */
    @TableId(type = IdType.ASSIGN_UUID)
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
     * 实验室报告文件(文件路径)
     */
    private String labReportFile;

    /**
     * 实验室报告文件名称(原始文件名)
     */
    private String labReportFileName;

    /**
     * 删除标志(0正常 2删除)
     */
    private String delFlag;

    /**
     * 工作流状态(S0草稿 S1待审批 S2已审批 S3已退回 S9已归档 S10作废)
     */
    private String workflowStatus;

    /**
     * 审核记录作废标记(0正常 1审核记录已作废)
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
     * 通过/失败标志(true/false)
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
     * 审批时间
     */
    private String approveTime;

    /**
     * 审核意见
     */
    private String auditOpinion;
}
