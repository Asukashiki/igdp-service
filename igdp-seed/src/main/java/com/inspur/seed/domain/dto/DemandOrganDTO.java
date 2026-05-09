package com.inspur.seed.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class DemandOrganDTO {

    @NotBlank(message = "Source code cannot be empty")
    private String sourceCode;

    private String sourceName;

    private String targetCode;

    private String targetName;

    @NotBlank(message = "Year cannot be empty")
    private String year;

    @NotBlank(message = "Level cannot be empty")
    private String level;

    private String demandSummaryId;

    private String variety;

}
