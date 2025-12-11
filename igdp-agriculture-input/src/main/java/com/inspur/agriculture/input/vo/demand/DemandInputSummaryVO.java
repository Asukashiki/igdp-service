package com.inspur.agriculture.input.vo.demand;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 农资需求汇总 VO
 *
 * @author inspur
 * @date 2025-12-10
 */
@Data
public class DemandInputSummaryVO {

    /**
     * 主键ID
     */
    private String id;

    /**
     * 来源编码
     */
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
     * 状态描述
     */
    private String statusDesc;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

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
