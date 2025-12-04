package com.inspur.seed.domain.dto.registration;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 提交机构注册申请DTO
 *
 * @author system
 */
@Data
public class EnterpriseSubmitDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 机构ID
     */
    @NotBlank(message = "Enterprise ID is required")
    private String id;

    /**
     * 版本号
     */
    @NotNull(message = "Version is required")
    private Integer version;
}
