package com.inspur.agriculture.input.vo.demand;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class DemandSummaryAdjustmentHistoryVO {

    private String id;

    private String detailId;

    private String summaryId;

    private String sourceCode;

    private String sourceName;

    private String targetCode;

    private String targetName;

    private String inputType;

    private String inputCategory;

    private String variety;

    private String season;

    private String unit;

    private BigDecimal originalQuantity;

    private BigDecimal beforeQuantity;

    private BigDecimal afterQuantity;

    private String remark;

    private String operatorId;

    private String operatorName;

    private String operationType;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;
}
