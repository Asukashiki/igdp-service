package com.inspur.seed.Institution.research.domain.dto;

import lombok.Data;

/**
 * 研究中心查询数据传输对象
 */
@Data
public class LocationMasterQueryDTO {

    /**
     * 位置ID
     */
    private String locationId;

    /**
     * 位置名称
     */
    private String locationName;

    /**
     * 地区
     */
    private String region;

    /**
     * 区域
     */
    private String zone;

    /**
     * 沃雷达（行政区域）
     */
    private String woneda;
}
