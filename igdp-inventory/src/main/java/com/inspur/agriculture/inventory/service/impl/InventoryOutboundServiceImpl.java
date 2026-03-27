package com.inspur.agriculture.inventory.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.agriculture.inventory.domain.InventoryOutbound;
import com.inspur.agriculture.inventory.domain.InventoryOutboundDetail;
import com.inspur.agriculture.inventory.domain.InventoryProduct;
import com.inspur.agriculture.inventory.domain.InventoryStock;
import com.inspur.agriculture.inventory.domain.InventoryStockBatch;
import com.inspur.agriculture.inventory.domain.InventoryWarehouse;
import com.inspur.agriculture.inventory.mapper.InventoryOutboundDetailMapper;
import com.inspur.agriculture.inventory.mapper.InventoryOutboundMapper;
import com.inspur.agriculture.inventory.mapper.InventoryProductMapper;
import com.inspur.agriculture.inventory.mapper.InventoryStockBatchMapper;
import com.inspur.agriculture.inventory.mapper.InventoryStockMapper;
import com.inspur.agriculture.inventory.service.IInventoryCoreService;
import com.inspur.agriculture.inventory.service.IInventoryOutboundDetailService;
import com.inspur.agriculture.inventory.service.IInventoryOutboundService;
import com.inspur.agriculture.inventory.service.IInventoryWarehouseService;
import com.inspur.common.exception.ServiceException;
import com.inspur.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Service
public class InventoryOutboundServiceImpl extends ServiceImpl<InventoryOutboundMapper, InventoryOutbound> implements IInventoryOutboundService {

    @Autowired
    private InventoryOutboundDetailMapper detailMapper;
    
    @Autowired
    private IInventoryCoreService coreService;

    @Autowired
    private IInventoryWarehouseService warehouseService;

    @Autowired
    private InventoryStockMapper stockMapper;

    @Autowired
    private InventoryStockBatchMapper stockBatchMapper;

