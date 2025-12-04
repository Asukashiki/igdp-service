package com.inspur.seed.domain.vo.registration;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * 许可证件VO
 *
 * @author system
 */
@Data
public class LicenseVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 许可证ID
     */
    private String id;

    /**
     * 证件类型
     */
    private String licenseType;

    /**
     * 证件类型名称
     */
    private String licenseTypeName;

    /**
     * 证件号码
     */
    private String licenseNumber;

    /**
     * 证件文件URL
     */
    private String licenseFileUrl;

    /**
     * 发证日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate issueDate;

    /**
     * 到期日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate expiryDate;
}
