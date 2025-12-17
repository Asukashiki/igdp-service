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
 * Breeder Seed 分发明细表
 *
 * @author igdp
 */
@Data
@TableName("breed_seed_distribute_detail")
public class BreedSeedDistributeDetail implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键(UUID)
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String distributeDetailId;

    /**
     * 分发主表ID
     */
    private String distributeId;

    /**
     * 生产批次ID
     */
    private String produceBatchId;

    /**
     * 生产批次名称(自动带出)
     */
    private String produceBatchName;

    /**
     * 品种ID
     */
    private String breedBatchName;


    private String parentalSeedSource;

    /**
     * 品种名称(自动带出)
     */
    private String varietyName;

    /**
     * 作物类型(自动带出)
     */
    private String cropType;

    /**
     * 分发数量
     */
    private BigDecimal distributeQuantity;

    /**
     * 生产批次剩余可分发量
     */
    private BigDecimal produceBatchRemaining;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
