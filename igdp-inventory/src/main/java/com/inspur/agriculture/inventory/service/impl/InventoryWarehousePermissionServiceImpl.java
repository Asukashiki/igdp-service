package com.inspur.agriculture.inventory.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.agriculture.inventory.domain.InventoryWarehousePermission;
import com.inspur.agriculture.inventory.mapper.InventoryWarehousePermissionMapper;
import com.inspur.agriculture.inventory.service.IInventoryWarehousePermissionService;
import org.springframework.stereotype.Service;

@Service
public class InventoryWarehousePermissionServiceImpl extends ServiceImpl<InventoryWarehousePermissionMapper, InventoryWarehousePermission> implements IInventoryWarehousePermissionService {
}
