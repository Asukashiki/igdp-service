package com.inspur.agriculture.inventory.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.agriculture.inventory.domain.InventoryOutbound;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface InventoryOutboundMapper extends BaseMapper<InventoryOutbound> {

    /**
     * 查询出库单（包含仓库名称）
     */
    InventoryOutbound selectOutboundWithWarehouse(@Param("id") Long id);

    /**
     * 查询出库单列表（包含仓库名称）
     */
    List<InventoryOutbound> selectOutboundListWithWarehouse(InventoryOutbound outbound);
}
