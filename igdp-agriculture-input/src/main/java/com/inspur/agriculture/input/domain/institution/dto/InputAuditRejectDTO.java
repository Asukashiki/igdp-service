package com.inspur.agriculture.input.domain.institution.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 审核驳回DTO
 *
 * @author system
 */
@Data
public class InputAuditRejectDTO implements Serializable {

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
     * 审核意见
     */
    @NotBlank(message = "Audit opinion is required")
    private String auditOpinion;

    /**
     * 审核备注
     */
    private String remark;
}
