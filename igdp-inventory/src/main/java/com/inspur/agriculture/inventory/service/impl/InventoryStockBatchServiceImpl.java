package com.inspur.agriculture.inventory.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.agriculture.inventory.domain.InventoryStockBatch;
import com.inspur.agriculture.inventory.mapper.InventoryStockBatchMapper;
import com.inspur.agriculture.inventory.service.IInventoryStockBatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryStockBatchServiceImpl extends ServiceImpl<InventoryStockBatchMapper, InventoryStockBatch> implements IInventoryStockBatchService {

    @Autowired
    private InventoryStockBatchMapper batchMapper;

    @Override
    public List<InventoryStockBatch> selectBatchList(InventoryStockBatch batch) {
        return batchMapper.selectBatchList(batch);
    }
}
