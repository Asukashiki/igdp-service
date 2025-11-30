package com.inspur.seed.vo.breed;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Breeder Seed 生产数据VO
 *
 * @author igdp
 */
@Data
public class BreedSeedProduceVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 种子生产批次ID
     */
    private String breedSeedProduceBatchId;

    /**
     * 育种批次ID
     */
    private String breedBatchId;

    /**
     * 品种ID
     */
    private String varietyId;

    /**
     * 品种名称
     */
    private String varietyName;

    /**
     * 作物类型
     */
    private String cropType;

    /**
     * 生产时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date time;

    /**
     * 地块ID
     */
    private String landId;

    /**
     * 地块名称
     */
    private String landName;

    /**
     * 投入种子数量
     */
    private BigDecimal inputSeedQuantity;

    /**
     * 产出种子数量
     */
    private BigDecimal produceSeedQuantrity;

    /**
     * 操作人ID
     */
    private String operatorId;

    /**
     * 操作人姓名
     */
    private String operatorName;

    /**
     * 生产状态
     */
    private String produceStatus;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
