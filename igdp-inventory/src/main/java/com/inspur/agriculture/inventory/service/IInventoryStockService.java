package com.inspur.agriculture.inventory.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.agriculture.inventory.domain.InventoryStock;

import java.util.List;

public interface IInventoryStockService extends IService<InventoryStock> {
    
    /**
     * 查询库存列表
     * @param stock 库存信息
     * @return 库存集合
     */
    List<InventoryStock> selectStockList(InventoryStock stock);

    /**
     * 根据ID查询库存
     * @param id 库存ID
     * @return 库存信息
     */
    InventoryStock selectStockById(Long id);
}
