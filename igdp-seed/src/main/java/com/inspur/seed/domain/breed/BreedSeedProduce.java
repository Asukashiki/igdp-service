package com.inspur.seed.domain.breed;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Breeder Seed 生产主表
 *
 * @author igdp
 */
@Data
@TableName("breed_seed_produce")
public class BreedSeedProduce implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键(UUID)
     */
    @TableId(type = IdType.INPUT)
    private String produceBatchId;

    /**
     * 批次名称
     */
    private String produceBatchName;

    /**
     * 育种批次ID
     */
    private String breedBatchId;

    /**
     * 育种批次名称(自动带出)
     */
    private String breedBatchName;

    /**
     * 品种ID
     */
    private String varietyId;

    /**
     * 品种名称(自动带出)
     */
    private String varietyName;

    /**
     * 实验批次ID
     */
    private String trialId;

    /**
     * 实验批次名称(自动带出)
     */
    private String trialName;

    /**
     * 作物类型(自动带出)
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
     * 地块名称(自动带出)
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
     * 种子等级来源
     */
    private String fromSeedLevel;

    /**
     * 种子等级去向
     */
    private String toSeedLevel;

    /**
     * 操作人ID
     */
    private String operatorId;

    /**
     * 操作人姓名(自动带出)
     */
    private String operatorName;

    /**
     * 生产状态(默认FINISHED)
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
