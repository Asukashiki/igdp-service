package com.inspur.agriculture.input.dto.demand;

import lombok.Data;

@Data
public class DemandOrganDTO {

    private String sourceCode;

    private String sourceName;

    private String targetCode;

    private String targetName;

    private String nextRegionCode;

    private String nextRegionName;

}
