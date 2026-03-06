package com.inspur.agriculture.inventory.service;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 库存核心服务接口
 */
public interface IInventoryCoreService {

    /**
     * 锁定库存 (下单/申请出库)
     * @param skuId SKU ID
     * @param warehouseId 仓库ID
     * @param qty 数量
     */
    void lockStock(Long skuId, Long warehouseId, BigDecimal qty);

    /**
     * 释放锁定库存 (取消订单)
     * @param skuId SKU ID
     * @param warehouseId 仓库ID
     * @param qty 数量
     */
    void releaseStock(Long skuId, Long warehouseId, BigDecimal qty);

    /**
     * 扣减库存 (确认出库)
     * @param skuId SKU ID
     * @param warehouseId 仓库ID
     * @param batchNo 批次号
     * @param qty 数量
     */
    void reduceStock(Long skuId, Long warehouseId, String batchNo, BigDecimal qty);

    /**
     * 增加库存 (确认入库)
     * @param skuId SKU ID
     * @param warehouseId 仓库ID
     * @param batchNo 批次号
     * @param qty 数量
     * @param prodDate 生产日期
     * @param expDate 过期日期
     */
    void increaseStock(Long skuId, Long warehouseId, String batchNo, BigDecimal qty, Date prodDate, Date expDate);

    /**
     * 预占库存 (可选)
     * @param skuId SKU ID
     * @param warehouseId 仓库ID
     * @param qty 数量
     */
    void reserveStock(Long skuId, Long warehouseId, BigDecimal qty);
}
