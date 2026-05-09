package com.inspur.agriculture.input.dto.demand;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.sql.Date;

@Data
public class DemandOrganDTO {

    @NotBlank(message = "Source code cannot be empty")
    private String sourceCode;

    private String sourceName;

    private String targetCode;

    private String targetName;

    private String nextRegionCode;

    private String nextRegionName;

    @NotBlank(message = "Year cannot be empty")
    private String year;

    @NotBlank(message = "Level cannot be empty")
    private String level;

    private String summaryId;

    private Date startDate;

    private Date endDate;

}
