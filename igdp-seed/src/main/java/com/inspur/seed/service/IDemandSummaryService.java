package com.inspur.seed.service;

/**
 * Demand Summary Service Interface
 * 需求汇总服务接口
 *
 * @author igdp
 * @date 2025-12-04
 */
public interface IDemandSummaryService {

    /**
     * 为单个农民需求生成汇总数据
     * 当DA提交农民需求时调用
     *
     * @param demandId 农民需求ID
     */
    void generateSummaryForDemand(String demandId);

    /**
     * 为整个批次重新生成汇总数据
     * 用于数据修正或批量重算
     *
     * @param batchId 批次ID
     */
    void regenerateSummaryForBatch(String batchId);

    /**
     * 删除农民需求时更新汇总数据
     * 当DA删除农民需求时调用
     *
     * @param demandId 农民需求ID
     */
    void updateSummaryOnDemandDelete(String demandId);
}
