package com.inspur.seed.multiplication.basic.domain.entity;

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
 * 繁殖批次信息表实体类
 *
 * @author igdp
 * @date 2025-11-29
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("breeding_batch_info")
public class BreedingBatchInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 繁殖批次编号（系统自动生成）
     */
    @TableField("batch_id")
    private String batchId;

    /**
     * 关联接收记录ID
     */
    @TableField("received_id")
    private String receivedId;

    /**
     * 作物类型（枚举值）
     */
    @TableField("crop_type")
    private String cropType;

    /**
     * 品种编码（自动生成，格式：作物类型_品种名称）
     */
    @TableField("variety_code")
    private String varietyCode;

    /**
     * 品种名称
     */
    @TableField("variety_name")
    private String varietyName;

    /**
     * 繁殖级别（01-原原种繁殖 02-原种繁殖 03-良种生产）
     */
    @TableField("breeding_level")
    private String breedingLevel;

    /**
     * 亲本种子来源
     */
    @TableField("parent_seed_source")
    private String parentSeedSource;

    /**
     * 开始日期
     */
    @TableField("start_date")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date startDate;

    /**
     * 结束日期
     */
    @TableField("end_date")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date endDate;

    /**
     * 批次状态（01-进行中 02-已完成 03-已终止）
     */
    @TableField("batch_status")
    private String batchStatus;

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


    /**
     * 待扩繁数量（kg）
     */
    @TableField("to_multiply_quantity")
    private BigDecimal toMultiplyQuantity;
}
