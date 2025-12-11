package com.inspur.agriculture.input.domain.institution.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 位置信息DTO
 *
 * @author system
 */
@Data
public class InputLocationDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 位置信息ID(修改时传入)
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
    @NotBlank(message = "Woreda is required")
    private String woreda;

    /**
     * Kebele
     */
    @NotBlank(message = "Kebele is required")
    private String kebele;

    /**
     * 详细地址
     */
    @NotBlank(message = "Full address is required")
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
