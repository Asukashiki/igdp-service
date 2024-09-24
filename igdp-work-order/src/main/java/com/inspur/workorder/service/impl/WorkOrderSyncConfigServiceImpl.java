package com.inspur.workorder.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.workorder.domain.WorkOrderSyncConfig;
import com.inspur.workorder.mapper.WorkOrderSyncConfigMapper;
import com.inspur.workorder.service.IWorkOrderSyncConfigService;
import org.springframework.stereotype.Service;

/**
 * @author liyunlong
 * @version 1.0
 * @ClassName WorkOrderSyncConfigServiceImpl
 * @date 2024/6/22 19:42
 */
@Service
public class WorkOrderSyncConfigServiceImpl extends ServiceImpl<WorkOrderSyncConfigMapper, WorkOrderSyncConfig> implements IWorkOrderSyncConfigService {
}
