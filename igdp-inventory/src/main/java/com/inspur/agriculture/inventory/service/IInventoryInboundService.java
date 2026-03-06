package com.inspur.agriculture.inventory.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.agriculture.inventory.domain.InventoryInbound;

/**
 * 入库单服务接口
 */
public interface IInventoryInboundService extends IService<InventoryInbound> {
    
    /**
     * 创建入库单
     * @param inbound 入库单
     * @return 结果
     */
    boolean createInbound(InventoryInbound inbound);

    /**
     * 审核入库单
     * @param id 入库单ID
     * @return 结果
     */
    boolean approveInbound(Long id);
}
