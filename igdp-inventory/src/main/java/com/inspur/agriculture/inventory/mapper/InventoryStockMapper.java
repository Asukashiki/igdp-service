package com.inspur.agriculture.inventory.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.agriculture.inventory.domain.InventoryStock;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

public interface InventoryStockMapper extends BaseMapper<InventoryStock> {

    /**
     * 查询库存列表
     * @param stock 库存信息
     * @return 库存集合
     */
    List<InventoryStock> selectStockList(InventoryStock stock);

    /**
     * 乐观锁更新库存
     * @param id 库存ID
     * @param qty 变动数量
     * @param version 版本号
     * @return 更新行数
     */
    int updateStockOptimistic(@Param("id") Long id, @Param("qty") BigDecimal qty, @Param("version") Long version);
}
