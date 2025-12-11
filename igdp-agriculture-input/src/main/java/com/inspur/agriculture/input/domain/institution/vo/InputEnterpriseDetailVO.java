package com.inspur.agriculture.input.domain.institution.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 机构注册申请详情VO
 *
 * @author system
 */
@Data
public class InputEnterpriseDetailVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 机构ID
     */
    private String id;

    /**
     * 机构名称
     */
    private String enterpriseName;

    /**
     * 企业注册号
     */
    private String enterpriseRegistrationId;

    /**
     * 统一社会信用代码
     */
    private String unifiedSocialCreditCode;

    /**
     * 种子企业许可证号
     */
    private String seedEnterpriseLicenseNumber;

    /**
     * 许可证有效期开始
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate licenseValidityStart;

    /**
     * 许可证有效期结束
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate licenseValidityEnd;

    /**
     * 企业类型
     */
    private String enterpriseType;

    /**
     * 机构类型
     */
    private String orgType;

    /**
     * 投入品类型数组
     */
    private List<String> inputTypes;

    /**
     * 销售区域代码数组
     */
    private List<String> salesRegions;

    /**
     * 申请状态
     */
    private String applicationStatus;

    /**
     * 版本号
     */
    private Integer version;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTime;

    /**
     * 位置信息
     */
    private InputLocationVO location;

    /**
     * 许可证件列表
     */
    private List<InputLicenseVO> licenses;

    /**
     * 审核记录列表
     */
    private List<InputAuditRecordVO> auditRecords;
}
