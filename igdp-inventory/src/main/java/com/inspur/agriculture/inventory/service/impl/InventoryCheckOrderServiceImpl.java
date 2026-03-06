package com.inspur.agriculture.inventory.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.agriculture.inventory.domain.InventoryCheckOrder;
import com.inspur.agriculture.inventory.domain.InventoryCheckOrderDetail;
import com.inspur.agriculture.inventory.mapper.InventoryCheckOrderDetailMapper;
import com.inspur.agriculture.inventory.mapper.InventoryCheckOrderMapper;
import com.inspur.agriculture.inventory.service.IInventoryCheckOrderDetailService;
import com.inspur.agriculture.inventory.service.IInventoryCheckOrderService;
import com.inspur.agriculture.inventory.service.IInventoryCoreService;
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
    private IInventoryCoreService coreService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createCheckOrder(InventoryCheckOrder checkOrder) {
        if (checkOrder == null) {
            throw new ServiceException("盘点单不能为空");
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
            throw new ServiceException("盘点单信息不完整");
        }
        InventoryCheckOrder existOrder = this.getById(checkOrder.getId());
        if (existOrder == null) {
            throw new ServiceException("盘点单不存在");
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
            throw new ServiceException("盘点单不存在");
        }
        
        existOrder.setStatus("FINISHED"); // 或 AUDITED
        existOrder.setAuditBy(checkOrder.getAuditBy());
        existOrder.setAuditTime(new Date());
        existOrder.setAuditComment(checkOrder.getAuditComment());
        this.updateById(existOrder);

        // 调整库存
        LambdaQueryWrapper<InventoryCheckOrderDetail> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(InventoryCheckOrderDetail::getCheckId, checkOrder.getId());
        List<InventoryCheckOrderDetail> details = detailMapper.selectList(queryWrapper);

        for (InventoryCheckOrderDetail detail : details) {
            if ("PROFIT".equals(detail.getDiffType())) {
                // 盘盈入库
                coreService.increaseStock(detail.getSkuId(), existOrder.getWarehouseId(), detail.getBatchNo(), detail.getDiffQty(), null, null);
            } else if ("LOSS".equals(detail.getDiffType())) {
                // 盘亏出库 (直接扣减，不走锁定流程)
                // 注意：reduceStock默认扣减锁定库存，这里需要特殊处理或者先锁定再扣减
                // 简化处理：先锁定再扣减
                coreService.lockStock(detail.getSkuId(), existOrder.getWarehouseId(), detail.getDiffQty());
                coreService.reduceStock(detail.getSkuId(), existOrder.getWarehouseId(), detail.getBatchNo(), detail.getDiffQty());
            }
        }
        return true;
    }
}
