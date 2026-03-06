package com.inspur.agriculture.inventory.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.agriculture.inventory.domain.InventoryOutbound;

/**
 * 出库单服务接口
 */
public interface IInventoryOutboundService extends IService<InventoryOutbound> {

    /**
     * 创建出库单 (并锁定库存)
     * @param outbound 出库单
     * @return 结果
     */
    boolean createOutbound(InventoryOutbound outbound);

    /**
     * 审核出库单 (并扣减库存)
     * @param id 出库单ID
     * @return 结果
     */
    boolean approveOutbound(Long id);
}
