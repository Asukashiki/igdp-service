package com.inspur.seed.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 繁殖种子试验与性能信息VO
 *
 * @author igdp
 * @date 2025-11-29
 */
@Data
public class BreedingSeedTrialPerformanceVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 数据ID
     */
    private String dataId;

    /**
     * 繁育批次ID
     */
    private String breedingBatchId;

    /**
     * 认证ID
     */
    private String authId;

    /**
     * 试验地点
     */
    private String trialLocation;

    /**
     * 试验年份
     */
    private Integer trialYear;

    /**
     * 平均产量
     */
    private BigDecimal averageYield;

    /**
     * 稳定性评分
     */
    private BigDecimal stabilityScore;

    /**
     * 试验报告
     */
    private String trialReport;

    /**
     * 照片
     */
    private String photo;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 更新人
     */
    private String updateBy;
}
