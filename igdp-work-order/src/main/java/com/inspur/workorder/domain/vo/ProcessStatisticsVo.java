package com.inspur.workorder.domain.vo;

import com.inspur.common.annotation.Excel;
import lombok.Data;

import java.util.List;

/**
 * 流程工单统计信息
 * @author liyunlong
 * @version 1.0
 * @ClassName ProcessStatisticsVo
 * @date 2024/5/18 9:08
 */
@Data
public class ProcessStatisticsVo {
    /**
     * 名称
     * */
    @Excel(name = "部门/人员")
    private String name;

    /**
     * 类别
     * */
    @Excel(name = "类型")
    private String type;

    /**
     * 编码
     * 状态码等信息
     * */
    private String code;

    @Excel(name = "年")
    private Integer year;

    @Excel(name = "月份")
    private Integer month;

    @Excel(name = "数量")
    private Long value;

    private String deptName;

    /**
     * 统计数据数组
     * */
    private Long[] data;

    private List<ProcessStatisticsVo> children;
}
