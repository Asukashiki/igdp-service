package com.inspur.seed.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.inspur.common.core.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 育种跟踪记录表实体类
 *
 * @author system
 */
@TableName("breeding_tracking")
@Setter
@Getter
public class BreedingTracking extends BaseEntity {

    /**
     * 跟踪记录唯一标识
     */
    @TableId
    private String trackingId;

    /**
     * 育种批次ID
     */
    private String batchId;

    /**
     * 阶段名称
     */
    private String stageName;

    /**
     * 位置
     */
    private String location;

    /**
     * 坐标(经纬度)
     */
    private String coordinates;

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
     * 病害观察
     */
    private String diseaseObservation;

    /**
     * 阶段完成日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate stageCompletionDate;

    /**
     * 记录人
     */
    private String recorder;

    /**
     * 记录时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime recordTime;



    /**
     * 创建者
     */
    private String createBy;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    /**
     * 更新者
     */
    private String updateBy;
}
