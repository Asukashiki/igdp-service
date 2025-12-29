package com.inspur.seed.breeding.breedingBatch.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.inspur.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

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

    /** 亲本来源 */
    @TableField("germplasm_source")
    private String germplasmSource;

    /** 亲本种子来源 */
    @TableField("parental_seed_source")
    private String parentalSeedSource;

    /** 育种目标 */
    @TableField("objective")
    private String objective;

    /** 开展年份 */
    @TableField("year")
    private Integer year;

    /**
     * 批次状态
     * 使用BreedingBatchStatusEnum枚举定义的状态码：
     * S0: onging
     * S1: finished
     */
    @TableField("status")
    private String status;

    /**
     * 批次状态
     * 使用BreedingBatchStatusEnum枚举定义的状态码：
     * S0: 草稿
     * S1: 待审核
     * S2: 审核通过
     * S3: 审核驳回
     * S4: 进行中
     * S5: 暂停
     * S6: 终止
     * S7: 完成
     * S8: 已归档
     * S9: 已作废
     * S10: 异常
     */
    @TableField("workflow_status")
    private String workflowStatus;

    /** 备注 - 映射到数据库remarks字段 */
    @TableField("remarks")
    private String remarks;

    /** 逻辑删除标识(0=未删除,1=已删除) */
    @TableLogic
    @TableField("is_deleted")
    private Integer isDeleted;
}
