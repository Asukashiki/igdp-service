package com.inspur.agriculture.input.dto.demand;

import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 农资汇聚统计 DTO
 *
 * @author inspur
 * @date 2025-12-09
 */
@Data
public class DemandInputSummaryItemDTO {

    /** 主键ID (更新时使用) */
    private String id;

    /** 农资分类 */
    @NotBlank(message = "农资分类不能为空")
    private String inputCategory;

    /** 农资类型 */
    @NotBlank(message = "农资类型不能为空")
    private String inputType;

    /** 总数量 */
    @NotNull(message = "总数量不能为空")
    @DecimalMin(value = "0.00", message = "总数量不能为负数")
    private BigDecimal totalQuantity;

    /** 总项目数 */
    @NotNull(message = "总项目数不能为空")
    @Min(value = 0, message = "总项目数不能为负数")
    private Integer totalCount;

    /** 涉及需求数 */
    @Min(value = 0, message = "涉及需求数不能为负数")
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

    private String summaryId;
}
