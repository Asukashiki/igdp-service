package com.inspur.seed.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * Demand Audit Reject DTO
 *
 * @author igdp
 * @date 2025-12-04
 */
@Data
public class DemandAuditRejectDTO {

    /**
     * Demand IDs
     */
    @NotEmpty(message = "Demand IDs cannot be empty")
    private List<String> ids;

    /**
     * Audit Opinion (required when rejecting)
     */
    @NotBlank(message = "Audit opinion cannot be empty")
    private String auditOpinion;

    /**
     * Audit Remark
     */
    private String remark;
}
