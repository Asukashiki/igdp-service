package com.inspur.seed.domain.dto.registration;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * 新增机构注册申请DTO
 *
 * @author system
 */
@Data
public class EnterpriseAddDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 机构名称
     */
    @NotBlank(message = "Enterprise name is required")
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
    @NotBlank(message = "Seed enterprise license number is required")
    private String seedEnterpriseLicenseNumber;

    /**
     * 许可证有效期开始
     */
    @NotNull(message = "License validity start date is required")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate licenseValidityStart;

    /**
     * 许可证有效期结束
     */
    @NotNull(message = "License validity end date is required")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate licenseValidityEnd;

    /**
     * 企业类型
     */
    @NotBlank(message = "Enterprise type is required")
    private String enterpriseType;

    /**
     * 机构类型(union/cooperative)
     */
    @NotBlank(message = "Organization type is required")
    private String orgType;

    /**
     * 投入品类型(支持数组或逗号分隔字符串: seed,fertilizer,pesticide)
     */
    @NotNull(message = "Input types are required")
    private Object inputTypes;

    /**
     * 销售区域代码(支持数组或逗号分隔字符串)
     */
    @NotNull(message = "Sales regions are required")
    private Object salesRegions;

    /**
     * 备注
     */
    private String remark;

    /**
     * 位置信息
     */
    @NotNull(message = "Location information is required")
    @Valid
    private LocationDTO location;

    /**
     * 许可证件列表
     */
    @NotEmpty(message = "Licenses are required")
    @Valid
    private List<LicenseDTO> licenses;
}
