package com.inspur.workorder.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.workorder.domain.WorkOrderNodeDetail;
import com.inspur.workorder.mapper.WorkOrderNodeDetailMapper;
import com.inspur.workorder.service.IWorkOrderNodeDetailService;
import org.springframework.stereotype.Service;

/**
 * @author liyunlong
 * @version 1.0
 * @ClassName WorkOrderNodeDetailServiceImpl
 * @date 2024/4/28 16:14
 */
@Service
public class WorkOrderNodeDetailServiceImpl extends ServiceImpl<WorkOrderNodeDetailMapper, WorkOrderNodeDetail> implements IWorkOrderNodeDetailService {
}
