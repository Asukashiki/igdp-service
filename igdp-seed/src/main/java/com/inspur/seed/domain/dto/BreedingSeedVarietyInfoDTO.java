package com.inspur.seed.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 繁殖种子品种信息DTO
 *
 * @author igdp
 * @date 2025-11-29
 */
@Data
public class BreedingSeedVarietyInfoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 数据ID
     */
    private String dataId;

    /**
     * 繁育批次ID(必填)
     */
    private String breedingBatchId;

    /**
     * 认证ID(必填)
     */
    private String authId;

    /**
     * 品种名称(必填)
     */
    private String varietyName;

    /**
     * 品种代码(必填)
     */
    private String varietyCode;

    /**
     * 作物类型(必填)
     */
    private String cropType;

    /**
     * 物种(必填)
     */
    private String species;

    /**
     * 属(必填)
     */
    private String genus;

    /**
     * 科(必填)
     */
    private String family;

    /**
     * 培育方法(必填)
     */
    private String breedingMethod;

    /**
     * 系谱(必填)
     */
    private String pedigree;

    /**
     * 培育年份(必填)
     */
    private Integer breedingYear;
}
