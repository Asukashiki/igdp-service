package com.inspur.agriculture.inventory.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.agriculture.inventory.domain.InventoryStockBatch;

import java.util.List;

public interface IInventoryStockBatchService extends IService<InventoryStockBatch> {

    /**
     * 查询批次库存列表
     * @param batch 批次库存信息
     * @return 批次库存集合
     */
    List<InventoryStockBatch> selectBatchList(InventoryStockBatch batch);
}
