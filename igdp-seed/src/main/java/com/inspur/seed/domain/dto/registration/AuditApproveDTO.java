package com.inspur.seed.domain.dto.registration;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 审核通过DTO
 *
 * @author system
 */
@Data
public class AuditApproveDTO implements Serializable {

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

    /**
     * 审核备注
     */
    private String remark;
}
