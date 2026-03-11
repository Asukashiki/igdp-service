package com.inspur.agriculture.inventory.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.agriculture.inventory.domain.InventoryWarehouse;
import com.inspur.agriculture.inventory.mapper.InventoryWarehouseMapper;
import com.inspur.agriculture.inventory.service.IInventoryWarehouseService;
import com.inspur.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryWarehouseServiceImpl extends ServiceImpl<InventoryWarehouseMapper, InventoryWarehouse>
        implements IInventoryWarehouseService {

    @Autowired
    private InventoryWarehouseMapper warehouseMapper;

    @Override
    public List<InventoryWarehouse> selectWarehouseList(InventoryWarehouse warehouse) {
        LambdaQueryWrapper<InventoryWarehouse> queryWrapper = new LambdaQueryWrapper<>();
        if (warehouse != null) {
            if (warehouse.getId() != null) {
                queryWrapper.eq(InventoryWarehouse::getId, warehouse.getId());
            }
            if (StringUtils.isNotEmpty(warehouse.getWarehouseCode())) {
                queryWrapper.like(InventoryWarehouse::getWarehouseCode, warehouse.getWarehouseCode());
            }
            if (StringUtils.isNotEmpty(warehouse.getWarehouseName())) {
                queryWrapper.like(InventoryWarehouse::getWarehouseName, warehouse.getWarehouseName());
            }
            if (StringUtils.isNotEmpty(warehouse.getType())) {
                queryWrapper.eq(InventoryWarehouse::getType, warehouse.getType());
            }
            if (StringUtils.isNotEmpty(warehouse.getAddress())) {
                queryWrapper.like(InventoryWarehouse::getAddress, warehouse.getAddress());
            }
            if (StringUtils.isNotEmpty(warehouse.getStatus())) {
                queryWrapper.eq(InventoryWarehouse::getStatus, warehouse.getStatus());
            }
        }
        return warehouseMapper.selectList(queryWrapper);
    }
}
