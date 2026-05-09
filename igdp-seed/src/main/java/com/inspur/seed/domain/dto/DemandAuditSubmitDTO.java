package com.inspur.seed.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * Demand Audit Submit DTO
 *
 * @author igdp
 * @date 2025-12-04
 */
@Data
public class DemandAuditSubmitDTO {

    /**
     * Demand IDs
     */
    @NotEmpty(message = "Demand IDs cannot be empty")
    private List<String> ids;

    private String currentUserId;

    private String currentUserName;

    @NotBlank(message = "Source code cannot be empty")
    private String sourceCode;

    @NotBlank(message = "Level cannot be empty")
    private String level;

    @NotBlank(message = "Year cannot be empty")
    private String year;
}
