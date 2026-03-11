package com.inspur.agriculture.inventory.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.agriculture.inventory.domain.InventoryProduct;

import java.util.List;

public interface InventoryProductMapper extends BaseMapper<InventoryProduct> {

    List<InventoryProduct> selectProductList(InventoryProduct product);

    InventoryProduct selectProductById(Long id);
}
