package com.inspur.offline.service;

import com.inspur.common.core.domain.AjaxResult;
import com.inspur.offline.domain.OfflineSyncRequest;

/**
 * 离线数据同步服务接口
 *
 * @author inspur
 */
public interface IOfflineSyncService {

    /**
     * 同步农民数据
     *
     * @param request 同步请求
     * @return 同步结果
     */
    AjaxResult syncFarmer(OfflineSyncRequest request);

    /**
     * 同步土地数据
     *
     * @param request 同步请求
     * @return 同步结果
     */
    AjaxResult syncLand(OfflineSyncRequest request);

    /**
     * 同步农艺性状数据
     *
     * @param request 同步请求
     * @return 同步结果
     */
    AjaxResult syncTrait(OfflineSyncRequest request);

    /**
     * 同步环境监测数据
     *
     * @param request 同步请求
     * @return 同步结果
     */
    AjaxResult syncEnvironment(OfflineSyncRequest request);

    /**
     * 同步田间检验数据
     *
     * @param request 同步请求
     * @return 同步结果
     */
    AjaxResult syncYield(OfflineSyncRequest request);

    /**
     * 同步繁育田间检测数据（仅新增）
     * 离线数据的ID是手动输入的，需要判断是否已存在防止重复提交
     *
     * @param request 同步请求
     * @return 同步结果
     */
    AjaxResult syncC1BreedingTracking(OfflineSyncRequest request);

    /**
     * 同步农事记录数据
     *
     * @param request 同步请求
     * @return 同步结果
     */
    AjaxResult syncFarmingRecord(OfflineSyncRequest request);

    /**
     * 同步农户需求数据（仅新增）
     * 离线数据的ID是手动输入的，需要判断是否已存在防止重复提交
     *
     * @param request 同步请求
     * @return 同步结果
     */
    AjaxResult syncFarmerDemand(OfflineSyncRequest request);
}
