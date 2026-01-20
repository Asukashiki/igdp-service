package com.inspur.seed.multiplication.c1Seed.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 可用Basic种子VO
 * 聚合OSE接收确认和批次采集两个数据源的Basic种子
 *
 * @author system
 * @since 2026-01-04
 */
@Data
public class AvailableBasicSeedVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 批次ID（唯一标识）
     */
    private String batchId;

    /**
     * 数据来源类型
     * OSE_RECEIVE: OSE接收确认模块
     * OSE_BATCH_COLLECTION: OSE批次信息数据采集模块
     */
    private String sourceType;

    /**
     * 来源记录ID（用于关联原始数据）
     */
    private String sourceId;

    /**
     * 品种名称
     */
    private String varietyName;

    /**
     * 作物类型
     */
    private String cropType;

    /**
     * 繁殖级别（应该都是Basic）
     */
    private String breedingLevel;

    /**
     * 亲本种子来源
     */
    private String parentalSeedSource;

    /**
     * 总数量 (kg)
     */
    private BigDecimal totalQuantity;

    /**
     * 已申请数量 (kg)
     */
    private BigDecimal appliedQuantity;

    /**
     * 可用数量 (kg)
     */
    private BigDecimal availableQuantity;

    /**
     * 批次状态
     */
    private String batchStatus;

    /**
     * 接收日期/采集日期
     */
    private String receiveDate;

    /**
     * 备注
     */
    private String remark;
}
