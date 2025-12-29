package com.inspur.seed.breeding.seedDistribution.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Breeder Seed 分发数据查询DTO
 *
 * @author igdp
 */
@Data
public class BreedSeedDistributeQueryDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 种子生产批次ID
     */
    private String produceBatchId;

    private String produceBatchName;

    /**
     * 作物种类
     */
    private String cropType;

    /**
     * 品种ID
     */
    private String varietyId;

    /**
     * 查询起始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startTime;

    /**
     * 查询结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endTime;
}
