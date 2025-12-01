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

    /** 计划名称 */
    @TableField("batch_name")
    private String batchName;

    /** 育种批次ID */
    @TableField("batch_id")
    private String batchId;

    /** 批次时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @TableField("batch_time")
    private Date batchTime;

    /** 作物类型 */
    @TableField("crop_type")
    private String cropType;

    /** 作物类型名称（中文） */
    @TableField(exist = false)
    private String cropTypeName;

    /** 品种名称 */
    @TableField("variety_name")
    private String varietyName;

    /** 物种 */
    @TableField("species")
    private String species;

    /** 属 */
    @TableField("genus")
    private String genus;

    /** 科 */
    @TableField("family")
    private String family;

    /** 繁育方法 */
    @TableField("breeding_method")
    private String breedingMethod;

    /** 繁育方法名称（中文） */
    @TableField(exist = false)
    private String breedingMethodName;

    /** 血统 */
    @TableField("pedigree")
    private String pedigree;

    /** 繁育年份 */
    @TableField("year_of_development")
    private Integer yearOfDevelopment;

    /** 生产地 */
    @TableField("product_place")
    private String productPlace;

    /** 产量说明 */
    @TableField("yield")
    private String yield;

    /** 负责人 */
    @TableField("person_in_charge")
    private String personInCharge;

    /** 计划起始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @TableField("start_date")
    private Date startDate;

    /** 计划结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @TableField("end_date")
    private Date endDate;

    /** 备注 - 映射到数据库remarks字段 */
    @TableField("remarks")
    private String remarks;

    /** 逻辑删除标识(0=未删除,1=已删除) */
    @TableLogic
    @TableField("is_deleted")
    private Integer isDeleted;
}
