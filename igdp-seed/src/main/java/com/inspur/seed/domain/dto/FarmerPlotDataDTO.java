package com.inspur.seed.domain.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 农民与地块属性数据采集DTO
 *
 * @author igdp
 * @date 2025-11-26
 */
@Data
public class FarmerPlotDataDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 数据ID
     */
    private String dataId;

    /**
     * 农民姓名(必填)
     */
    private String farmerName;

    /**
     * 性别(必填)
     */
    private String gender;

    /**
     * 青年类别(必填)
     */
    private String youthCategory;

    /**
     * 合作社成员资格(必填)
     */
    private String cooperativeMembership;

    /**
     * 地块面积平方米(必填)
     */
    private BigDecimal plotSizeM2;

    /**
     * 家庭ID
     */
    private String householdId;

    /**
     * 联系电话(必填)
     */
    private String contactPhone;
}
