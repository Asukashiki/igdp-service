package com.inspur.agriculture.input.domain.demand;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 农资需求汇总主表
 *
 * @author inspur
 * @date 2025-12-10
 */
@Data
@TableName("demand_input_summary")
public class DemandInputSummary implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
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
