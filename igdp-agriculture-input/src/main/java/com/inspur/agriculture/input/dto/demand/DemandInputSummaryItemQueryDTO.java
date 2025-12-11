package com.inspur.agriculture.input.dto.demand;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 农资汇聚统计查询 DTO
 *
 * @author inspur
 * @date 2025-12-09
 */
@Data
public class DemandInputSummaryItemQueryDTO {

    /** 农资分类 */
    private String inputCategory;

    /** 农资类型 */
    private String inputType;

    /** 状态: 0-待审核/1-成功/2-拒绝 */
    private String status;

    /** 来源名称 */
    private String sourceName;

    /** 来源编码 */
    private String sourceCode;

    /** 目标名称 */
    private String targetName;

    /** 目标编码 */
    private String targetCode;

    /** 关键字搜索 */
    private String keyword;

    /** 开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /** 结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    private String summaryId;
}
