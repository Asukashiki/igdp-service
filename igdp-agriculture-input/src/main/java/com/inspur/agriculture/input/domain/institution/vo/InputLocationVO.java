package com.inspur.agriculture.input.domain.institution.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 位置信息VO
 *
 * @author system
 */
@Data
public class InputLocationVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 位置ID
     */
    private String id;

    /**
     * 大区
     */
    private String region;

    /**
     * Zone
     */
    private String zone;

    /**
     * Woreda
     */
    private String woreda;

    /**
     * Kebele
     */
    private String kebele;

    /**
     * 详细地址
     */
    private String fullAddress;

    /**
     * GPS纬度
     */
    private BigDecimal gpsLatitude;

    /**
     * GPS经度
     */
    private BigDecimal gpsLongitude;

    /**
     * 经营范围
     */
    private String businessScope;

    /**
     * 年生产能力
     */
    private BigDecimal annualProductionCapacity;
}
