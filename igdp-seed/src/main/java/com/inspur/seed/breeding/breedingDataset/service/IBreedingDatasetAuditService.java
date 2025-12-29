package com.inspur.seed.breeding.breedingDataset.service;

import com.inspur.common.core.domain.AjaxResult;
import com.inspur.seed.breeding.breedingDataset.domain.dto.BreedingDatasetAuditDTO;
import com.inspur.seed.breeding.breedingDataset.domain.dto.BreedingDatasetAuditQueryDTO;

/**
 * 育种数据集审核Service接口
 *
 * @author system
 * @since 2025-01-30
 */
public interface IBreedingDatasetAuditService {

    /**
     * 获取待审核/已审核数据集列表(分页)
     *
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    AjaxResult getAuditList(BreedingDatasetAuditQueryDTO queryDTO);

    /**
     * 根据ID获取审核详情
     *
     * @param id 审核记录ID
     * @return 审核详情
     */
    AjaxResult getAuditById(String id);

    /**
     * 根据数据集ID获取审核详情
     *
     * @param datasetId 数据集ID
     * @return 审核详情
     */
    AjaxResult getAuditByDatasetId(String datasetId);

    /**
     * 执行审核(通过/驳回)
     *
     * @param auditDTO 审核信息
     * @return 操作结果
     */
    AjaxResult performAudit(BreedingDatasetAuditDTO auditDTO);

    /**
     * 获取审核历史记录
     *
     * @param datasetId 数据集ID
     * @return 审核历史列表
     */
    AjaxResult getAuditHistory(String datasetId);
}
