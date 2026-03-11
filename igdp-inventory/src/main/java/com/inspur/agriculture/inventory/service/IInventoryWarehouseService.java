package com.inspur.agriculture.inventory.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.agriculture.inventory.domain.InventoryWarehouse;

import java.util.List;

public interface IInventoryWarehouseService extends IService<InventoryWarehouse> {
    List<InventoryWarehouse> selectWarehouseList(InventoryWarehouse warehouse);
}
