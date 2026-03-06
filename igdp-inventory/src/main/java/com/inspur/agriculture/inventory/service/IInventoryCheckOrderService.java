package com.inspur.agriculture.inventory.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.agriculture.inventory.domain.InventoryCheckOrder;

/**
 * 盘点单服务接口
 */
public interface IInventoryCheckOrderService extends IService<InventoryCheckOrder> {

    /**
     * 创建盘点单
     * @param checkOrder 盘点单
     * @return 结果
     */
    boolean createCheckOrder(InventoryCheckOrder checkOrder);

    /**
     * 盘点录入 (更新实盘数量)
     * @param checkOrder 盘点单 (含明细)
     * @return 结果
     */
    boolean recordCheckResult(InventoryCheckOrder checkOrder);

    /**
     * 盘点审核 (生效库存调整)
     * @param checkOrder 盘点单 (含审核信息)
     * @return 结果
     */
    boolean auditCheckOrder(InventoryCheckOrder checkOrder);
}
