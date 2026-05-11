package com.inspur.agriculture.input.dto.demand;

import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class DemandSummaryDetailAdjustDTO {

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

    @NotNull(message = "Adjusted quantity cannot be empty")
    @DecimalMin(value = "0.00", message = "Adjusted quantity cannot be negative")
    private BigDecimal adjustedQuantity;

    @NotBlank(message = "Adjustment remark cannot be empty")
    private String adjustmentRemark;

    private String currentUserId;

    private String currentUserName;
}
