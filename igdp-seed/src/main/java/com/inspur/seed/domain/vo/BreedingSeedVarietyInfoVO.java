package com.inspur.seed.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 繁殖种子品种信息VO
 *
 * @author igdp
 * @date 2025-11-29
 */
@Data
public class BreedingSeedVarietyInfoVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 数据ID
     */
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
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 更新人
     */
    private String updateBy;
}
