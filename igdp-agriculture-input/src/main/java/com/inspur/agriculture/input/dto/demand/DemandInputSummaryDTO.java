package com.inspur.agriculture.input.dto.demand;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 农资需求汇总 DTO
 *
 * @author inspur
 * @date 2025-12-10
 */
@Data
public class DemandInputSummaryDTO {

    /**
     * 主键ID (更新时使用)
     */
    private String id;

    /**
     * 来源编码
     */
    @NotBlank(message = "来源编码不能为空")
    private String sourceCode;

    /**
     * 来源名称
     */
    private String sourceName;

    /**
     * 目标编码
     */
    private String targetCode;

    /**
     * 目标名称
     */
    private String targetName;

    /**
     * 状态
     */
    private String status;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 年份
     */
    private String year;

    private Integer subQuantity;
}
