package com.inspur.farmland.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.inspur.common.core.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 农田信息实体类
 */
@TableName("t_farmland")
@Setter
@Getter
public class LandInfo extends BaseEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 状态(0草稿 1已批准 2已拒绝) */
    private String status;

    /** 农户ID */
    private String farmerId;

    /** 所属村ID */
    @TableField("kebele_id")
    private String kebeleId;

    /** 纬度 */
    @TableField("gps_lat")
    private BigDecimal gpsLat;

    /** 经度 */
    @TableField("gps_long")
    private BigDecimal gpsLong;

    /** GPS多边形坐标(JSON格式) */
    private String gpsPolygon;

    /** 面积(公顷) */
    @TableField("area_ta")
    private BigDecimal areaTa;

    /** 土壤类型代码 */
    private String soilCode;

    /** 灌溉类型代码 */
    private String irrigationCode;

    /** 坡度等级 */
    private String slopeClass;

    /** 土地用途 */
    private String landUseType;

    /** 批准人 */
    private Long approvedBy;

    /** 批准时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime approvedTime;

    /** 拒绝原因 */
    private String rejectionReason;

    /** 备注 */
    private String remark;

    /** 创建部门 */
    private Long createDept;

    /** 逻辑删除标记 */
    private Integer delFlag;

    /** 租户ID */
    private String tenantId;

    /** 审批意见 */
    private String approvedComment;

    @TableField(exist = false)
    private String kebeleCode;

    @TableField(exist = false)
    private String kebeleName;

    @TableField(exist = false)
    private String woredaCode;

    @TableField(exist = false)
    private String zoneCode;

    @TableField(exist = false)
    private String farmerName;

    @TableField(exist = false)
    private String landName;

    @TableField(exist = false)
    private String landNo;

    @TableField(exist = false)
    private String ownerType;

    @TableField(exist = false)
    private String ownerName;

    @TableField(exist = false)
    private String ownerIdCard;

    @TableField(exist = false)
    private String landType;

    @TableField(exist = false)
    private String landGraphic;

    @TableField(exist = false)
    private String areaUnit;

    @TableField(exist = false)
    private String regionCode;

    @TableField(exist = false)
    private String regionName;

    @TableField(exist = false)
    private String zoneName;

    @TableField(exist = false)
    private String woredaName;

    @TableField(exist = false)
    private String address;

    @TableField(exist = false)
    private String farmerIdCard;

    @TableField(exist = false)
    private String farmerPhone;

    @TableField(exist = false)
    private String currentStatus;

    @TableField(exist = false)
    private BigDecimal maxSeedAmount;

    @TableField(exist = false)
    private BigDecimal maxFertilizerAmount;

    @TableField(exist = false)
    private String daId;

    @TableField(exist = false)
    private String daName;

    @TableField(exist = false)
    private String createByName;

    @TableField(exist = false)
    private String createOrg;

    @TableField(exist = false)
    private String createOrgName;

    @TableField(exist = false)
    private String updateByName;

    public String getLandId() {
        return id == null ? null : String.valueOf(id);
    }

    public void setLandId(String landId) {
        if (landId == null || landId.trim().isEmpty()) {
            this.id = null;
            return;
        }
        this.id = Long.valueOf(landId);
    }

    public BigDecimal getLatitude() {
        return gpsLat;
    }

    public void setLatitude(BigDecimal latitude) {
        this.gpsLat = latitude;
    }

    public BigDecimal getLongitude() {
        return gpsLong;
    }

    public void setLongitude(BigDecimal longitude) {
        this.gpsLong = longitude;
    }

    public String getPlotBoundary() {
        return gpsPolygon;
    }

    public void setPlotBoundary(String plotBoundary) {
        this.gpsPolygon = plotBoundary;
    }

    public BigDecimal getAreaSize() {
        return areaTa;
    }

    public void setAreaSize(BigDecimal areaSize) {
        this.areaTa = areaSize;
    }
}
