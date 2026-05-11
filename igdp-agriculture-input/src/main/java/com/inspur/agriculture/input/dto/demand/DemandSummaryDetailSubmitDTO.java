package com.inspur.agriculture.input.dto.demand;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class DemandSummaryDetailSubmitDTO {

    @NotBlank(message = "Detail ID cannot be empty")
    private String detailId;

    @NotBlank(message = "Summary ID cannot be empty")
    private String summaryId;

    @NotBlank(message = "Year cannot be empty")
    private String year;

    @NotBlank(message = "Source code cannot be empty")
    private String sourceCode;

    @NotBlank(message = "Target code cannot be empty")
    private String targetCode;

    @NotBlank(message = "Level cannot be empty")
    private String level;

    private String currentUserId;

    private String currentUserName;
}
