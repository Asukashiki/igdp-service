package com.inspur.seed.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 繁殖检测信息VO
 *
 * @author igdp
 * @date 2025-11-29
 */
@Data
public class BreedingTestVO {

    /**
     * 主键ID
     */
    private String id;

    /**
     * 检测编号
     */
    private String testId;

    /**
     * 关联跟踪编号
     */
    private String trackingId;

    /**
     * 关联繁殖批次编号
     */
    private String batchId;

    /**
     * 作物种类
     */
    private String cropType;

    /**
     * 作物种类名称
     */
    private String cropTypeName;

    /**
     * 发芽率（%）
     */
    private BigDecimal germinationRate;

    /**
     * 纯度（%）
     */
    private BigDecimal purity;

    /**
     * 水分含量（%）
     */
    private BigDecimal moistureContent;

    /**
     * 病虫害检测结果
     */
    private String pestDetection;

    /**
     * 检测日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date testDate;

    /**
     * 检测机构
     */
    private String testOrg;

    /**
     * 检测人员
     */
    private String testPerson;

    /**
     * 检测结论
     */
    private String testResult;

    /**
     * 检测结论名称
     */
    private String testResultName;

    /**
     * 检测报告附件路径
     */
    private String testReportUrl;

    /**
     * 操作机构ID
     */
    private String orgId;

    /**
     * 操作机构名称
     */
    private String orgName;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
}
