package com.inspur.farmland.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.inspur.common.core.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 农民信息实体类
 *
 * @author inspur
 */
@TableName("t_farmer_info")
@Setter
@Getter
public class FarmerInfo extends BaseEntity {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 农民编码（业务主键）
     */
    private String farmerId;

    /**
     * 农民姓名
     */
    private String farmerName;

    /**
     * 身份证号/ID
     */
    private String idCard;

    /**
     * 性别：M-男 F-女
     */
    private String gender;

    /**
     * 出生日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 青年类别：1-是 0-否
     */
    private String youthCategory;

    /**
     * 所属Union ID
     */
    private String unionId;

    /**
     * 所属Union名称
     */
    private String unionName;

    /**
     * 所属Cooperative ID
     */
    private String cooperativeId;

    /**
     * 所属Cooperative名称
     */
    private String cooperativeName;

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
     * 总土地面积（公顷）
     */
    private BigDecimal totalLandArea;

    /**
     * 地块数量
     */
    private Integer landCount;

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
