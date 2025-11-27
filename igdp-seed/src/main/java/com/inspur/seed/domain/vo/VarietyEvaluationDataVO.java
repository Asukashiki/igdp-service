package com.inspur.seed.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 品种评估数据采集VO
 *
 * @author igdp
 * @date 2025-11-26
 */
@Data
public class VarietyEvaluationDataVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 数据ID
     */
    private String dataId;

    /**
     * 地块ID
     */
    private String plotId;

    /**
     * 地块面积(平方米)
     */
    private BigDecimal plotAreaM2;

    /**
     * 籽粒重量(KG)
     */
    private BigDecimal grainWeightKg;

    /**
     * 产量(公担/公顷)
     */
    private BigDecimal yieldQtPerHa;

    /**
     * 含水量(百分比)
     */
    private BigDecimal moistureContent;

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
