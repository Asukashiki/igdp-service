package com.inspur.workorder.service.impl;

import cn.hutool.core.util.StrUtil;
import com.inspur.common.constant.Constants;
import com.inspur.workorder.domain.WorkOrderSyncConfig;
import com.inspur.workorder.mapper.WorkOrderSyncConfigMapper;
import com.inspur.workorder.mapper.WorkOrderSyncMapper;
import com.inspur.workorder.service.IWorkOrderSyncService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author liyunlong
 * @version 1.0
 * @ClassName WorkOrderSyncServiceImpl
 * @date 2024/6/22 20:02
 */
@Service("workOrderSyncService")
public class WorkOrderSyncServiceImpl implements IWorkOrderSyncService {
    @Resource
    private WorkOrderSyncConfigMapper workOrderSyncConfigMapper;
    @Resource
    private WorkOrderSyncMapper workOrderSyncMapper;

    @Override
    public void syncWorkOrderByConfigId(Long configId) {
        WorkOrderSyncConfig config = workOrderSyncConfigMapper.selectById(configId);
        if (null != config && Constants.STATUS_VALID.equals(config.getStatus())) {
            String sqlStr = config.getSqlStr();
            if (StrUtil.isNotEmpty(sqlStr)) {
                if (!sqlStr.startsWith("drop") && !sqlStr.startsWith("truncate")) {
                    workOrderSyncMapper.syncBySql(sqlStr);
                }
            }
        }

    }
}
