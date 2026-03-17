package com.inspur.agriculture.inventory.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.agriculture.inventory.domain.InventoryStockBatch;
import com.inspur.agriculture.inventory.domain.InventoryWarehouse;
import com.inspur.agriculture.inventory.mapper.InventoryStockBatchMapper;
import com.inspur.agriculture.inventory.service.IInventoryStockBatchService;
import com.inspur.agriculture.inventory.service.IInventoryWarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class InventoryStockBatchServiceImpl extends ServiceImpl<InventoryStockBatchMapper, InventoryStockBatch> implements IInventoryStockBatchService {

    @Autowired
    private IInventoryWarehouseService warehouseService;

    @Override
    public List<InventoryStockBatch> listByWarehouseCode(String warehouseCode) {
        if (StringUtils.isBlank(warehouseCode)) {
            return Collections.emptyList();
        }
        InventoryWarehouse warehouse = warehouseService.selectWarehouseByCode(warehouseCode);
        if (warehouse == null) {
            return Collections.emptyList();
        }

        LambdaQueryWrapper<InventoryStockBatch> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InventoryStockBatch::getWarehouseId, warehouse.getId())
                .orderByDesc(InventoryStockBatch::getUpdateTime);
        return this.list(wrapper);
    }

    @Override
    public InventoryStockBatch getBatchDetail(String batchNo, String warehouseCode) {
        if (StringUtils.isBlank(batchNo) || StringUtils.isBlank(warehouseCode)) {
            return null;
        }
        InventoryWarehouse warehouse = warehouseService.selectWarehouseByCode(warehouseCode);
        if (warehouse == null) {
            return null;
        }

        LambdaQueryWrapper<InventoryStockBatch> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InventoryStockBatch::getWarehouseId, warehouse.getId())
                .eq(InventoryStockBatch::getBatchNo, batchNo);
        return this.getOne(wrapper, false);
    }

    @Override
    public List<InventoryStockBatch> selectBatchList(InventoryStockBatch batch) {
        return baseMapper.selectBatchList(batch);
    }
}
