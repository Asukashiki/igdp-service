package com.inspur.seed.domain.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 环境与土壤属性数据采集DTO
 *
 * @author igdp
 * @date 2025-11-26
 */
@Data
public class EnvironmentSoilDataDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 数据ID
     */
    private String dataId;

    /**
     * 土壤pH值(必填)
     */
    private BigDecimal soilPh;

    /**
     * 土壤电导率(必填)
     */
    private BigDecimal soilEc;

    /**
     * 土壤氮含量百分比(必填)
     */
    private BigDecimal soilNitrogenPercent;

    /**
     * 土壤磷含量PPM(必填)
     */
    private BigDecimal soilPhosphorusPpm;

    /**
     * 土壤钾含量PPM(必填)
     */
    private BigDecimal soilPotassiumPpm;

    /**
     * 前茬作物
     */
    private String previousCrop;

    /**
     * 水源(必填)
     */
    private String waterSource;

    /**
     * 地貌(必填)
     */
    private String topography;

    /**
     * 坡度百分比
     */
    private BigDecimal slopePercent;

    /**
     * 土壤湿度百分比(必填)
     */
    private BigDecimal soilMoisturePercent;

    /**
     * 土壤温度C(必填)
     */
    private BigDecimal soilTemperatureC;

    /**
     * 降雨量MM
     */
    private BigDecimal rainfallMm;

    /**
     * 空气温度C
     */
    private BigDecimal airTemperatureC;

    /**
     * 湿度百分比(必填)
     */
    private BigDecimal humidityPercent;

    /**
     * 风速MS(必填)
     */
    private BigDecimal windSpeedMs;

    /**
     * 太阳辐射Wm2(必填)
     */
    private BigDecimal solarRadiationWm2;

    /**
     * 时间戳
     */
    private Date timestamp;
}
