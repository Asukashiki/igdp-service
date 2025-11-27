package com.inspur.seed.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 企业审核展示视图对象
 *
 * @author system
 */
@Setter
@Getter
public class EnterpriseAuditVO {

    /**
     * 审核记录唯一标识
     */
    private String auditId;

    /**
     * 企业ID
     */
    private String enterpriseId;

    /**
     * 企业名称
     */
    private String enterpriseName;

    /**
     * 统一社会信用代码
     */
    private String unifiedSocialCreditCode;

    /**
     * 许可证号
     */
    private String seedLicenseNo;

    /**
     * 申请日期（创建时间）
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 当前审核阶段
     */
    private String auditStage;

    /**
     * 分配审核人
     */
    private String auditor;

    /**
     * 审核结果
     */
    private Integer auditResult;

    /**
     * 认证状态
     */
    private Integer certificationStatus;

    /**
     * 企业类型（Production-oriented/trade-oriented/integrated）
     */
    private String enterpriseType;

    /**
     * 许可证有效期起始日
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate licenseStartDate;

    /**
     * 许可证有效期截止日
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate licenseEndDate;

    /**
     * 完整地址
     */
    private String detailedAddress;

    /**
     * 企业成立时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate establishmentDate;

    /**
     * 法人姓名
     */
    private String legalPersonName;


    /**
     * 法人ID
     */
    private String legalPersonId;

    /**
     * 联系人姓名
     */
    private String contactPerson;

    /**
     * 联系电话
     */
    private String contactPhone;

    /**
     * 邮箱
     */
    private String contactEmail;

    /**
     * 营业执照存储路径
     */
    private String businessLicenseUrl;

    /**
     * 种子许可证存储路径
     */
    private String seedLicenseUrl;

    /**
     * 税务登记证存储路径
     */
    private String taxRegistrationUrl;

    /**
     * 工厂许可证存储路径
     */
    private String factoryLicenseUrl;


    /**
     * 审核意见
     */
    private String auditOpinion;


    /**
     * 驳回原因（驳回时必填）
     */
    private String rejectReason;





}