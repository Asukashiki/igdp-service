package com.inspur.seed.Institution.research.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 研究中心值对象
 */
@Data
public class LocationMasterVO {

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

    /**
     * 纬度
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal latitude;

    /**
     * 经度
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal longitude;

    /**
     * 创建者
     */
    private String createBy;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新者
     */
    private String updateBy;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;
}
