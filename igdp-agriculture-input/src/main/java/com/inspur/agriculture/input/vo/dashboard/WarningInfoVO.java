package com.inspur.agriculture.input.vo.dashboard;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 预警信息VO
 *
 * @author igdp
 */
@Data
public class WarningInfoVO {

    /** 预警ID */
    private String warningId;

    /** 预警类型(1-临期预警/2-过期预警/3-库存不足/4-超容量预警) */
    private String warningType;

    /** 预警类型描述 */
    private String warningTypeDesc;

    /** 预警级别(1-低/2-中/3-高) */
    private Integer warningLevel;

    /** 预警级别描述 */
    private String warningLevelDesc;

    /** 关联对象类型(1-投入品/2-仓库) */
    private String objectType;

    /** 关联对象ID */
    private String objectId;

    /** 关联对象名称 */
    private String objectName;

    /** 预警内容 */
    private String warningContent;

    /** 当前值 */
    private String currentValue;

    /** 阈值 */
    private String threshold;

    /** 预警时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date warningTime;

    /** 处理状态(0-未处理/1-已处理/2-已忽略) */
    private String status;

    /** 仓库名称 */
    private String warehouseName;

    /** 批次号 */
    private String batchNo;
}
