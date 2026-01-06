package com.inspur.agriculture.input.dto.demand;

import lombok.Data;

import java.sql.Date;

@Data
public class DemandOrganDTO {

    private String sourceCode;

    private String sourceName;

    private String targetCode;

    private String targetName;

    private String nextRegionCode;

    private String nextRegionName;

    private String year;

    private String summaryId;

    private Date startDate;

    private Date endDate;

}
