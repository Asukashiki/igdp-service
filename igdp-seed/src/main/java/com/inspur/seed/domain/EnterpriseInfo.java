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

/**
 * 种子企业认证信息实体类
 *
 * @author system
 */
@TableName("enterprise_info")
@Setter
@Getter
public class EnterpriseInfo extends BaseEntity {

    /**
     * 企业唯一标识（主键，系统生成）
     */
    @TableId
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
     * 企业类型（Production-oriented/trade-oriented/integrated）
     */
    private String enterpriseType;

    /**
     * 种子经营许可证编号
     */
    private String seedLicenseNo;

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
     * 地区
     */
    private String region;

    /**
     * 区域
     */
    private String zone;

    /**
     * 县
     */
    private String county;

    /**
     * 乡
     */
    private String township;

    /**
     * 完整地址
     */
    private String detailedAddress;

    /**
     * 业务范围
     */
    private String businessScope;

    /**
     * 年生产能力（吨/年）
     */
    private BigDecimal annualProductionCapacity;

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
     * 操作人
     */
    private String operator;

    /**
     * 操作机构
     */
    private String operationOrg;

    /**
     * 操作时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime operationTime;

    /**
     * 认证状态（0-待审核/1-通过/2-驳回/-1草稿状态）
     */
    private Integer certificationStatus;
}
