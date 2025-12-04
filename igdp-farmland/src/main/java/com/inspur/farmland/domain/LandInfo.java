package com.inspur.farmland.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.inspur.common.core.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * 土地信息实体类
 *
 * @author inspur
 */
@TableName("t_land_info")
@Setter
@Getter
public class LandInfo extends BaseEntity {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 土地编码（业务主键）
     */
    private String landId;

    /**
     * 地块名称
     */
    private String landName;

    /**
     * 地块编号
     */
    private String landNo;

    /**
     * 土地权属类型：COLLECTIVE-集体所有 CONTRACT-承包经营权 PRIVATE-私有
     */
    private String ownerType;

    /**
     * 权属人/单位名称
     */
    private String ownerName;

    /**
     * 权属人身份证号
     */
    private String ownerIdCard;

    /**
     * 地块类型：PADDY-水田 DRY-旱地 GARDEN-园地 FOREST-林地 OTHER-其他
     */
    private String landType;

    /**
     * 地形：FLAT-平坦 GENTLE_SLOPE-缓坡 STEEP_SLOPE-陡坡
     */
    private String landGraphic;

    /**
     * 地块面积（公顷）
     */
    private BigDecimal areaSize;

    /**
     * 面积单位：HECTARE-公顷 MU-亩 SQM-平方米
     */
    private String areaUnit;

    /**
     * 纬度
     */
    private BigDecimal latitude;

    /**
     * 经度
     */
    private BigDecimal longitude;

    /**
     * 地块边界坐标（GeoJSON格式）
     */
    private String plotBoundary;

    /**
     * 州代码
     */
    private String regionCode;

    /**
     * 州名称
     */
    private String regionName;

    /**
     * 区代码
     */
    private String zoneCode;

    /**
     * 区名称
     */
    private String zoneName;

    /**
     * 镇代码
     */
    private String woredaCode;

    /**
     * 镇名称
     */
    private String woredaName;

    /**
     * 村代码
     */
    private String kebeleCode;

    /**
     * 村名称
     */
    private String kebeleName;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 关联农民ID
     */
    private String farmerId;

    /**
     * 关联农民姓名
     */
    private String farmerName;

    /**
     * 关联农民身份证号
     */
    private String farmerIdCard;

    /**
     * 关联农民电话
     */
    private String farmerPhone;

    /**
     * 当前状态：CULTIVATING-耕种中 IDLE-闲置 FALLOW-休耕
     */
    private String currentStatus;

    /**
     * 估算最大种子量（kg）
     */
    private BigDecimal maxSeedAmount;

    /**
     * 估算最大肥料量（kg）
     */
    private BigDecimal maxFertilizerAmount;

    /**
     * 负责DA编码
     */
    private String daId;

    /**
     * 负责DA姓名
     */
    private String daName;

    /**
     * 数据状态：1-正常 0-删除
     */
    private String status;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建人姓名
     */
    private String createByName;

    /**
     * 创建机构代码
     */
    private String createOrg;

    /**
     * 创建机构名称
     */
    private String createOrgName;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新人姓名
     */
    private String updateByName;
}
