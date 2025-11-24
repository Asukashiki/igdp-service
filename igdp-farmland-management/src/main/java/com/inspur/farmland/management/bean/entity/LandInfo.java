package com.inspur.farmland.management.bean.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 土地信息实体类
 * 
 * @author inspur
 */
@Data
@TableName("land_info")
public class LandInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 土地ID */
    @TableId(value = "LAND_ID", type = IdType.AUTO)
    private Long landId;

    /** 地块名称 */
    @TableField("LAND_NAME")
    private String landName;

    /** 土地权属 */
    @TableField("OWNER_TYPE")
    private String ownerType;

    /** 行政区划代码 */
    @TableField("AD_CODE")
    private String adCode;

    /** 详细地址 */
    @TableField("DETAIL_ADDRESS")
    private String detailAddress;

    /** 地块面积 */
    @TableField("AREA_SIZE")
    private BigDecimal areaSize;

    /** 地块类型 */
    @TableField("LAND_TYPE")
    private String landType;

    /** 当前状态 */
    @TableField("CURRENT_STATUS")
    private String currentStatus;

    /** 纬度 */
    @TableField("LATITUDE")
    private BigDecimal latitude;

    /** 经度 */
    @TableField("LONGITUDE")
    private BigDecimal longitude;

    /** 关联农民用户ID */
    @TableField("FARMER_USER_ID")
    private Long farmerUserId;

    /** 创建人ID */
    @TableField("CREATE_BY")
    private Long createBy;

    /** 创建时间 */
    @TableField("CREATE_TIME")
    private Date createTime;

    /** 最后更新时间 */
    @TableField("UPDATE_TIME")
    private Date updateTime;

    /** 备注 */
    @TableField("REMARK")
    private String remark;
}