package com.inspur.seed.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 研究中心数据传输对象
 */
@Data
public class LocationMasterDTO {
    
    /**
     * 位置ID
     */
    private String locationId;
    
    /**
     * 位置名称
     */
    @NotBlank(message = "位置名称不能为空")
    @Size(max = 100, message = "位置名称长度不能超过100个字符")
    private String locationName;
    
    /**
     * 地区
     */
    @Size(max = 100, message = "地区长度不能超过100个字符")
    private String region;
    
    /**
     * 区域
     */
    @Size(max = 100, message = "区域长度不能超过100个字符")
    private String zone;
    
    /**
     * 沃雷达（行政区域）
     */
    @Size(max = 100, message = "沃雷达长度不能超过100个字符")
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
    @Size(max = 500, message = "备注长度不能超过500个字符")
    private String remark;
}