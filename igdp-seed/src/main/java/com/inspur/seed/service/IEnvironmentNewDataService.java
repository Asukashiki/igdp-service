package com.inspur.seed.service;

import com.inspur.seed.domain.EnvironmentNewData;

import java.util.List;

/**
 * 环境监测新数据Service接口
 * Environment New Data Service Interface
 *
 * @author inspur
 */
public interface IEnvironmentNewDataService {

    /**
     * 查询环境监测数据列表
     * Query environment new data list
     *
     * @param environmentNewData 查询条件
     * @return 环境数据列表
     */
    List<EnvironmentNewData> selectEnvironmentNewDataList(EnvironmentNewData environmentNewData);

    /**
     * 根据ID查询环境数据详情
     * Query environment data by ID
     *
     * @param envRecordId 环境记录ID
     * @return 环境数据
     */
    EnvironmentNewData selectEnvironmentNewDataById(String envRecordId);

    /**
     * 新增环境监测数据
     * Insert new environment data
     *
     * @param environmentNewData 环境数据
     * @return 环境记录ID
     */
    String insertEnvironmentNewData(EnvironmentNewData environmentNewData);

    /**
     * 修改环境监测数据
     * Update environment data
     *
     * @param environmentNewData 环境数据
     * @return 影响行数
     */
    int updateEnvironmentNewData(EnvironmentNewData environmentNewData);

    /**
     * 批量删除环境监测数据
     * Batch delete environment data
     *
     * @param envRecordIds 环境记录ID数组
     * @return 影响行数
     */
    int deleteEnvironmentNewDataByIds(String[] envRecordIds);

    /**
     * 提交环境监测数据审核
     * Submit environment new data for audit
     *
     * @param envRecordId 环境记录ID
     * @return 影响行数
     */
    int submitForAudit(String envRecordId);

    /**
     * 审核通过环境监测数据
     * Approve environment new data
     *
     * @param envRecordId 环境记录ID
     * @param auditComment 审核意见
     * @return 影响行数
     */
    int approve(String envRecordId, String auditComment);

    /**
     * 驳回环境监测数据
     * Reject environment new data
     *
     * @param envRecordId 环境记录ID
     * @param auditComment 审核意见
     * @return 影响行数
     */
    int reject(String envRecordId, String auditComment);
}
