package com.inspur.seed.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.seed.domain.OseBatchCollection;

import java.util.List;

/**
 * OSE繁殖批次信息数据采集 - Service接口
 * OSE Batch Information Data Collection Service
 *
 * @author system
 * @date 2026-01-04
 */
public interface IOseBatchCollectionService extends IService<OseBatchCollection> {

    /**
     * 查询OSE繁殖批次采集列表
     *
     * @param oseBatchCollection 查询条件
     * @return 采集列表
     */
    List<OseBatchCollection> selectOseBatchCollectionList(OseBatchCollection oseBatchCollection);

    /**
     * 新增OSE繁殖批次采集
     *
     * @param oseBatchCollection 采集信息
     * @return 结果
     */
    int insertOseBatchCollection(OseBatchCollection oseBatchCollection);

    /**
     * 修改OSE繁殖批次采集
     *
     * @param oseBatchCollection 采集信息
     * @return 结果
     */
    int updateOseBatchCollection(OseBatchCollection oseBatchCollection);

    /**
     * 批量删除OSE繁殖批次采集
     *
     * @param ids 需要删除的ID数组
     * @return 结果
     */
    int deleteOseBatchCollectionByIds(Long[] ids);
}
