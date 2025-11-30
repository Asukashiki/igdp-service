package com.inspur.seed.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 繁殖检测信息查询DTO
 *
 * @author igdp
 * @date 2025-11-29
 */
@Data
public class BreedingTestQueryDTO {

    /**
     * 当前页码
     */
    private Integer pageNum = 1;

    /**
     * 每页条数
     */
    private Integer pageSize = 10;

    /**
     * 检测编号（模糊查询）
     */
    private String testId;

    /**
     * 关联跟踪编号
     */
    private String trackingId;

    /**
     * 关联繁殖批次编号
     */
    private String batchId;

    /**
     * 作物种类
     */
    private String cropType;

    /**
     * 检测结论
     */
    private String testResult;

    /**
     * 检测日期起
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date testDateBegin;

    /**
     * 检测日期止
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date testDateEnd;
}
