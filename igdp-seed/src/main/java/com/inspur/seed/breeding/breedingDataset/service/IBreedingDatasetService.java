package com.inspur.seed.breeding.breedingDataset.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.seed.breeding.breedingDataset.domain.dto.BreedingDatasetDTO;
import com.inspur.seed.breeding.breedingDataset.domain.dto.BreedingDatasetQueryDTO;
import com.inspur.seed.breeding.breedingDataset.domain.entity.BreedingDataset;

/**
 * 育种数据集Service接口
 *
 * @author system
 * @date 2025-01-30
 */
public interface IBreedingDatasetService extends IService<BreedingDataset> {

    /**
     * 查询育种数据集列表
     *
     * @param queryDTO 查询条件
     * @return 数据集列表
     */
    AjaxResult getDatasetList(BreedingDatasetQueryDTO queryDTO);

    /**
     * 根据ID查询育种数据集详情
     *
     * @param id 数据集ID
     * @return 数据集详情
     */
    AjaxResult getDatasetById(String id);

    /**
     * 新增育种数据集
     *
     * @param dto 数据集信息
     * @return 操作结果
     */
    AjaxResult addDataset(BreedingDatasetDTO dto);

    /**
     * 修改育种数据集
     *
     * @param dto 数据集信息
     * @return 操作结果
     */
    AjaxResult updateDataset(BreedingDatasetDTO dto);

    /**
     * 删除育种数据集
     *
     * @param ids 数据集ID数组
     * @return 操作结果
     */
    AjaxResult deleteDataset(String[] ids);

    /**
     * 提交审核
     *
     * @param id 数据集ID
     * @return 操作结果
     */
    AjaxResult submitDataset(String id);

    /**
     * 统计数据集各项数据记录数
     *
     * @param batchId 育种批次ID
     * @return 统计结果
     */
    AjaxResult statisticsData(String batchId);
}
