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
import java.math.BigDecimal;
import java.util.Date;

/**
 * 繁殖跟踪信息表实体类
 *
 * @author igdp
 * @date 2025-11-29
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("breeding_tracking_info")
public class BreedingTrackingInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 跟踪编号（系统自动生成）
     */
    @TableField("tracking_id")
    private String trackingId;

    /**
     * 关联繁殖批次编号
     */
    @TableField("batch_id")
    private String batchId;

    /**
     * 作物类型（枚举值）
     */
    @TableField("crop_type")
    private String cropType;

    /**
     * 阶段名称（01-亲本系准备 02-原原种繁殖 03-原种繁殖 04-良种生产）
     */
    @TableField("stage_name")
    private String stageName;

    /**
     * 跟踪结果（01-正常 02-异常 03-待观察）
     */
    @TableField("tracking_result")
    private String trackingResult;

    /**
     * 位置描述
     */
    @TableField("location")
    private String location;

    /**
     * GPS经度
     */
    @TableField("gps_longitude")
    private String gpsLongitude;

    /**
     * GPS纬度
     */
    @TableField("gps_latitude")
    private String gpsLatitude;

    /**
     * 预期产量（kg）
     */
    @TableField("expected_yield")
    private BigDecimal expectedYield;

    /**
     * 实际产量（kg）
     */
    @TableField("actual_yield")
    private BigDecimal actualYield;

    /**
     * 田间检查评分（0-100）
     */
    @TableField("field_inspection_score")
    private BigDecimal fieldInspectionScore;

    /**
     * 病害观察记录
     */
    @TableField("disease_observation")
    private String diseaseObservation;

    /**
     * 阶段开始日期
     */
    @TableField("start_date")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date startDate;

    /**
     * 阶段完成日期
     */
    @TableField("complete_date")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date completeDate;

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
