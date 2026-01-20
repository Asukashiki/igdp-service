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
     * 同步农事记录数据
     *
     * @param request 同步请求
     * @return 同步结果
     */
    AjaxResult syncFarmingRecord(OfflineSyncRequest request);
}
