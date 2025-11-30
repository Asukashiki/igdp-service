package com.inspur.seed.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 物联网传感器VO
 *
 * @author igdp
 * @date 2025-11-30
 */
@Data
public class IotSensorVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private String dataId;

    /**
     * 传感器编号
     */
    private String iotId;

    /**
     * 传感器名称
     */
    private String iotName;

    /**
     * 传感器类型
     */
    private String iotType;

    /**
     * 传感器类型名称
     */
    private String iotTypeName;

    /**
     * 制造商
     */
    private String manufacturer;

    /**
     * 校准日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date calibrationDate;

    /**
     * 固件版本
     */
    private String firmwareVersion;

    /**
     * 电池状态
     */
    private String batteryStatus;

    /**
     * 机构ID
     */
    private String orgId;

    /**
     * 机构名称
     */
    private String orgName;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
}
