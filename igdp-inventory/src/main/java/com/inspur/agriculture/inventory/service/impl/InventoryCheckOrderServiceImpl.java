package com.inspur.agriculture.inventory.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.agriculture.inventory.domain.InventoryCheckOrder;
import com.inspur.agriculture.inventory.domain.InventoryCheckOrderDetail;
import com.inspur.agriculture.inventory.domain.InventoryStock;
import com.inspur.agriculture.inventory.domain.InventoryWarehouse;
import com.inspur.agriculture.inventory.mapper.InventoryCheckOrderDetailMapper;
import com.inspur.agriculture.inventory.mapper.InventoryCheckOrderMapper;
import com.inspur.agriculture.inventory.mapper.InventoryStockMapper;
import com.inspur.agriculture.inventory.service.IInventoryCheckOrderService;
import com.inspur.agriculture.inventory.service.IInventoryWarehouseService;
import com.inspur.common.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
public class InventoryCheckOrderServiceImpl extends ServiceImpl<InventoryCheckOrderMapper, InventoryCheckOrder> implements IInventoryCheckOrderService {

    @Autowired
    private InventoryCheckOrderDetailMapper detailMapper;

    @Autowired
    private IInventoryWarehouseService warehouseService;

    @Autowired
    private InventoryStockMapper stockMapper;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createCheckOrder(InventoryCheckOrder checkOrder) {
        if (checkOrder == null) {
            throw new ServiceException("Inventory check order cannot be null.");
        }
        checkOrder.setStatus("DRAFT");
        checkOrder.setCreateTime(LocalDateTime.now());
        this.save(checkOrder);

        List<InventoryCheckOrderDetail> details = checkOrder.getDetailList();
        if (details != null && !details.isEmpty()) {
            for (InventoryCheckOrderDetail detail : details) {
                detail.setCheckId(checkOrder.getId());
                detailMapper.insert(detail);
            }
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean recordCheckResult(InventoryCheckOrder checkOrder) {
        if (checkOrder == null || checkOrder.getId() == null) {
            throw new ServiceException("Inventory check order information is incomplete.");
        }
        InventoryCheckOrder existOrder = this.getById(checkOrder.getId());
        if (existOrder == null) {
            throw new ServiceException("Inventory check order not found.");
        }

        existOrder.setStatus("CHECKING");
        existOrder.setCheckDate(new Date());
        existOrder.setCheckBy(checkOrder.getCheckBy());
        existOrder.setRemark(checkOrder.getRemark());
        this.updateById(existOrder);

        List<InventoryCheckOrderDetail> details = checkOrder.getDetailList();
        if (details != null && !details.isEmpty()) {
            for (InventoryCheckOrderDetail detail : details) {
                if (detail.getId() != null) {
                    // 计算差异
                    BigDecimal bookQty = detail.getBookQty() != null ? detail.getBookQty() : BigDecimal.ZERO;
                    BigDecimal realQty = detail.getRealQty() != null ? detail.getRealQty() : BigDecimal.ZERO;
                    BigDecimal diffQty = realQty.subtract(bookQty);

                    detail.setDiffQty(diffQty.abs());
                    if (diffQty.compareTo(BigDecimal.ZERO) > 0) {
                        detail.setDiffType("PROFIT"); // 盘盈
                    } else if (diffQty.compareTo(BigDecimal.ZERO) < 0) {
                        detail.setDiffType("LOSS"); // 盘亏
                    } else {
                        detail.setDiffType("NORMAL"); // 正常
                    }

                    detailMapper.updateById(detail);
                }
            }
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean auditCheckOrder(InventoryCheckOrder checkOrder) {
        InventoryCheckOrder existOrder = this.getById(checkOrder.getId());
        if (existOrder == null) {
            throw new ServiceException("Inventory check order not found.");
        }

        existOrder.setStatus("FINISHED"); // 或 AUDITED

        existOrder.setStatus("FINISHED");
        existOrder.setAuditBy(checkOrder.getAuditBy());
        existOrder.setAuditTime(new Date());
        existOrder.setAuditComment(checkOrder.getAuditComment());
        this.updateById(existOrder);

        LambdaQueryWrapper<InventoryCheckOrderDetail> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(InventoryCheckOrderDetail::getCheckId, checkOrder.getId());
        List<InventoryCheckOrderDetail> details = detailMapper.selectList(queryWrapper);

        Long warehouseId = resolveWarehouseId(existOrder.getWarehouseCode());
        for (InventoryCheckOrderDetail detail : details) {
            InventoryStock stock = resolveStock(warehouseId, detail.getProductId());
            BigDecimal qty = detail.getDiffQty() == null ? null : detail.getDiffQty().abs();
            if (qty == null || qty.compareTo(BigDecimal.ZERO) <= 0) {
                throw new ServiceException("Invalid diff quantity.");
            }
            if ("PROFIT".equals(detail.getDiffType())) {
                // 盘盈入库
                coreService.increaseStock(detail.getProductId(), existOrder.getWarehouseId(), detail.getBatchNo(), detail.getDiffQty(), null, null);
                stock.setAvailableQty(stock.getAvailableQty().add(qty));
            } else if ("LOSS".equals(detail.getDiffType())) {
                // 盘亏出库 (直接扣减，不走锁定流程)
                // 注意：reduceStock默认扣减锁定库存，这里需要特殊处理或者先锁定再扣减
                // 简化处理：先锁定再扣减
                coreService.lockStock(detail.getProductId(), existOrder.getWarehouseId(), detail.getDiffQty());
                coreService.reduceStock(detail.getProductId(), existOrder.getWarehouseId(), detail.getBatchNo(), detail.getDiffQty());
                if (stock.getAvailableQty().compareTo(qty) < 0) {
                    throw new ServiceException("Insufficient stock to reduce.");
                }
                stock.setAvailableQty(stock.getAvailableQty().subtract(qty));
            }
            stockMapper.updateById(stock);
        }
        return true;
    }

    private Long resolveWarehouseId(String warehouseCode) {
        if (warehouseCode == null || warehouseCode.trim().isEmpty()) {
            throw new ServiceException("Warehouse code is required.");
        }
        InventoryWarehouse warehouse = warehouseService.selectWarehouseByCode(warehouseCode.trim());
        if (warehouse == null || warehouse.getId() == null) {
            throw new ServiceException("Warehouse not found: " + warehouseCode);
        }
        return warehouse.getId();
    }

    private InventoryStock resolveStock(Long warehouseId, Long productId) {
        if (productId == null) {
            throw new ServiceException("Product ID is required.");
        }
        LambdaQueryWrapper<InventoryStock> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InventoryStock::getWarehouseId, warehouseId)
                .eq(InventoryStock::getProductId, productId);
        List<InventoryStock> stocks = stockMapper.selectList(wrapper);
        if (stocks == null || stocks.isEmpty()) {
            throw new ServiceException("Inventory stock not found for productId=" + productId + ", warehouseId=" + warehouseId);
        }
        if (stocks.size() > 1) {
            throw new ServiceException("Inventory stock not unique for productId=" + productId + ", warehouseId=" + warehouseId);
        }
        return stocks.get(0);
    }

}
