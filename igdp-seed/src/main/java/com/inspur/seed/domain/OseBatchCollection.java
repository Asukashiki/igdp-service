package com.inspur.seed.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.inspur.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * OSE繁殖批次信息数据采集 - 数据库实体
 * OSE Batch Information Data Collection Entity
 *
 * @author system
 * @date 2026-01-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ose_batch_collection")
public class OseBatchCollection extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 批次ID（自动生成） */
    @TableField("batch_id")
    private String batchId;

    /** 繁殖批次ID（关联breeding_batch表） */
    @TableField("breeding_batch_id")
    private String breedingBatchId;

    /** 亲本种子来源 */
    @TableField("parental_seed_source")
    private String parentalSeedSource;

    /** 品种名称 */
    @TableField("variety_name")
    private String varietyName;

    /** 作物类型 */
    @TableField("crop_type")
    private String cropType;

    /** 繁殖级别 */
    @TableField("breeding_level")
    private String breedingLevel;

    /** 繁殖数量(kg) */
    @TableField("to_multiply_quantity")
    private BigDecimal toMultiplyQuantity;

    /** 采集日期 */
    @TableField("collection_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date collectionDate;

    /** 操作员 */
    @TableField("operator")
    private String operator;

    /** 备注 */
    @TableField("remark")
    private String remark;

    /** 删除标志（0正常 2删除） */
    @TableField("del_flag")
    private String delFlag;
}
