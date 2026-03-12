package com.inspur.agriculture.inventory.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.agriculture.inventory.domain.InventoryStockBatch;

import java.util.List;

public interface IInventoryStockBatchService extends IService<InventoryStockBatch> {

    /**
     * 根据仓库编码查询批次库存列表
     * @param warehouseCode 仓库编码
     * @return 批次库存列表
     */
    List<InventoryStockBatch> listByWarehouseCode(String warehouseCode);

    /**
     * 根据批次号和仓库编码查询批次详情
     * @param batchNo 批次号
     * @param warehouseCode 仓库编码
     * @return 批次库存详情
     */
    InventoryStockBatch getBatchDetail(String batchNo, String warehouseCode);
}
