package com.inspur.agriculture.inventory.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.inspur.agriculture.inventory.domain.InventoryStock;
import com.inspur.agriculture.inventory.domain.InventoryStockBatch;
import com.inspur.agriculture.inventory.domain.InventoryStockLog;
import com.inspur.agriculture.inventory.domain.InventoryWarehouse;
import com.inspur.agriculture.inventory.mapper.InventoryStockBatchMapper;
import com.inspur.agriculture.inventory.mapper.InventoryStockLogMapper;
import com.inspur.agriculture.inventory.mapper.InventoryStockMapper;
import com.inspur.agriculture.inventory.service.IInventoryCoreService;
import com.inspur.agriculture.inventory.service.IInventoryWarehouseService;
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

    @Autowired
    private IInventoryWarehouseService warehouseService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void lockStock(Long productId, String warehouseCode, BigDecimal qty) {
        Long warehouseId = getWarehouseIdByCode(warehouseCode);
        if (qty == null || qty.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ServiceException("Lock quantity must be greater than 0.");
        }

        InventoryStock stock = getOrCreateStock(productId, warehouseId);

        if (stock.getAvailableQty().compareTo(qty) < 0) {
            throw new ServiceException("Insufficient available stock. Current available: " + stock.getAvailableQty());
        }

        int rows = stockMapper.updateStockOptimistic(stock.getId(), qty, stock.getVersion());
        if (rows == 0) {
            throw new ServiceException("Stock is busy, please retry.");
        }

        recordLog(productId, warehouseId, null, "LOCK", qty, stock.getAvailableQty(), stock.getAvailableQty().subtract(qty), "LOCK_STOCK", null, null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void releaseStock(Long productId, String warehouseCode, BigDecimal qty) {
        Long warehouseId = getWarehouseIdByCode(warehouseCode);
        if (qty == null || qty.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ServiceException("Release quantity must be greater than 0.");
        }

        InventoryStock stock = getOrCreateStock(productId, warehouseId);

        stock = stockMapper.selectById(stock.getId());
        if (stock.getLockedQty().compareTo(qty) < 0) {
             throw new ServiceException("Release quantity is greater than locked quantity.");
        }
        
        stock.setAvailableQty(stock.getAvailableQty().add(qty));
        stock.setLockedQty(stock.getLockedQty().subtract(qty));
        int rows = stockMapper.updateById(stock);
        if (rows == 0) {
            throw new ServiceException("Stock update failed (optimistic lock), please retry.");
        }

        recordLog(productId, warehouseId, null, "RELEASE", qty, stock.getAvailableQty().subtract(qty), stock.getAvailableQty(), "RELEASE_STOCK", null, null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void reduceStock(Long productId, String warehouseCode, String batchNo, BigDecimal qty) {
        reduceStockWithBatch(productId, warehouseCode, batchNo, qty, qty);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void reduceStockWithBatch(Long productId, String warehouseCode, String batchNo, BigDecimal stockQtyKg, BigDecimal batchQty) {
        Long warehouseId = getWarehouseIdByCode(warehouseCode);
        if (stockQtyKg == null || stockQtyKg.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ServiceException("Reduce quantity must be greater than 0.");
        }

        InventoryStock stock = getOrCreateStock(productId, warehouseId);
        if (stock.getLockedQty().compareTo(stockQtyKg) < 0) {
            throw new ServiceException("Reduce quantity is greater than locked quantity (please lock first).");
        }
        stock.setLockedQty(stock.getLockedQty().subtract(stockQtyKg));
        int rows = stockMapper.updateById(stock);
        if (rows == 0) {
            throw new ServiceException("Stock update failed, please retry.");
        }

        if (batchNo != null && !batchNo.isEmpty()) {
            LambdaQueryWrapper<InventoryStockBatch> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(InventoryStockBatch::getStockId, stock.getId())
                    .eq(InventoryStockBatch::getBatchNo, batchNo);
            InventoryStockBatch batch = stockBatchMapper.selectOne(queryWrapper);
            if (batch == null || batch.getQty() == null) {
                throw new ServiceException("Batch stock not found.");
            }
            if (batchQty == null || batchQty.compareTo(BigDecimal.ZERO) <= 0) {
                throw new ServiceException("Batch reduce quantity must be greater than 0.");
            }
            if (batch.getQty().compareTo(batchQty) < 0) {
                throw new ServiceException("Insufficient batch stock.");
            }
            BigDecimal newQty = batch.getQty().subtract(batchQty);
            if (newQty.compareTo(BigDecimal.ZERO) == 0) {
                stockBatchMapper.deleteById(batch.getId());
            } else {
                batch.setQty(newQty);
                stockBatchMapper.updateById(batch);
            }
        }

        recordLog(productId, warehouseId, batchNo, "OUTBOUND", stockQtyKg, stock.getAvailableQty(), stock.getAvailableQty(), "REDUCE_STOCK", null, null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void increaseStock(Long productId, String warehouseCode, String batchNo, BigDecimal qty, Date prodDate, Date expDate) {
        increaseStock(productId, warehouseCode, batchNo, qty, prodDate, expDate, null, "AVAILABLE");
    }

    @Override
    public void increaseStock(Long productId, String warehouseCode, String batchNo, BigDecimal qty, Date prodDate, Date expDate, String qualityGrade, String stockStatus) {
        increaseStock(productId, warehouseCode, batchNo, qty, prodDate, expDate, qualityGrade, stockStatus, null, null);
    }

    @Override
    public void increaseStock(Long productId, String warehouseCode, String batchNo, BigDecimal qty, Date prodDate, Date expDate, String qualityGrade, String stockStatus, String mainCategory, String subCategory) {
        increaseStockWithBatch(productId, warehouseCode, batchNo, qty, qty, null, prodDate, expDate, qualityGrade, stockStatus, mainCategory, subCategory);
    }

    @Override
    public void increaseStockWithBatch(Long productId, String warehouseCode, String batchNo, BigDecimal stockQtyKg, BigDecimal batchQty, String batchUnit,
                                       Date prodDate, Date expDate, String qualityGrade, String stockStatus, String mainCategory, String subCategory) {
        Long warehouseId = getWarehouseIdByCode(warehouseCode);
        if (stockQtyKg == null || stockQtyKg.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ServiceException("Increase quantity must be greater than 0.");
        }

        InventoryStock stock = getOrCreateStock(productId, warehouseId);
        BigDecimal before = stock.getAvailableQty();
        stock.setAvailableQty(stock.getAvailableQty().add(stockQtyKg));
        if (qualityGrade != null) stock.setQualityGrade(qualityGrade);
        if (stockStatus != null) stock.setStockStatus(stockStatus);

        stockMapper.updateById(stock);

        if (batchNo != null && !batchNo.isEmpty()) {
            LambdaQueryWrapper<InventoryStockBatch> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(InventoryStockBatch::getStockId, stock.getId())
                    .eq(InventoryStockBatch::getBatchNo, batchNo);
            InventoryStockBatch batch = stockBatchMapper.selectOne(queryWrapper);
            if (batch == null) {
                batch = new InventoryStockBatch();
                batch.setProductId(productId);
                batch.setWarehouseId(warehouseId);
                batch.setStockId(stock.getId());
                batch.setBatchNo(batchNo);
                batch.setQty(batchQty != null ? batchQty : BigDecimal.ZERO);
                batch.setUnit(batchUnit);
                batch.setProductionDate(prodDate);
                batch.setExpireDate(expDate);
                batch.setQualityGrade(qualityGrade);
                batch.setStockStatus(stockStatus != null ? stockStatus : "AVAILABLE");
                batch.setMainCategory(mainCategory);
                batch.setSubCategory(subCategory);
                stockBatchMapper.insert(batch);
            } else {
                BigDecimal addQty = batchQty != null ? batchQty : BigDecimal.ZERO;
                batch.setQty(batch.getQty().add(addQty));
                if (batchUnit != null && !batchUnit.isEmpty()) batch.setUnit(batchUnit);
                if (mainCategory != null) batch.setMainCategory(mainCategory);
                if (subCategory != null) batch.setSubCategory(subCategory);
                stockBatchMapper.updateById(batch);
            }
        }

        recordLog(productId, warehouseId, batchNo, "INBOUND", stockQtyKg, before, stock.getAvailableQty(), "INCREASE_STOCK", null, null);
    }

    @Override
    public void reserveStock(Long productId, String warehouseCode, BigDecimal qty) {
        lockStock(productId, warehouseCode, qty);
    }

    private Long getWarehouseIdByCode(String warehouseCode) {
        if (warehouseCode == null || warehouseCode.isEmpty()) {
            throw new ServiceException("Warehouse code cannot be empty.");
        }
        InventoryWarehouse warehouse = warehouseService.selectWarehouseByCode(warehouseCode);
        if (warehouse == null) {
            throw new ServiceException("Warehouse not found: " + warehouseCode);
        }
        return warehouse.getId();
    }

    private InventoryStock getOrCreateStock(Long productId, Long warehouseId) {
        if (productId == null) {
            throw new ServiceException("Product ID cannot be empty.");
        }
        LambdaQueryWrapper<InventoryStock> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(InventoryStock::getWarehouseId, warehouseId)
                    .eq(InventoryStock::getProductId, productId);
        InventoryStock stock = stockMapper.selectOne(queryWrapper);
        if (stock == null) {
            stock = new InventoryStock();
            stock.setProductId(productId);
            stock.setWarehouseId(warehouseId);
            stock.setAvailableQty(BigDecimal.ZERO);
            stock.setLockedQty(BigDecimal.ZERO);
            stock.setVersion(0L);
            stockMapper.insert(stock);
        }
        return stock;
    }

    private void recordLog(Long productId, Long warehouseId, String batchNo, String type, BigDecimal changeQty, 
                           BigDecimal beforeQty, BigDecimal afterQty, String bizType, Long bizId, String bizNo) {
        InventoryStockLog log = new InventoryStockLog();
        log.setProductId(productId);
        log.setWarehouseId(warehouseId);
        log.setBatchNo(batchNo);
        log.setChangeType(type);
        log.setChangeQuantity(changeQty);
        log.setBeforeQuantity(beforeQty);
        log.setAfterQuantity(afterQty);
        log.setBizType(bizType);
        log.setBizId(bizId);
        log.setBizNo(bizNo);
        stockLogMapper.insert(log);
    }
}

