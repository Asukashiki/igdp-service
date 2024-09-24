package com.inspur.workorder.service;

/**
 * 同步流程工单数据到待办的接口
 * @author liyunlong
 * @version 1.0
 * @ClassName IWorkOrderSyncService
 * @date 2024/6/22 19:59
 */
public interface IWorkOrderSyncService {

    /**
     * 根据configId同步
     * 配置内容在WorkOrderSyncConfig
     * @param configId 配置id
     * */
    void syncWorkOrderByConfigId(Long configId);
}
