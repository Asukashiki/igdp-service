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

import java.util.Date;

/**
 * 育种批次信息实体类
 *
 * @author inspur
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("breeding_batch")
public class BreedingBatch extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 数据标识(主键) */
    @TableId(value = "data_id", type = IdType.ASSIGN_UUID)
    private String dataId;

    /** 育种批次ID */
    @TableField("batch_id")
    private String batchId;

    /** 作物类型 */
    @TableField("crop_type")
    private String cropType;

    /** 作物类型名称（中文） */
    @TableField(exist = false)
    private String cropTypeName;

    /** 品种编码 */
    @TableField("variety_code")
    private String varietyCode;

    /** 品种名称 */
    @TableField("variety_name")
    private String varietyName;

    /** 繁育方法 */
    @TableField("breeding_method")
    private String breedingMethod;

    /** 繁育方法名称（中文） */
    @TableField(exist = false)
    private String breedingMethodName;

    /** 育种目标（原计划名称） */
    @TableField("batch_name")
    private String batchName;

    /** 开展年份 */
    @TableField("year")
    private Integer year;

    /** 批次状态 (not_approved/approved/ongoing/done) */
    @TableField("status")
    private String status;

    /** 备注 - 映射到数据库remarks字段 */
    @TableField("remarks")
    private String remarks;

    /** 逻辑删除标识(0=未删除,1=已删除) */
    @TableLogic
    @TableField("is_deleted")
    private Integer isDeleted;
}
