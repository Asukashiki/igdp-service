package com.inspur.seed.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 繁殖跟踪信息新增DTO
 *
 * @author igdp
 * @date 2025-11-29
 */
@Data
public class BreedingTrackingAddDTO {

    /**
     * 关联繁殖批次编号
     */
    @NotBlank(message = "繁殖批次编号不能为空")
    private String batchId;

    /**
     * 作物类型（枚举值）
     */
    private String cropType;

    /**
     * 阶段名称（01/02/03/04）
     */
    private String stageName;

    /**
     * 跟踪结果（01/02/03）
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
     * 预期产量（kg）
     */
    private BigDecimal expectedYield;

    /**
     * 实际产量（kg）
     */
    private BigDecimal actualYield;

    /**
     * 田间检查评分（0-100）
     */
    private BigDecimal fieldInspectionScore;

    /**
     * 病害观察记录
     */
    private String diseaseObservation;

    /**
     * 阶段开始日期
     */
    @NotNull(message = "阶段开始日期不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date startDate;

    /**
     * 阶段完成日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date completeDate;

    /**
     * 操作机构ID
     */
    private String orgId;

    /**
     * 操作机构名称
     */
    private String orgName;

    /**
     * 备注
     */
    private String remark;
}
