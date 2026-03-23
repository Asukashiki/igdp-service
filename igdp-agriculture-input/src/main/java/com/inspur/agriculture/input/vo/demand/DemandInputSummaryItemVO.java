package com.inspur.agriculture.input.vo.demand;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 农资汇聚统计 VO
 *
 * @author inspur
 * @date 2025-12-09
 */
@Data
public class DemandInputSummaryItemVO {

    /** 主键ID */
    private String id;

    /** 农资分类 */
    private String inputCategory;

    /** 农资类型 */
    private String inputType;


    /** 总数量 */
    private BigDecimal totalQuantity;

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

    /** 状态: 0-待审核/1-成功/2-拒绝 */
    private String status;

    /** 状态描述 */
    private String statusDesc;

    /** 汇聚统计时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedTime;
}
