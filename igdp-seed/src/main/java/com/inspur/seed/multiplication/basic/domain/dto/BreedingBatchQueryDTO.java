package com.inspur.seed.multiplication.basic.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 繁殖批次信息查询DTO
 *
 * @author igdp
 * @date 2025-11-29
 */
@Data
public class BreedingBatchQueryDTO {

    /**
     * 当前页码
     */
    private Integer pageNum = 1;

    /**
     * 每页条数
     */
    private Integer pageSize = 10;

    /**
     * 繁殖批次编号（模糊查询）
     */
    private String batchId;

    /**
     * 作物类型
     */
    private String cropType;

    /**
     * 品种名称（模糊查询）
     */
    private String varietyName;

    /**
     * 繁殖级别
     */
    private String breedingLevel;

    /**
     * 批次状态
     */
    private String batchStatus;

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
