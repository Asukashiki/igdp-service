package com.inspur.seed.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.inspur.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 繁殖种子试验与性能信息实体类
 *
 * @author igdp
 * @date 2025-11-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("seed_breeding_seed_trial_performance")
public class BreedingSeedTrialPerformance extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 数据ID(主键)
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String dataId;

    /**
     * 繁育批次ID
     */
    private String breedingBatchId;

    /**
     * 认证ID
     */
    private String authId;

    /**
     * 试验地点
     */
    private String trialLocation;

    /**
     * 试验年份
     */
    private Integer trialYear;

    /**
     * 平均产量
     */
    private BigDecimal averageYield;

    /**
     * 稳定性评分
     */
    private BigDecimal stabilityScore;

    /**
     * 试验报告(文件路径)
     */
    @TableField(insertStrategy = FieldStrategy.ALWAYS, updateStrategy = FieldStrategy.ALWAYS)
    private String trialReport;

    /**
     * 照片(文件路径)
     */
    @TableField(insertStrategy = FieldStrategy.ALWAYS, updateStrategy = FieldStrategy.ALWAYS)
    private String photo;

    /**
     * 删除标志(0正常 2删除)
     */
    private String delFlag;
}
