package com.inspur.agriculture.input.domain.institution.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * 许可证件DTO
 *
 * @author system
 */
@Data
public class InputLicenseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 许可证ID(修改时传入)
     */
    private String id;

    /**
     * 证件类型(business_license/seed_license/tax_certificate/factory_permit)
     */
    @NotBlank(message = "License type is required")
    private String licenseType;

    /**
     * 证件号码
     */
    private String licenseNumber;

    /**
     * 证件文件URL
     */
    @NotBlank(message = "License file URL is required")
    private String licenseFileUrl;

    /**
     * 发证日期
     */
    private LocalDate issueDate;

    /**
     * 到期日期
     */
    private LocalDate expiryDate;
}
