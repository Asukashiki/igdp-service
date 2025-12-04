package com.inspur.seed.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Demand Lock DTO (Ministry Final Approval)
 *
 * @author igdp
 * @date 2025-12-04
 */
@Data
public class DemandLockDTO {

    /**
     * Batch ID
     */
    @NotBlank(message = "Batch ID cannot be empty")
    private String batchId;

    /**
     * Batch Version (Optimistic Lock)
     */
    @NotNull(message = "Version cannot be empty")
    private Integer version;
}
