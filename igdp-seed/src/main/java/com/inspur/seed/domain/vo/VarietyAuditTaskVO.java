package com.inspur.seed.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 品种审核任务VO类
 * 用于审核任务列表和详情页面展示
 *
 * @author system
 */
@Data
public class VarietyAuditTaskVO {
    
    /**
     * 登记申请ID
     */
    private String registrationId;
    
    /**
     * 申请号
     */
    private String registrationNo;
    
    /**
     * 品种名称
     */
    private String varietyName;
    
    /**
     * 作物类型
     */
    private String cropType;
    
    /**
     * 提交单位（企业名称）
     */
    private String enterpriseName;
    
    /**
     * 审核状态（0-审核中/1-待发布/2-审核未通过）
     */
    private Integer recordStatus;
    
    /**
     * 审核结果（1-通过/2-驳回）
     */
    private Integer auditResult;
    
    /**
     * 审核意见
     */
    private String auditOpinion;
    
    /**
     * 驳回原因
     */
    private String rejectReason;
    
    /**
     * 审核人
     */
    private String auditor;
    
    /**
     * 审核时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime auditTime;
    
    /**
     * 备案日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate recordDate;
    
    /**
     * 备案类型
     */
    private String recordType;
    
    /**
     * 企业类型
     */
    private String enterpriseType;
    
    /**
     * 统一社会信用代码
     */
    private String unifiedSocialCreditCode;
    
    /**
     * 种子许可证编号
     */
    private String seedLicenseNo;
    

    /**
     * 品种代码
     */
    private String varietyCode;
    
    /**
     * 物种
     */
    private String species;
    
    /**
     * 属
     */
    private String genus;
    
    /**
     * 科
     */
    private String family;
    

    /**
     * 育种方法
     */
    private String breedingMethod;
    
    /**
     * 方法系谱
     */
    private String methodPedigree;
    
    /**
     * 培育年份
     */
    private Integer breedingYear;
    
    /**
     * 最低产量潜力
     */
    private String minYieldPotential;
    
    /**
     * 最高产量潜力
     */
    private String maxYieldPotential;
    
    /**
     * 抗病性
     */
    private String diseaseResistance;
    
    /**
     * 抗逆性
     */
    private String stressResistance;
    
    /**
     * 生育期
     */
    private Integer growthPeriod;
    
    /**
     * 株高
     */
    private String plantHeight;
    
    /**
     * 谷物质量性状
     */
    private String grainQualityTraits;
    
    // 试验和性能数据
    
    /**
     * 试验地点
     */
    private String testLocation;
    
    /**
     * 试验年份
     */
    private Integer testYear;
    
    /**
     * 平均产量
     */
    private String averageYield;
    
    /**
     * 稳定性评分
     */
    private String stabilityScore;
    
    /**
     * 试验报告存储路径
     */
    private String testReportUrl;
    
    // 监管数据
    
    /**
     * 核准文件编号
     */
    private String approvalDocNo;
    
    /**
     * 核准机构
     */
    private String approvalOrg;
    
    /**
     * 核准日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate approvalDate;
    
    /**
     * 认证文件存储路径
     */
    private String certificationDocUrl;
}