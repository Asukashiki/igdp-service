package com.inspur.seed.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 机构位置运营信息实体类
 *
 * @author system
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("org_enterprise_location")
public class OrgEnterpriseLocation implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键UUID
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 机构ID(关联org_enterprise_info)
     */
    private String enterpriseId;

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

    /**
     * 创建人ID
     */
    private String createdBy;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTime;

    /**
     * 更新人ID
     */
    private String updatedBy;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedTime;

    /**
     * 是否删除(0:否 1:是)
     */
    @TableLogic
    private Integer isDeleted;
}
