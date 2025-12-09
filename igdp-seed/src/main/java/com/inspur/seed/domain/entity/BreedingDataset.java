package com.inspur.seed.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 育种数据集实体类
 *
 * @author system
 * @date 2025-01-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("breeding_dataset")
public class BreedingDataset implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 数据集编号(审核通过后生成)
     */
    @TableField("dataset_code")
    private String datasetCode;

    /**
     * 试验ID(外键关联试验基础信息表)
     */
    @TableField("trial_id")
    private String trialId;

    /**
     * 育种批次ID
     */
    @TableField("batch_id")
    private String batchId;

    /**
     * 育种批次名称(冗余字段)
     */
    @TableField("batch_name")
    private String batchName;

    /**
     * 作物类型(冗余字段)
     */
    @TableField("crop_type")
    private String cropType;

    /**
     * 品种名称(冗余字段)
     */
    @TableField("variety_name")
    private String varietyName;

    /**
     * 版本号（同一试验下每次提交自动递增）
     */
    @TableField("version_no")
    private Integer versionNo;

    /**
     * 编制人(外键关联用户表USER_MASTER)
     */
    @TableField("compiled_by")
    private String compiledBy;

    /**
     * 编制时间
     */
    @TableField("compiled_at")
    private LocalDateTime compiledAt;

    /**
     * 记录数量(手动输入)
     */
    @TableField("record_count")
    private Integer recordCount;

    /**
     * 试验记录数
     */
    @TableField("trial_count")
    private Integer trialCount;

    /**
     * 田间数据记录数
     */
    @TableField("field_data_count")
    private Integer fieldDataCount;

    /**
     * 环境数据记录数
     */
    @TableField("env_data_count")
    private Integer envDataCount;

    /**
     * 实验室检测记录数
     */
    @TableField("lab_test_count")
    private Integer labTestCount;

    /**
     * 产量数据记录数
     */
    @TableField("yield_data_count")
    private Integer yieldDataCount;

    /**
     * 数据集状态: draft草稿/submitted已提交/reviewing审核中/approved已通过/rejected已驳回
     */
    @TableField("dataset_status")
    private String datasetStatus;

    /**
     * 提交时间
     */
    @TableField("submit_time")
    private LocalDateTime submitTime;

    /**
     * 提交人ID
     */
    @TableField("submit_by")
    private String submitBy;

    /**
     * 提交人姓名
     */
    @TableField("submit_by_name")
    private String submitByName;

    /**
     * 提交机构代码
     */
    @TableField("submit_org_code")
    private String submitOrgCode;

    /**
     * 提交机构名称
     */
    @TableField("submit_org_name")
    private String submitOrgName;

    /**
     * 状态:1有效0无效
     */
    @TableField("status")
    private String status;

    /**
     * 创建人ID
     */
    @TableField("created_by")
    private String createdBy;

    /**
     * 创建人姓名
     */
    @TableField("created_by_name")
    private String createdByName;

    /**
     * 创建机构代码
     */
    @TableField("created_org_code")
    private String createdOrgCode;

    /**
     * 创建机构名称
     */
    @TableField("created_org_name")
    private String createdOrgName;

    /**
     * 创建时间
     */
    @TableField("created_time")
    private LocalDateTime createdTime;

    /**
     * 更新人ID
     */
    @TableField("updated_by")
    private String updatedBy;

    /**
     * 更新时间
     */
    @TableField("updated_time")
    private LocalDateTime updatedTime;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 删除标记:0未删除1已删除
     */
    @TableLogic
    @TableField("deleted")
    private String deleted;
}
