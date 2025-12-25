package com.inspur.seed.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.seed.domain.BreedingBatch;
import com.inspur.seed.domain.dto.BreedingBatchDTO;
import com.inspur.seed.domain.vo.BreedingBatchDetailVO;

import java.util.List;

/**
 * 育种批次信息Service接口
 *
 * @author inspur
 */
public interface IBreedingBatchService extends IService<BreedingBatch> {

    /**
     * 查询育种批次列表
     *
     * @param breedingBatch 查询条件
     * @return 育种批次列表
     */
    List<BreedingBatch> selectBreedingBatchList(BreedingBatch breedingBatch);


    /**
     * 查询作废育种批次列表
     *
     * @param breedingBatch 查询条件
     * @return 育种批次列表
     */
    List<BreedingBatch> selectBreedingBatchVoidedList(BreedingBatch breedingBatch);

    /**
     * 根据数据ID查询育种批次详细信息（包含审批意见）
     *
     * @param dataId 数据ID
     * @return 育种批次详细信息
     */
    BreedingBatchDetailVO selectBreedingBatchById(String dataId);

    /**
     * 新增育种批次
     *
     * @param breedingBatch 育种批次信息
     * @return 数据ID
     */
    String insertBreedingBatch(BreedingBatch breedingBatch);

    /**
     * 修改育种批次
     *
     * @param breedingBatch 育种批次信息
     * @return 影响行数
     */
    int updateBreedingBatch(BreedingBatch breedingBatch);

    /**
     * 批量删除育种批次
     *
     * @param dataIds 数据ID数组
     * @return 影响行数
     */
    int deleteBreedingBatchByIds(String[] dataIds);

    /**
     * 获取育种批次下拉列表
     *
     * @return 育种批次列表
     */
    List<BreedingBatch> selectBatchOptions();

    /**
     * 提交审核
     *
     * @param dataId 数据ID
     * @return 影响行数
     */
    int submitAudit(String dataId);

    /**
     * 审核通过
     *
     * @param breedingBatchDTO 包含审批意见的育种批次信息
     * @return 影响行数
     */
    int approve(BreedingBatchDTO breedingBatchDTO);

    /**
     * 审核驳回
     *
     * @param breedingBatchDTO 包含审批意见的育种批次信息
     * @return 影响行数
     */
    int reject(BreedingBatchDTO breedingBatchDTO);

    /**
     * 归档
     *
     * @param dataId 数据ID
     * @return 影响行数
     */
    int archive(String dataId);

    /**
     * 作废
     *
     * @param dataId 数据ID
     * @return 影响行数
     */
    int cancel(String dataId);

    boolean finished(String batchId);
}
