package com.inspur.seed.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * Farmer Demand Delete DTO
 *
 * @author igdp
 * @date 2025-12-04
 */
@Data
public class FarmerDemandDeleteDTO {

    /**
     * Demand ID
     */
    @NotBlank(message = "Demand ID cannot be empty")
    private String id;
}
