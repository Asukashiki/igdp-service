package com.inspur.seed.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 环境与土壤属性数据采集VO
 *
 * @author igdp
 * @date 2025-11-26
 */
@Data
public class EnvironmentSoilDataVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 数据ID
     */
    private String dataId;

    /**
     * 土壤pH值
     */
    private BigDecimal soilPh;

    /**
     * 土壤电导率
     */
    private BigDecimal soilEc;

    /**
     * 土壤氮含量(百分比)
     */
    private BigDecimal soilNitrogenPercent;

    /**
     * 土壤磷含量(PPM)
     */
    private BigDecimal soilPhosphorusPpm;

    /**
     * 土壤钾含量(PPM)
     */
    private BigDecimal soilPotassiumPpm;

    /**
     * 前茬作物
     */
    private String previousCrop;

    /**
     * 水源
     */
    private String waterSource;

    /**
     * 地貌
     */
    private String topography;

    /**
     * 坡度(百分比)
     */
    private BigDecimal slopePercent;

    /**
     * 土壤湿度(百分比)
     */
    private BigDecimal soilMoisturePercent;

    /**
     * 土壤温度(摄氏度)
     */
    private BigDecimal soilTemperatureC;

    /**
     * 降雨量(MM)
     */
    private BigDecimal rainfallMm;

    /**
     * 空气温度(摄氏度)
     */
    private BigDecimal airTemperatureC;

    /**
     * 湿度(百分比)
     */
    private BigDecimal humidityPercent;

    /**
     * 风速(M/S)
     */
    private BigDecimal windSpeedMs;

    /**
     * 太阳辐射(W/m2)
     */
    private BigDecimal solarRadiationWm2;

    /**
     * 时间戳
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date timestamp;

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
