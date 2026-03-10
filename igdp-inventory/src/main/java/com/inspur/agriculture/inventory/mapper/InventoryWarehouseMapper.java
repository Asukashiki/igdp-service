package com.inspur.agriculture.inventory.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.agriculture.inventory.domain.InventoryWarehouse;

import java.util.List;

public interface InventoryWarehouseMapper extends BaseMapper<InventoryWarehouse> {

    List<InventoryWarehouse> selectWarehouseList(InventoryWarehouse warehouse);

    InventoryWarehouse selectWarehouseById(Long id);
}
