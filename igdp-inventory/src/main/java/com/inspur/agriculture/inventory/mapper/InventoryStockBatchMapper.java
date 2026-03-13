package com.inspur.agriculture.inventory.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.agriculture.inventory.domain.InventoryStockBatch;

import java.util.List;

public interface InventoryStockBatchMapper extends BaseMapper<InventoryStockBatch> {

    /**
     * 查询批次库存列表
     * @param batch 批次库存信息
     * @return 批次库存集合
     */
    List<InventoryStockBatch> selectBatchList(InventoryStockBatch batch);
}
