package com.inspur.agriculture.inventory.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.agriculture.inventory.domain.InventoryTraceInfo;
import com.inspur.agriculture.inventory.mapper.InventoryTraceInfoMapper;
import com.inspur.agriculture.inventory.service.IInventoryTraceInfoService;
import org.springframework.stereotype.Service;

@Service
public class InventoryTraceInfoServiceImpl extends ServiceImpl<InventoryTraceInfoMapper, InventoryTraceInfo> implements IInventoryTraceInfoService {
}
