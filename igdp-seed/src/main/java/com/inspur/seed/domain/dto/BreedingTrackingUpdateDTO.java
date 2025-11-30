package com.inspur.seed.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 繁殖跟踪信息修改DTO
 *
 * @author igdp
 * @date 2025-11-29
 */
@Data
public class BreedingTrackingUpdateDTO {

    /**
     * 主键ID
     */
    @NotBlank(message = "主键ID不能为空")
    private String id;

    /**
     * 作物类型
     */
    private String cropType;

    /**
     * 阶段名称
     */
    private String stageName;

    /**
     * 跟踪结果
     */
    private String trackingResult;

    /**
     * 位置描述
     */
    private String location;

    /**
     * GPS经度
     */
    private String gpsLongitude;

    /**
     * GPS纬度
     */
    private String gpsLatitude;

    /**
     * 预期产量
     */
    private BigDecimal expectedYield;

    /**
     * 实际产量
     */
    private BigDecimal actualYield;

    /**
     * 田间检查评分
     */
    @DecimalMin(value = "0", message = "田间检查评分最小值为0")
    @DecimalMax(value = "100", message = "田间检查评分最大值为100")
    private BigDecimal fieldInspectionScore;

    /**
     * 病害观察记录
     */
    private String diseaseObservation;

    /**
     * 阶段开始日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date startDate;

    /**
     * 阶段完成日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date completeDate;

    /**
     * 备注
     */
    private String remark;
}
