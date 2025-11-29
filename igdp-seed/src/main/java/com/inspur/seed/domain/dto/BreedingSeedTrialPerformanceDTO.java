package com.inspur.seed.domain.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 繁殖种子试验与性能信息DTO
 *
 * @author igdp
 * @date 2025-11-29
 */
@Data
public class BreedingSeedTrialPerformanceDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 数据ID
     */
    private String dataId;

    /**
     * 繁育批次ID(必填)
     */
    private String breedingBatchId;

    /**
     * 认证ID(必填)
     */
    private String authId;

    /**
     * 试验地点(必填)
     */
    private String trialLocation;

    /**
     * 试验年份(必填)
     */
    private Integer trialYear;

    /**
     * 平均产量(必填)
     */
    private BigDecimal averageYield;

    /**
     * 稳定性评分(必填)
     */
    private BigDecimal stabilityScore;

    /**
     * 试验报告(文件路径,必填)
     */
    private String trialReport;

    /**
     * 照片(文件路径,必填)
     */
    private String photo;
}
