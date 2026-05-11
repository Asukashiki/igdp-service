package com.inspur.agriculture.input.domain.demand;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 农资需求汇总明细调整历史
 *
 * @author inspur
 */
@Data
@TableName("demand_input_summary_adjustment_history")
public class DemandInputSummaryAdjustmentHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private String detailId;

    private String summaryId;

    private String year;

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

    private Integer deleted;
}
