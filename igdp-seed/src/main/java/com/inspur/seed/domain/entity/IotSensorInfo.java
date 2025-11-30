package com.inspur.seed.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 物联网传感器信息表实体类
 *
 * @author igdp
 * @date 2025-11-30
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("iot_sensor_info")
public class IotSensorInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String dataId;

    /**
     * 传感器编号
     */
    @TableField("iot_id")
    private String iotId;

    /**
     * 传感器名称
     */
    @TableField("iot_name")
    private String iotName;

    /**
     * 传感器类型（01-温度 02-湿度 03-光照 04-土壤 05-气体 99-其他）
     */
    @TableField("iot_type")
    private String iotType;

    /**
     * 制造商
     */
    @TableField("manufacturer")
    private String manufacturer;

    /**
     * 校准日期
     */
    @TableField("calibration_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date calibrationDate;

    /**
     * 固件版本
     */
    @TableField("firmware_version")
    private String firmwareVersion;

    /**
     * 电池状态
     */
    @TableField("battery_status")
    private String batteryStatus;

    /**
     * 操作机构ID
     */
    @TableField("org_id")
    private String orgId;

    /**
     * 操作机构名称
     */
    @TableField("org_name")
    private String orgName;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 创建人
     */
    @TableField("create_by")
    private String createBy;

    /**
     * 创建时间
     */
    @TableField("create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 更新人
     */
    @TableField("update_by")
    private String updateBy;

    /**
     * 更新时间
     */
    @TableField("update_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    /**
     * 删除标志（0-正常 2-删除）
     */
    @TableField("del_flag")
    @TableLogic(value = "0", delval = "2")
    private String delFlag;
}
