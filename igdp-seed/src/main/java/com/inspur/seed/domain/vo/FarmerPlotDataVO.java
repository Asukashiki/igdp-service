package com.inspur.seed.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 农民与地块属性数据采集VO
 *
 * @author igdp
 * @date 2025-11-26
 */
@Data
public class FarmerPlotDataVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 数据ID
     */
    private String dataId;

    /**
     * 农民姓名
     */
    private String farmerName;

    /**
     * 性别
     */
    private String gender;

    /**
     * 青年类别
     */
    private String youthCategory;

    /**
     * 合作社成员资格
     */
    private String cooperativeMembership;

    /**
     * 地块面积(平方米)
     */
    private BigDecimal plotSizeM2;

    /**
     * 家庭ID
     */
    private String householdId;

    /**
     * 联系电话
     */
    private String contactPhone;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 更新人
     */
    private String updateBy;
}
