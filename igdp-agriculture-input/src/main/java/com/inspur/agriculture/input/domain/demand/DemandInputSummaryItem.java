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
 * 农资汇聚统计表
 *
 * @author inspur
 * @date 2025-12-09
 */
@Data
@TableName("demand_input_summary_item")
public class DemandInputSummaryItem implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /** 农资分类 */
    private String inputCategory;

    /** 农资类型 */
    private String inputType;

    /** 总数量 */
    private BigDecimal totalQuantity;

    /** Woreda调整后数量 */
    private BigDecimal adjustedQuantity;

    /** 是否已调整 */
    private Integer hasAdjustment;

    /** 最新调整备注 */
    private String adjustmentRemark;

    /** 调整状态: pending/adjusted/submitted/approved/rejected */
    private String adjustmentStatus;

    /** 提交到Zone时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date submittedToZoneTime;

    /** 总项目数 */
    private Integer totalCount;

    /** 涉及需求数 */
    private Integer demandCount;

    /** 包含的品种(逗号分隔) */
    private String varieties;

    /** 包含的规格(逗号分隔) */
    private String specifications;

    /** 包含的单位(逗号分隔) */
    private String units;

    /** 来源名称 */
    private String sourceName;

    /** 来源编码 */
    private String sourceCode;

    /** 目标名称 */
    private String targetName;

    /** 目标编码 */
    private String targetCode;

    /** 季节 */
    private String season;

    /** 状态: 0-待审核/1-成功/2-拒绝 */
    private String status;

    /** 汇聚统计时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedTime;

    private String summaryId;
}
