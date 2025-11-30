package com.inspur.seed.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 繁殖跟踪信息查询DTO
 *
 * @author igdp
 * @date 2025-11-29
 */
@Data
public class BreedingTrackingQueryDTO {

    /**
     * 当前页码
     */
    private Integer pageNum = 1;

    /**
     * 每页条数
     */
    private Integer pageSize = 10;

    /**
     * 跟踪编号（模糊查询）
     */
    private String trackingId;

    /**
     * 繁殖批次编号（精确/模糊）
     */
    private String batchId;

    /**
     * 作物类型
     */
    private String cropType;

    /**
     * 阶段名称
     */
    private String stageName;

    /**
     * 跟踪结果
     */
    private String trackingResult;

    /**
     * 开始日期起
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date startDateBegin;

    /**
     * 开始日期止
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date startDateEnd;
}
