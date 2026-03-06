package com.inspur.agriculture.inventory.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.inspur.agriculture.inventory.domain.InventoryStock;
import com.inspur.agriculture.inventory.domain.InventoryStockBatch;
import com.inspur.agriculture.inventory.domain.InventoryStockLog;
import com.inspur.agriculture.inventory.mapper.InventoryStockBatchMapper;
import com.inspur.agriculture.inventory.mapper.InventoryStockLogMapper;
import com.inspur.agriculture.inventory.mapper.InventoryStockMapper;
import com.inspur.agriculture.inventory.service.IInventoryCoreService;
import com.inspur.common.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 库存核心服务实现
 */
@Service
public class InventoryCoreServiceImpl implements IInventoryCoreService {

    @Autowired
    private InventoryStockMapper stockMapper;
    
    @Autowired
    private InventoryStockBatchMapper stockBatchMapper;
    
    @Autowired
    private InventoryStockLogMapper stockLogMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void lockStock(Long skuId, Long warehouseId, BigDecimal qty) {
        if (qty == null || qty.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ServiceException("锁定数量必须大于0");
        }

        // 1. 查询或初始化库存
        InventoryStock stock = getOrCreateStock(skuId, warehouseId);

        // 2. 检查可用库存
        if (stock.getAvailableQty().compareTo(qty) < 0) {
            throw new ServiceException("可用库存不足，当前可用: " + stock.getAvailableQty());
        }

        // 3. 乐观锁更新 (available -= qty, locked += qty)
        // 注意：Mapper XML 中逻辑是 available - qty, locked + qty
        int rows = stockMapper.updateStockOptimistic(stock.getId(), qty, stock.getVersion());
        if (rows == 0) {
            throw new ServiceException("库存正忙，请重试");
        }

        // 4. 记录日志
        recordLog(skuId, warehouseId, null, "LOCK", qty, stock.getAvailableQty(), stock.getAvailableQty().subtract(qty), "LOCK_STOCK", null, null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void releaseStock(Long skuId, Long warehouseId, BigDecimal qty) {
        if (qty == null || qty.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ServiceException("释放数量必须大于0");
        }

        InventoryStock stock = getOrCreateStock(skuId, warehouseId);

        // 释放逻辑：available += qty, locked -= qty
        // 这里可以使用 updateStockOptimistic 的逆操作，或者单独写一个 update
        // 为了简单，这里直接更新字段，使用乐观锁
        // 但 updateStockOptimistic 是固定的减可用加锁定。
        // 所以我需要手动更新
        
        // 重新查询以获取最新版本
        stock = stockMapper.selectById(stock.getId());
        if (stock.getLockedQty().compareTo(qty) < 0) {
             throw new ServiceException("释放数量大于锁定数量");
        }
        
        stock.setAvailableQty(stock.getAvailableQty().add(qty));
        stock.setLockedQty(stock.getLockedQty().subtract(qty));
        int rows = stockMapper.updateById(stock);
        if (rows == 0) {
            throw new ServiceException("库存更新失败(乐观锁)，请重试");
        }

        recordLog(skuId, warehouseId, null, "RELEASE", qty, stock.getAvailableQty().subtract(qty), stock.getAvailableQty(), "RELEASE_STOCK", null, null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void reduceStock(Long skuId, Long warehouseId, String batchNo, BigDecimal qty) {
        if (qty == null || qty.compareTo(BigDecimal.ZERO) <= 0) {
             throw new ServiceException("扣减数量必须大于0");
        }

        // 1. 扣减总库存的锁定部分 (locked -= qty)
        InventoryStock stock = getOrCreateStock(skuId, warehouseId);
        if (stock.getLockedQty().compareTo(qty) < 0) {
            throw new ServiceException("扣减数量大于锁定数量(需先锁定)");
        }
        stock.setLockedQty(stock.getLockedQty().subtract(qty));
        int rows = stockMapper.updateById(stock);
        if (rows == 0) {
            throw new ServiceException("库存更新失败，请重试");
        }

        // 2. 扣减批次库存 (如果指定了批次)
        if (batchNo != null && !batchNo.isEmpty()) {
            LambdaQueryWrapper<InventoryStockBatch> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(InventoryStockBatch::getSkuId, skuId)
                        .eq(InventoryStockBatch::getWarehouseId, warehouseId)
                        .eq(InventoryStockBatch::getBatchNo, batchNo);
            InventoryStockBatch batch = stockBatchMapper.selectOne(queryWrapper);
            if (batch == null || batch.getQty().compareTo(qty) < 0) {
                throw new ServiceException("批次库存不足");
            }
            batch.setQty(batch.getQty().subtract(qty));
            stockBatchMapper.updateById(batch);
        } else {
             // 如果未指定批次，可能需要先进先出扣减，这里简化为不处理或报错
             // 暂不处理自动分配批次逻辑，假设业务层传入了批次或者允许无批次扣减(如果不需要批次管理)
        }

        recordLog(skuId, warehouseId, batchNo, "OUTBOUND", qty, stock.getAvailableQty(), stock.getAvailableQty(), "REDUCE_STOCK", null, null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void increaseStock(Long skuId, Long warehouseId, String batchNo, BigDecimal qty, Date prodDate, Date expDate) {
        if (qty == null || qty.compareTo(BigDecimal.ZERO) <= 0) {
             throw new ServiceException("增加数量必须大于0");
        }

        // 1. 增加总库存 (available += qty)
        InventoryStock stock = getOrCreateStock(skuId, warehouseId);
        BigDecimal before = stock.getAvailableQty();
        stock.setAvailableQty(stock.getAvailableQty().add(qty));
        stockMapper.updateById(stock);

        // 2. 增加批次库存
        if (batchNo != null && !batchNo.isEmpty()) {
            LambdaQueryWrapper<InventoryStockBatch> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(InventoryStockBatch::getSkuId, skuId)
                        .eq(InventoryStockBatch::getWarehouseId, warehouseId)
                        .eq(InventoryStockBatch::getBatchNo, batchNo);
            InventoryStockBatch batch = stockBatchMapper.selectOne(queryWrapper);
            if (batch == null) {
                batch = new InventoryStockBatch();
                batch.setSkuId(skuId);
                batch.setWarehouseId(warehouseId);
                batch.setBatchNo(batchNo);
                batch.setQty(qty);
                batch.setProductionDate(prodDate);
                batch.setExpireDate(expDate);
                stockBatchMapper.insert(batch);
            } else {
                batch.setQty(batch.getQty().add(qty));
                stockBatchMapper.updateById(batch);
            }
        }

        recordLog(skuId, warehouseId, batchNo, "INBOUND", qty, before, stock.getAvailableQty(), "INCREASE_STOCK", null, null);
    }

    @Override
    public void reserveStock(Long skuId, Long warehouseId, BigDecimal qty) {
        // 暂复用 lockStock
        lockStock(skuId, warehouseId, qty);
    }

    private InventoryStock getOrCreateStock(Long skuId, Long warehouseId) {
        LambdaQueryWrapper<InventoryStock> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(InventoryStock::getSkuId, skuId)
                    .eq(InventoryStock::getWarehouseId, warehouseId);
        InventoryStock stock = stockMapper.selectOne(queryWrapper);
        if (stock == null) {
            stock = new InventoryStock();
            stock.setSkuId(skuId);
            stock.setWarehouseId(warehouseId);
            stock.setAvailableQty(BigDecimal.ZERO);
            stock.setLockedQty(BigDecimal.ZERO);
            stock.setVersion(0L);
            stockMapper.insert(stock);
        }
        return stock;
    }

    private void recordLog(Long skuId, Long warehouseId, String batchNo, String type, BigDecimal changeQty, 
                           BigDecimal beforeQty, BigDecimal afterQty, String bizType, Long bizId, String bizNo) {
        InventoryStockLog log = new InventoryStockLog();
        log.setSkuId(skuId);
        log.setWarehouseId(warehouseId);
        log.setBatchNo(batchNo);
        log.setChangeType(type);
        log.setChangeQty(changeQty);
        log.setBeforeQty(beforeQty);
        log.setAfterQty(afterQty);
        log.setBizType(bizType);
        log.setBizId(bizId);
        log.setBizNo(bizNo);
        stockLogMapper.insert(log);
    }
}
