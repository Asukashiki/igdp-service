package com.inspur.seed.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.inspur.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 繁殖种子品种信息实体类
 *
 * @author igdp
 * @date 2025-11-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("seed_breeding_seed_variety_info")
public class BreedingSeedVarietyInfo extends BaseEntity {

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
     * 品种名称
     */
    private String varietyName;

    /**
     * 品种代码
     */
    private String varietyCode;

    /**
     * 作物类型
     */
    private String cropType;

    /**
     * 物种
     */
    private String species;

    /**
     * 属
     */
    private String genus;

    /**
     * 科
     */
    private String family;

    /**
     * 培育方法
     */
    private String breedingMethod;

    /**
     * 系谱
     */
    private String pedigree;

    /**
     * 培育年份
     */
    private Integer breedingYear;

    /**
     * 删除标志(0正常 2删除)
     */
    private String delFlag;
}
