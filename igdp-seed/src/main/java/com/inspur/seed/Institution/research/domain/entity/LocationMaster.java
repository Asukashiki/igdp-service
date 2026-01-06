package com.inspur.seed.Institution.research.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 研究中心实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("location_master")
public class LocationMaster {

    /**
     * 位置ID
     */
    @TableId("location_id")
    private String locationId;

    /**
     * 位置名称
     */
    @TableField("location_name")
    private String locationName;

    /**
     * 地区
     */
    @TableField("region")
    private String region;

    /**
     * 区域
     */
    @TableField("zone")
    private String zone;

    /**
     * 沃雷达（行政区域）
     */
    @TableField("woneda")
    private String woneda;

    /**
     * 纬度
     */
    @TableField("latitude")
    private BigDecimal latitude;

    /**
     * 经度
     */
    @TableField("longitude")
    private BigDecimal longitude;

    /**
     * 创建者
     */
    @TableField("create_by")
    private String createBy;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 更新者
     */
    @TableField("update_by")
    private String updateBy;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private Date updateTime;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;
}
