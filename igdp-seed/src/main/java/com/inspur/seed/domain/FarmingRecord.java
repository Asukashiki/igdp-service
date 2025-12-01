package com.inspur.seed.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.inspur.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 农事记录实体类
 *
 * @author inspur
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("farming_record")
public class FarmingRecord extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 农事记录ID(主键) */
    @TableId(value = "farming_id", type = IdType.ASSIGN_UUID)
    private String farmingId;

    /** 育种批次ID */
    @TableField("batch_id")
    private String batchId;

    /** 试验ID */
    @TableField("trial_id")
    private String trialId;

    /** 操作类型 */
    @TableField("operation_type")
    private String operationType;

    /** 操作时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @TableField("operation_time")
    private Date operationTime;

    /** 肥料类型(施肥操作必填) */
    @TableField("fertilizer_type")
    private String fertilizerType;

    /** 施肥量(kg/亩，施肥操作必填) */
    @TableField("fertilizer_amount")
    private BigDecimal fertilizerAmount;

    /** 灌溉方式(灌溉操作必填) */
    @TableField("irrigation_method")
    private String irrigationMethod;

    /** 农药类型(病虫害防治必填) */
    @TableField("pesticide_type")
    private String pesticideType;

    /** 农药用量(病虫害防治必填) */
    @TableField("pesticide_dosage")
    private String pesticideDosage;

    /** 操作描述 */
    @TableField("operation_desc")
    private String operationDesc;

    /** 逻辑删除标识(0=未删除,1=已删除) */
    @TableLogic
    @TableField("is_deleted")
    private Integer isDeleted;

    /** 查询起始时间（查询参数，非数据库字段） */
    @TableField(exist = false)
    private Date queryStartTime;

    /** 查询结束时间（查询参数，非数据库字段） */
    @TableField(exist = false)
    private Date queryEndTime;
}
