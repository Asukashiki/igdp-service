package com.inspur.seed.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.inspur.common.core.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Union基本信息实体类
 *
 * @author system
 */
@TableName("union_info")
@Setter
@Getter
public class UnionInfo extends BaseEntity {

    /**
     * 数据唯一标识（主键，系统生成）
     */
    @TableId
    private String dataId;

    /**
     * 企业唯一标识（系统生成）
     */
    private String enterpriseId;

    /**
     * 企业名称
     */
    @NotBlank(message = "企业名称不能为空")
    private String enterpriseName;

    /**
     * 企业注册ID
     */
    private String enterpriseRegistrationId;

    /**
     * 统一社会信用代码
     */
    private String unifiedSocialCreditCode;

    /**
     * 种子企业许可证编号
     */
    @NotBlank(message = "种子企业许可证编号不能为空")
    private String seedEnterpriseLicenseNumber;

    /**
     * 许可证有效期起始日
     */
    @NotNull(message = "许可证有效期起始日不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate licenseValidityStart;

    /**
     * 许可证有效期截止日
     */
    @NotNull(message = "许可证有效期截止日不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate licenseValidityEnd;

    /**
     * 企业类型
     */
    @NotBlank(message = "企业类型不能为空")
    private String enterpriseType;

    /**
     * 地区
     */
    private String region;

    /**
     * 区域
     */
    private String zone;

    /**
     * Woreda
     */
    @NotBlank(message = "Woreda不能为空")
    private String woreda;

    /**
     * Kebele
     */
    @NotBlank(message = "Kebele不能为空")
    private String kebele;

    /**
     * 完整地址
     */
    @NotBlank(message = "完整地址不能为空")
    private String fullAddress;

    /**
     * GPS纬度
     */
    @NotNull(message = "GPS纬度不能为空")
    private BigDecimal gpsLatitude;

    /**
     * GPS经度
     */
    private BigDecimal gpsLongitude;

    /**
     * 业务范围
     */
    private String businessScope;

    /**
     * 年生产能力
     */
    private BigDecimal annualProductionCapacity;

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
     * 认证状态（-1草稿/0待审核/1通过/2驳回）
     */
    private Integer certificationStatus;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 驳回原因
     */
    private String rejectReason;
}
