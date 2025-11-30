package com.inspur.seed.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.inspur.common.core.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 品种登记信息实体类
 *
 * @author system
 */
@TableName("variety_registration")
@Setter
@Getter
public class VarietyRegistration extends BaseEntity {

    /**
     * 登记申请唯一标识（主键，系统生成）
     */
    @TableId
    private String registrationId;

    /**
     * 登记申请号（格式：VAR+年月日+6位随机数）
     */
    private String registrationNo;

    /**
     * 关联企业唯一标识
     */
    private String enterpriseId;

    /**
     * 企业名称（冗余字段，便于查询）
     */
    private String enterpriseName;

    /**
     * 统一社会信用代码（冗余）
     */
    private String unifiedSocialCreditCode;

    /**
     * 企业类型（枚举：生产型/贸易型/综合型）
     */
    private String enterpriseType;

    /**
     * 种子许可证编号（冗余）
     */
    private String seedLicenseNo;

    /**
     * 备案类型
     */
    private String recordType;

    /**
     * 备案日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate recordDate;

    /**
     * 备案状态（0-审核中/1-待发布/2-审核未通过/3-已发布）
     */
    private Integer recordStatus;

    /**
     * 品种名称
     */
    private String varietyName;

    /**
     * 品种代码
     */
    private String varietyCode;

    /**
     * 作物类型
     */
    private String cropType;

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
     * 最低产量潜力（公担/公顷）
     */
    private BigDecimal minYieldPotential;

    /**
     * 最高产量潜力（公担/公顷）
     */
    private BigDecimal maxYieldPotential;

    /**
     * 抗病性
     */
    private String diseaseResistance;

    /**
     * 抗逆性
     */
    private String stressResistance;

    /**
     * 生育期（天）
     */
    private Integer growthPeriod;

    /**
     * 株高（厘米）
     */
    private BigDecimal plantHeight;

    /**
     * 谷物质量性状
     */
    private String grainQualityTraits;

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
    private BigDecimal averageYield;

    /**
     * 稳定性评分
     */
    private BigDecimal stabilityScore;

    /**
     * 试验报告存储路径
     */
    private String testReportUrl;

    /**
     * 照片存储路径
     */
    private String photoUrl;

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

    /**
     * 操作人
     */
    private String operator;

    /**
     * 操作机构
     */
    private String operationOrg;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 操作时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime operationTime;
}
