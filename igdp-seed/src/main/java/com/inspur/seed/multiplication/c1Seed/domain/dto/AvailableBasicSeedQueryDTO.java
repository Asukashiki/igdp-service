package com.inspur.seed.multiplication.c1Seed.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 查询可用Basic种子DTO
 *
 * @author system
 * @since 2026-01-04
 */
@Data
public class AvailableBasicSeedQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 品种名称（模糊查询）
     */
    private String varietyName;

    /**
     * 作物类型
     */
    private String cropType;

    /**
     * 数据来源类型过滤
     * OSE_RECEIVE: 只返回OSE接收确认的种子
     * OSE_BATCH_COLLECTION: 只返回批次采集的种子
     * null: 返回所有来源的种子
     */
    private String sourceType;

    /**
     * 是否只返回有可用数量的种子
     */
    private Boolean onlyAvailable;
}