    @Autowired
    private InventoryProductMapper productMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createOutbound(InventoryOutbound outbound) {
        if (outbound == null) {
            throw new ServiceException("Outbound order cannot be null.");
        }
        if (outbound.getWarehouseCode() == null || outbound.getWarehouseCode().isEmpty()) {
            throw new ServiceException("Warehouse code cannot be empty.");
        }
        
        // 自动设置出库类型为GENERAL（一般出库）
        outbound.setType("GENERAL");
        
        // 自动设置操作人为当前登录用户
        try {
            String currentUsername = SecurityUtils.getUsername();
            outbound.setOperator(currentUsername);
        } catch (Exception e) {
            if (outbound.getOperator() == null || outbound.getOperator().isEmpty()) {
                outbound.setOperator("SYSTEM");
            }
        }
        
        // 自动设置出库时间为当前时间
        outbound.setOrderDate(new Date());
        
        outbound.setStatus("DRAFT");
        outbound.setCreateTime(LocalDateTime.now());
        this.save(outbound);
        
        List<InventoryOutboundDetail> details = outbound.getDetailList();
        if (details != null && !details.isEmpty()) {
            for (InventoryOutboundDetail detail : details) {
                detail.setOutboundId(outbound.getId());
                detailMapper.insert(detail);
            }
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateOutbound(InventoryOutbound outbound) {
        if (outbound == null || outbound.getId() == null) {
            throw new ServiceException("Outbound order cannot be null.");
        }
        
        InventoryOutbound existOutbound = this.getById(outbound.getId());
        if (existOutbound == null) {
            throw new ServiceException("Outbound order not found.");
        }
        if (!"DRAFT".equals(existOutbound.getStatus()) && !"SUBMITTED".equals(existOutbound.getStatus())) {
            throw new ServiceException("Only draft or submitted outbound orders can be updated.");
        }
        
        existOutbound.setType(outbound.getType() != null ? outbound.getType() : existOutbound.getType());
        existOutbound.setWarehouseCode(outbound.getWarehouseCode() != null ? outbound.getWarehouseCode() : existOutbound.getWarehouseCode());
        existOutbound.setReceiverType(outbound.getReceiverType() != null ? outbound.getReceiverType() : existOutbound.getReceiverType());
        existOutbound.setReceiver(outbound.getReceiver() != null ? outbound.getReceiver() : existOutbound.getReceiver());
        existOutbound.setBizNo(outbound.getBizNo() != null ? outbound.getBizNo() : existOutbound.getBizNo());
        existOutbound.setOperator(outbound.getOperator() != null ? outbound.getOperator() : existOutbound.getOperator());
        existOutbound.setOrderDate(outbound.getOrderDate() != null ? outbound.getOrderDate() : existOutbound.getOrderDate());
        existOutbound.setRemark(outbound.getRemark() != null ? outbound.getRemark() : existOutbound.getRemark());
        this.updateById(existOutbound);
        
        LambdaQueryWrapper<InventoryOutboundDetail> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(InventoryOutboundDetail::getOutboundId, outbound.getId());
        detailMapper.delete(queryWrapper);
        
        List<InventoryOutboundDetail> details = outbound.getDetailList();
        if (details != null && !details.isEmpty()) {
            for (InventoryOutboundDetail detail : details) {
                detail.setOutboundId(outbound.getId());
                detailMapper.insert(detail);
            }
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean submitOutbound(Long id) {
        InventoryOutbound outbound = this.getById(id);
        if (outbound == null) {
            throw new ServiceException("Outbound order not found.");
        }
        if (!"DRAFT".equals(outbound.getStatus())) {
            throw new ServiceException("Only draft outbound orders can be submitted.");
        }
        outbound.setStatus("SUBMITTED");
        return this.updateById(outbound);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean auditOutbound(InventoryOutbound outbound) {
        InventoryOutbound existOutbound = this.getById(outbound.getId());
        if (existOutbound == null) {
            throw new ServiceException("Outbound order not found.");
        }
        if (!"SUBMITTED".equals(existOutbound.getStatus())) {
            throw new ServiceException("Only submitted outbound orders can be audited.");
        }

        existOutbound.setAuditBy(SecurityUtils.getUsername());
        existOutbound.setAuditTime(new Date());
        existOutbound.setAuditComment(outbound.getAuditComment());

        if ("APPROVED".equals(outbound.getStatus())) {
            existOutbound.setStatus("APPROVED");
            this.updateById(existOutbound);

            LambdaQueryWrapper<InventoryOutboundDetail> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(InventoryOutboundDetail::getOutboundId, outbound.getId());
            List<InventoryOutboundDetail> details = detailMapper.selectList(queryWrapper);
            for (InventoryOutboundDetail detail : details) {
                detail.setProductId(resolveProductId(detail));
            }

            InventoryWarehouse warehouse = warehouseService.selectWarehouseByCode(existOutbound.getWarehouseCode());
            if (warehouse == null) {
                throw new ServiceException("Warehouse not found: " + existOutbound.getWarehouseCode());
            }

            // 暂时取消库存容量校验
            // validateOutboundAvailability(warehouse, details);

            for (InventoryOutboundDetail detail : details) {
                BigDecimal qty = validateQuantity(detail.getQty(), "Outbound detail quantity");
                coreService.lockStock(detail.getProductId(), existOutbound.getWarehouseCode(), qty, detail.getMainCategory(), detail.getSubCategory(), detail.getProductName());
                if (detail.getBatchNo() != null && !detail.getBatchNo().isEmpty()) {
                    InventoryStock stock = getStockByProductAndWarehouse(detail.getProductId(), warehouse.getId());
                    if (stock == null) {
                        throw new ServiceException("No stock available for this product in the selected warehouse.");
                    }
                    InventoryStockBatch batch = getBatchByStockId(stock.getId(), detail.getBatchNo());
                    if (batch == null) {
                        throw new ServiceException("Batch not found: " + detail.getBatchNo());
                    }
                    BigDecimal batchQty = validateQuantity(detail.getQty(), "Outbound detail quantity");
                    coreService.reduceStockWithBatch(detail.getProductId(), existOutbound.getWarehouseCode(), detail.getBatchNo(), qty, batchQty);
                } else {
                    coreService.reduceStock(detail.getProductId(), existOutbound.getWarehouseCode(), null, qty);
                }
            }
        } else if ("REJECTED".equals(outbound.getStatus())) {
            existOutbound.setStatus("REJECTED");
            this.updateById(existOutbound);
        } else {
            throw new ServiceException("Invalid audit status.");
        }
        
        return true;
    }

    @Override
    public InventoryOutbound selectOutboundWithWarehouse(Long id) {
        InventoryOutbound outbound = baseMapper.selectOutboundWithWarehouse(id);
        if (outbound != null) {
            LambdaQueryWrapper<InventoryOutboundDetail> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(InventoryOutboundDetail::getOutboundId, id);
            List<InventoryOutboundDetail> details = detailMapper.selectList(queryWrapper);
            outbound.setDetailList(details);
        }
        return outbound;
    }

    @Override
    public List<InventoryOutbound> selectOutboundListWithWarehouse(InventoryOutbound outbound) {
        List<InventoryOutbound> list = baseMapper.selectOutboundListWithWarehouse(outbound);
        if (list != null && !list.isEmpty()) {
            for (InventoryOutbound item : list) {
                LambdaQueryWrapper<InventoryOutboundDetail> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(InventoryOutboundDetail::getOutboundId, item.getId());
                List<InventoryOutboundDetail> details = detailMapper.selectList(queryWrapper);
                item.setDetailList(details);
            }
        }
        return list;
    }

    private void validateOutboundAvailability(InventoryWarehouse warehouse, List<InventoryOutboundDetail> details) {
        if (details == null || details.isEmpty()) {
            throw new ServiceException("Outbound details cannot be empty.");
        }

        BigDecimal capacity = warehouse.getCapacity();
        if (capacity != null && capacity.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal currentTotal = getCurrentWarehouseTotalQty(warehouse.getId());
            if (currentTotal.compareTo(capacity) > 0) {
                throw new ServiceException("Current warehouse stock exceeds capacity. Capacity: " + capacity + ", Current: " + currentTotal + ".");
            }
        }

        Date now = new Date();
        for (InventoryOutboundDetail detail : details) {
            if (detail == null) {
                throw new ServiceException("Outbound detail cannot be null.");
            }

            Date expireDate = detail.getExpireDate();
            if (expireDate != null && expireDate.before(now)) {
                throw new ServiceException("Outbound item has expired. Batch: " + safeString(detail.getBatchNo()));
            }

            BigDecimal qty = validateQuantity(detail.getQty(), "Outbound detail quantity");
            InventoryStock stock = getStockByProductAndWarehouse(detail.getProductId(), warehouse.getId());
            if (stock == null) {
                throw new ServiceException("No stock available for this product in the selected warehouse.");
            }
            BigDecimal available = stock.getAvailableQty() != null ? stock.getAvailableQty() : BigDecimal.ZERO;
            if (available.compareTo(qty) < 0) {
                throw new ServiceException("Insufficient available stock for this product. Available: " + available + ".");
            }

            if (detail.getBatchNo() != null && !detail.getBatchNo().isEmpty()) {
                InventoryStockBatch batch = getBatchByStockId(stock.getId(), detail.getBatchNo());
                if (batch == null) {
                    throw new ServiceException("Batch not found: " + detail.getBatchNo());
                }
                if (batch.getExpireDate() != null && batch.getExpireDate().before(now)) {
                    throw new ServiceException("Batch has expired: " + detail.getBatchNo());
                }
                BigDecimal batchQty = batch.getQty() != null ? batch.getQty() : BigDecimal.ZERO;
                BigDecimal requiredBatchQty = validateQuantity(detail.getQty(), "Outbound detail quantity");
                if (batchQty.compareTo(requiredBatchQty) < 0) {
                    throw new ServiceException("Insufficient batch stock for batch: " + detail.getBatchNo() + ". Available: " + batchQty + " " + safeString(batch.getUnit()) + ".");
                }
            }
        }
    }

    private InventoryStock getStockByProductAndWarehouse(Long productId, Long warehouseId) {
        if (productId == null) {
            throw new ServiceException("Product ID cannot be empty.");
        }
        LambdaQueryWrapper<InventoryStock> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InventoryStock::getWarehouseId, warehouseId)
                .eq(InventoryStock::getProductId, productId);
        return stockMapper.selectOne(wrapper);
    }

    private BigDecimal getCurrentWarehouseTotalQty(Long warehouseId) {
        LambdaQueryWrapper<InventoryStock> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InventoryStock::getWarehouseId, warehouseId);
        List<InventoryStock> stocks = stockMapper.selectList(wrapper);
        BigDecimal total = BigDecimal.ZERO;
        if (stocks != null) {
            for (InventoryStock stock : stocks) {
                BigDecimal available = stock.getAvailableQty() != null ? stock.getAvailableQty() : BigDecimal.ZERO;
                BigDecimal locked = stock.getLockedQty() != null ? stock.getLockedQty() : BigDecimal.ZERO;
                total = total.add(available).add(locked);
            }
        }
        return total;
    }

    private InventoryStockBatch getBatchByStockId(Long stockId, String batchNo) {
    LambdaQueryWrapper<InventoryStockBatch> wrapper = new LambdaQueryWrapper<>();
    wrapper.eq(InventoryStockBatch::getStockId, stockId)
            .eq(InventoryStockBatch::getBatchNo, batchNo);
    return stockBatchMapper.selectOne(wrapper);
}

    private BigDecimal validateQuantity(BigDecimal qty, String fieldName) {
        if (qty == null) {
            throw new ServiceException(fieldName + " cannot be null.");
        }
        if (qty.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ServiceException(fieldName + " must be greater than 0.");
        }
        return qty;
    }

    private String safeString(String value) {
        return value == null ? "-" : value;
    }

    private Long resolveProductId(InventoryOutboundDetail detail) {
        if (detail == null) {
            throw new ServiceException("Outbound detail cannot be null.");
        }
        if (detail.getProductId() != null) {
            return detail.getProductId();
        }
        String mainCategory = detail.getMainCategory();
        String subCategory = detail.getSubCategory();
        String productName = detail.getProductName();
        if (mainCategory == null || mainCategory.isEmpty() || subCategory == null || subCategory.isEmpty()) {
            throw new ServiceException("Product ID is required when mainCategory or subCategory is missing.");
        }

        LambdaQueryWrapper<InventoryProduct> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InventoryProduct::getMainCategory, mainCategory)
                .eq(InventoryProduct::getSubCategory, subCategory);
        if (productName != null && !productName.isEmpty()) {
            wrapper.eq(InventoryProduct::getProductName, productName);
        }
        wrapper.last("limit 1");
        InventoryProduct product = productMapper.selectOne(wrapper);
        if (product == null) {
            throw new ServiceException("Product not found for category: " + mainCategory + " / " + subCategory + (productName != null ? " / " + productName : ""));
        }
        return product.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteOutbound(Long id) {
        InventoryOutbound outbound = this.getById(id);
        if (outbound == null) {
            throw new ServiceException("Outbound order not found.");
        }
        if (!"DRAFT".equals(outbound.getStatus()) && !"SUBMITTED".equals(outbound.getStatus())) {
            throw new ServiceException("Only draft or submitted orders can be deleted.");
        }
        // 删除明细
        LambdaQueryWrapper<InventoryOutboundDetail> detailWrapper = new LambdaQueryWrapper<>();
        detailWrapper.eq(InventoryOutboundDetail::getOutboundId, id);
        detailMapper.delete(detailWrapper);
        // 删除主单
        return this.removeById(id);
    }
}
