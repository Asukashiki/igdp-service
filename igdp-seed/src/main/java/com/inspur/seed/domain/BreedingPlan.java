package com.inspur.seed.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.inspur.common.core.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * 育种计划表实体类
 *
 * @author system
 */
@TableName("breeding_plan")
@Setter
@Getter
public class BreedingPlan extends BaseEntity {

    /**
     * 育种计划唯一标识
     */
    @TableId
    private String planId;

    /**
     * 关联企业唯一标识
     */
    private String enterpriseId;

    /**
     * 计划名称
     */
    private String planName;

    /**
     * 育种年度
     */
    private Integer breedingYear;

    /**
     * 育种批次ID
     */
    private String batchId;

    /**
     * 种植基地
     */
    private String plantingBase;

    /**
     * 作物类型
     */
    private String cropType;

    /**
     * 品种名称
     */
    private String varietyName;

    /**
     * 繁殖级别
     */
    private String propagationLevel;

    /**
     * 亲本种子来源
     */
    private String parentSeedSource;

    /**
     * 负责人
     */
    private String personInCharge;

    /**
     * 计划起始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    /**
     * 计划结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    /**
     * 育种目标
     */
    private String breedingGoal;

    /**
     * 备注
     */
    private String remarks;
}
