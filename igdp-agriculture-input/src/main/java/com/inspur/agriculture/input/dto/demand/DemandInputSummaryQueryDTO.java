package com.inspur.agriculture.input.dto.demand;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 农资需求汇总查询 DTO
 *
 * @author inspur
 * @date 2025-12-10
 */
@Data
public class DemandInputSummaryQueryDTO {

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
     * 年份
     */
    private String year;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 关键字搜索
     */
    private String keyword;

    /**
     * 开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /**
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
}
