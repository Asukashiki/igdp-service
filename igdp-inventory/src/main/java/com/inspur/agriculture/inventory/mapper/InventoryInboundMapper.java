package com.inspur.agriculture.inventory.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.agriculture.inventory.domain.InventoryInbound;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface InventoryInboundMapper extends BaseMapper<InventoryInbound> {

    /**
     * 查询入库单（包含仓库名称）
     */
    InventoryInbound selectInboundWithWarehouse(@Param("id") Long id);

    /**
     * 查询入库单列表（包含仓库名称）
     */
    List<InventoryInbound> selectInboundListWithWarehouse(InventoryInbound inbound);
}
