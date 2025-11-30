package com.inspur.seed.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 繁殖种子技术性状信息VO
 *
 * @author igdp
 * @date 2025-11-29
 */
@Data
public class BreedingSeedTechnicalTraitVO implements Serializable {

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
     * 最低产量潜力
     */
    private BigDecimal minYieldPotential;

    /**
     * 最高产量潜力
     */
    private BigDecimal maxYieldPotential;

    /**
     * 抗病性
     */
    private String diseaseResistance;

    /**
     * 抗逆性
     */
    private String stressResistance;

    /**
     * 成熟期
     */
    private Integer maturityPeriod;

    /**
     * 株高
     */
    private BigDecimal plantHeight;

    /**
     * 谷物质量性状
     */
    private String grainQualityTrait;

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
