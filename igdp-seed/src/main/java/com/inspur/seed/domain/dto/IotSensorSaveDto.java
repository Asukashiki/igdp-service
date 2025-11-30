package com.inspur.seed.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 * 物联网传感器新增/修改DTO
 *
 * @author igdp
 * @date 2025-11-30
 */
@Data
public class IotSensorSaveDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID（修改时必填）
     */
    private String dataId;

    /**
     * 传感器编号（必填，最大32位）
     */
    @NotBlank(message = "传感器编号不能为空")
    @Size(max = 32, message = "传感器编号长度不能超过32位")
    private String iotId;

    /**
     * 传感器名称（必填，最大100位）
     */
    @NotBlank(message = "传感器名称不能为空")
    @Size(max = 100, message = "传感器名称长度不能超过100位")
    private String iotName;

    /**
     * 传感器类型（必填，2位字符）
     */
    @NotBlank(message = "传感器类型不能为空")
    @Size(min = 2, max = 2, message = "传感器类型必须为2位字符")
    private String iotType;

    /**
     * 制造商（必填，最大32位）
     */
    @NotBlank(message = "制造商不能为空")
    @Size(max = 32, message = "制造商长度不能超过32位")
    private String manufacturer;

    /**
     * 校准日期（必填）
     */
    @NotNull(message = "校准日期不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date calibrationDate;

    /**
     * 固件版本（必填，最大32位）
     */
    @NotBlank(message = "固件版本不能为空")
    @Size(max = 32, message = "固件版本长度不能超过32位")
    private String firmwareVersion;

    /**
     * 电池状态（最大64位）
     */
    @Size(max = 64, message = "电池状态长度不能超过64位")
    private String batteryStatus;

    /**
     * 备注（最大500位）
     */
    @Size(max = 500, message = "备注长度不能超过500位")
    private String remark;
}
