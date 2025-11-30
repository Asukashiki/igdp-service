package com.inspur.seed.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 繁殖跟踪信息VO
 *
 * @author igdp
 * @date 2025-11-29
 */
@Data
public class BreedingTrackingVO {

    /**
     * 主键ID
     */
    private String id;

    /**
     * 跟踪编号
     */
    private String trackingId;

    /**
     * 关联繁殖批次编号
     */
    private String batchId;

    /**
     * 作物类型
     */
    private String cropType;

    /**
     * 作物类型名称
     */
    private String cropTypeName;

    /**
     * 阶段名称
     */
    private String stageName;

    /**
     * 阶段名称文本
     */
    private String stageNameText;

    /**
     * 跟踪结果
     */
    private String trackingResult;

    /**
     * 跟踪结果名称
     */
    private String trackingResultName;

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
     * 检测记录数量
     */
    private Integer testCount;
}
