package com.inspur.agriculture.inventory.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.agriculture.inventory.domain.InventoryOutbound;
import com.inspur.agriculture.inventory.domain.InventoryOutboundDetail;
import com.inspur.agriculture.inventory.domain.InventoryStock;
import com.inspur.agriculture.inventory.domain.InventoryStockBatch;
import com.inspur.agriculture.inventory.domain.InventoryWarehouse;
import com.inspur.agriculture.inventory.mapper.InventoryOutboundDetailMapper;
import com.inspur.agriculture.inventory.mapper.InventoryOutboundMapper;
import com.inspur.agriculture.inventory.mapper.InventoryStockBatchMapper;
import com.inspur.agriculture.inventory.mapper.InventoryStockMapper;
import com.inspur.agriculture.inventory.service.IInventoryCoreService;
import com.inspur.agriculture.inventory.service.IInventoryOutboundDetailService;
import com.inspur.agriculture.inventory.service.IInventoryOutboundService;
import com.inspur.agriculture.inventory.service.IInventoryWarehouseService;
import com.inspur.common.exception.ServiceException;
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createOutbound(InventoryOutbound outbound) {
        if (outbound == null) {
            throw new ServiceException("Outbound order cannot be null.");
        }
        if (outbound.getWarehouseCode() == null || outbound.getWarehouseCode().isEmpty()) {
            throw new ServiceException("Warehouse code cannot be empty.");
        }
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
        
        existOutbound.setType(outbound.getType());
        existOutbound.setWarehouseCode(outbound.getWarehouseCode());
        existOutbound.setReceiverType(outbound.getReceiverType());
        existOutbound.setReceiver(outbound.getReceiver());
        existOutbound.setBizNo(outbound.getBizNo());
        existOutbound.setOperator(outbound.getOperator());
        existOutbound.setOrderDate(outbound.getOrderDate());
        existOutbound.setRemark(outbound.getRemark());
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

        existOutbound.setAuditBy(outbound.getAuditBy());
        existOutbound.setAuditTime(new Date());
        existOutbound.setAuditComment(outbound.getAuditComment());

        if ("APPROVED".equals(outbound.getStatus())) {
            existOutbound.setStatus("APPROVED");
            this.updateById(existOutbound);

            LambdaQueryWrapper<InventoryOutboundDetail> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(InventoryOutboundDetail::getOutboundId, outbound.getId());
            List<InventoryOutboundDetail> details = detailMapper.selectList(queryWrapper);

            InventoryWarehouse warehouse = warehouseService.selectWarehouseByCode(existOutbound.getWarehouseCode());
            if (warehouse == null) {
                throw new ServiceException("Warehouse not found: " + existOutbound.getWarehouseCode());
            }

            validateOutboundAvailability(warehouse, details);

            for (InventoryOutboundDetail detail : details) {
                BigDecimal qtyKg = convertToKg(detail.getQty(), detail.getUnit(), "Outbound detail quantity");
                coreService.lockStock(detail.getProductId(), existOutbound.getWarehouseCode(), qtyKg);
                coreService.reduceStock(detail.getProductId(), existOutbound.getWarehouseCode(), detail.getBatchNo(), qtyKg);
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

        Date now = new Date();
        for (InventoryOutboundDetail detail : details) {
            if (detail == null) {
                throw new ServiceException("Outbound detail cannot be null.");
            }

            Date expireDate = detail.getExpireDate();
            if (expireDate != null && expireDate.before(now)) {
                throw new ServiceException("Outbound item has expired. Batch: " + safeString(detail.getBatchNo()));
            }

            BigDecimal qtyKg = convertToKg(detail.getQty(), detail.getUnit(), "Outbound detail quantity");
            InventoryStock stock = getStockByProduct(warehouse.getId(), detail.getProductId());
            if (stock == null) {
                throw new ServiceException("No stock available for product: " + detail.getProductId());
            }
            BigDecimal available = stock.getAvailableQty() != null ? stock.getAvailableQty() : BigDecimal.ZERO;
            if (available.compareTo(qtyKg) < 0) {
                throw new ServiceException("Insufficient available stock for product: " + detail.getProductId() + ". Available: " + available + " KG.");
            }

            if (detail.getBatchNo() != null && !detail.getBatchNo().isEmpty()) {
                InventoryStockBatch batch = getBatchByProduct(warehouse.getId(), detail.getProductId(), detail.getBatchNo());
                if (batch == null) {
                    throw new ServiceException("Batch not found: " + detail.getBatchNo());
                }
                if (batch.getExpireDate() != null && batch.getExpireDate().before(now)) {
                    throw new ServiceException("Batch has expired: " + detail.getBatchNo());
                }
                BigDecimal batchQty = batch.getQty() != null ? batch.getQty() : BigDecimal.ZERO;
                if (batchQty.compareTo(qtyKg) < 0) {
                    throw new ServiceException("Insufficient batch stock for batch: " + detail.getBatchNo() + ". Available: " + batchQty + " KG.");
                }
            }
        }
    }

    private InventoryStock getStockByProduct(Long warehouseId, Long productId) {
        LambdaQueryWrapper<InventoryStock> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InventoryStock::getWarehouseId, warehouseId)
                .eq(InventoryStock::getProductId, productId);
        return stockMapper.selectOne(wrapper);
    }

    private InventoryStockBatch getBatchByProduct(Long warehouseId, Long productId, String batchNo) {
        LambdaQueryWrapper<InventoryStockBatch> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InventoryStockBatch::getWarehouseId, warehouseId)
                .eq(InventoryStockBatch::getProductId, productId)
                .eq(InventoryStockBatch::getBatchNo, batchNo);
        return stockBatchMapper.selectOne(wrapper);
    }

    private BigDecimal convertToKg(BigDecimal qty, String unit, String fieldName) {
        if (qty == null) {
            throw new ServiceException(fieldName + " cannot be null.");
        }
        if (qty.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ServiceException(fieldName + " must be greater than 0.");
        }

        String normalizedUnit = unit == null ? "" : unit.trim().toUpperCase(Locale.ROOT);
        if ("KG".equals(normalizedUnit)) {
            return qty;
        }
        if ("G".equals(normalizedUnit)) {
            return qty.divide(new BigDecimal("1000"), 6, RoundingMode.HALF_UP);
        }
        if ("ML".equals(normalizedUnit)) {
            return qty.divide(new BigDecimal("1000"), 6, RoundingMode.HALF_UP);
        }
        throw new ServiceException("Unsupported unit: " + unit + ". Only KG, g, and ML are supported.");
    }

    private String safeString(String value) {
        return value == null ? "-" : value;
    }
}
