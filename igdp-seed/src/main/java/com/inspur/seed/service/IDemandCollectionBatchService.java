package com.inspur.seed.service;

import com.inspur.seed.domain.entity.DemandCollectionBatch;

import java.util.List;

/**
 * Demand Collection Batch Service Interface
 * 需求采集批次管理服务接口
 *
 * @author igdp
 * @date 2025-12-05
 */
public interface IDemandCollectionBatchService {

    /**
     * 根据年份获取或创建批次
     * Get or create batch by year
     * 如果当前年份的批次不存在，则自动创建
     *
     * @param year 年份
     * @return 批次信息
     */
    DemandCollectionBatch getOrCreateBatchByYear(String batchNo,Integer year);
}
