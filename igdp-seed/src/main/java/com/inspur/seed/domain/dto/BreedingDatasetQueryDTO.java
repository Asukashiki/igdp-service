package com.inspur.seed.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 育种数据集查询DTO
 *
 * @author system
 * @date 2025-01-30
 */
@Data
public class BreedingDatasetQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 育种批次ID
     */
    private String batchId;

    /**
     * 育种批次名称
     */
    private String batchName;

    /**
     * 作物类型
     */
    private String cropType;

    /**
     * 品种名称
     */
    private String varietyName;

    /**
     * 数据集状态
     */
    private String datasetStatus;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 当前页码
     */
    private Integer pageNum;

    /**
     * 每页记录数
     */
    private Integer pageSize;
}
