package com.inspur.seed.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.inspur.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 农民与地块属性数据采集实体类
 *
 * @author igdp
 * @date 2025-11-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("seed_farmer_plot_data")
public class FarmerPlotData extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 数据ID(主键)
     */
    @TableId(type = IdType.ASSIGN_UUID)
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
     * 删除标志(0正常 2删除)
     */
    private String delFlag;
}
