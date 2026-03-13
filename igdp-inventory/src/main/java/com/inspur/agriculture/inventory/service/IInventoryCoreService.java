package com.inspur.agriculture.inventory.service;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 库存核心服务接口
 */
public interface IInventoryCoreService {

    /**
     * 锁定库存 (下单/申请出库)
     * @param productId Product ID
     * @param warehouseCode 仓库编码
     * @param qty 数量
     */
    void lockStock(Long productId, String warehouseCode, BigDecimal qty);

    /**
     * 释放锁定库存 (取消订单)
     * @param productId Product ID
     * @param warehouseCode 仓库编码
     * @param qty 数量
     */
    void releaseStock(Long productId, String warehouseCode, BigDecimal qty);

    /**
     * 扣减库存 (确认出库)
     * @param productId Product ID
     * @param warehouseCode 仓库编码
     * @param batchNo 批次号
     * @param qty 数量
     */
    void reduceStock(Long productId, String warehouseCode, String batchNo, BigDecimal qty);

    /**
     * Reduce stock with separate batch quantity.
     * @param productId Product ID
     * @param warehouseCode Warehouse code
     * @param batchNo Batch number
     * @param stockQtyKg Stock quantity in KG
     * @param batchQty Batch quantity in its own unit
     */
    void reduceStockWithBatch(Long productId, String warehouseCode, String batchNo, BigDecimal stockQtyKg, BigDecimal batchQty);

    /**
     * 增加库存 (确认入库)
     * @param productId Product ID
     * @param warehouseCode 仓库编码
     * @param batchNo 批次号
     * @param qty 数量
     * @param prodDate 生产日期
     * @param expDate 过期日期
     */
    void increaseStock(Long productId, String warehouseCode, String batchNo, BigDecimal qty, Date prodDate, Date expDate);

    /**
     * 增加库存 (确认入库) - 支持质量等级和状态
     * @param productId Product ID
     * @param warehouseCode 仓库编码
     * @param batchNo 批次号
     * @param qty 数量
     * @param prodDate 生产日期
     * @param expDate 过期日期
     * @param qualityGrade 质量等级
     * @param stockStatus 库存状态
     */
    void increaseStock(Long productId, String warehouseCode, String batchNo, BigDecimal qty, Date prodDate, Date expDate, String qualityGrade, String stockStatus);

    /**
     * 增加库存 (确认入库) - 支持商品分类
     * @param productId Product ID
     * @param warehouseCode 仓库编码
     * @param batchNo 批次号
     * @param qty 数量
     * @param prodDate 生产日期
     * @param expDate 过期日期
     * @param qualityGrade 质量等级
     * @param stockStatus 库存状态
     * @param mainCategory 商品大类
     * @param subCategory 商品小类
     */
    void increaseStock(Long productId, String warehouseCode, String batchNo, BigDecimal qty, Date prodDate, Date expDate, String qualityGrade, String stockStatus, String mainCategory, String subCategory);

    /**
     * Increase stock with separate batch quantity and unit.
     * @param productId Product ID
     * @param warehouseCode Warehouse code
     * @param batchNo Batch number
     * @param stockQtyKg Stock quantity in KG
     * @param batchQty Batch quantity in its own unit
     * @param batchUnit Batch unit
     * @param prodDate Production date
     * @param expDate Expire date
     * @param qualityGrade Quality grade
     * @param stockStatus Stock status
     * @param mainCategory Main category
     * @param subCategory Sub category
     */
    void increaseStockWithBatch(Long productId, String warehouseCode, String batchNo, BigDecimal stockQtyKg, BigDecimal batchQty, String batchUnit,
                                Date prodDate, Date expDate, String qualityGrade, String stockStatus, String mainCategory, String subCategory);

    /**
     * 预占库存 (可选)
     * @param productId Product ID
     * @param warehouseCode 仓库编码
     * @param qty 数量
     */
    void reserveStock(Long productId, String warehouseCode, BigDecimal qty);
}
