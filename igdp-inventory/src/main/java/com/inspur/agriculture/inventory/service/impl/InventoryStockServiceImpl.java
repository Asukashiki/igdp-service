package com.inspur.agriculture.inventory.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.agriculture.inventory.domain.InventoryStock;
import com.inspur.agriculture.inventory.mapper.InventoryStockMapper;
import com.inspur.agriculture.inventory.service.IInventoryStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryStockServiceImpl extends ServiceImpl<InventoryStockMapper, InventoryStock> implements IInventoryStockService {

    @Autowired
    private InventoryStockMapper stockMapper;

    @Override
    public List<InventoryStock> selectStockList(InventoryStock stock) {
        return stockMapper.selectStockList(stock);
    }

    @Override
    public InventoryStock selectStockById(Long id) {
        return stockMapper.selectStockById(id);
    }
}
