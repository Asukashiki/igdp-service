package com.inspur.agriculture.inventory.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.agriculture.inventory.domain.InventoryInbound;
import com.inspur.agriculture.inventory.domain.InventoryInboundDetail;
import com.inspur.agriculture.inventory.domain.InventoryStock;
import com.inspur.agriculture.inventory.domain.InventoryWarehouse;
import com.inspur.agriculture.inventory.mapper.InventoryInboundDetailMapper;
import com.inspur.agriculture.inventory.mapper.InventoryInboundMapper;
import com.inspur.agriculture.inventory.mapper.InventoryStockMapper;
import com.inspur.agriculture.inventory.service.IInventoryCoreService;
import com.inspur.agriculture.inventory.service.IInventoryInboundDetailService;
import com.inspur.agriculture.inventory.service.IInventoryInboundService;
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
public class InventoryInboundServiceImpl extends ServiceImpl<InventoryInboundMapper, InventoryInbound> implements IInventoryInboundService {

    @Autowired
    private InventoryInboundDetailMapper detailMapper;
    
    @Autowired
    private IInventoryCoreService coreService;

    @Autowired
    private IInventoryWarehouseService warehouseService;

    @Autowired
    private InventoryStockMapper stockMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createInbound(InventoryInbound inbound) {
        if (inbound == null) {
            throw new ServiceException("Inbound order cannot be null.");
        }
        if (inbound.getWarehouseCode() == null || inbound.getWarehouseCode().isEmpty()) {
            throw new ServiceException("Warehouse code cannot be empty.");
        }
        
        inbound.setStatus("DRAFT");
        inbound.setCreateTime(LocalDateTime.now());
        this.save(inbound);
        
        List<InventoryInboundDetail> details = inbound.getDetailList();
        if (details != null && !details.isEmpty()) {
            for (InventoryInboundDetail detail : details) {
                detail.setInboundId(inbound.getId());
                detailMapper.insert(detail);
            }
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateInbound(InventoryInbound inbound) {
        if (inbound == null || inbound.getId() == null) {
            throw new ServiceException("Inbound order cannot be null.");
        }
        
        InventoryInbound existInbound = this.getById(inbound.getId());
        if (existInbound == null) {
            throw new ServiceException("Inbound order not found.");
        }
        if (!"DRAFT".equals(existInbound.getStatus()) && !"SUBMITTED".equals(existInbound.getStatus())) {
            throw new ServiceException("Only draft or submitted inbound orders can be updated.");
        }
        
        existInbound.setType(inbound.getType());
        existInbound.setWarehouseCode(inbound.getWarehouseCode());
        existInbound.setBizNo(inbound.getBizNo());
        existInbound.setOperator(inbound.getOperator());
        existInbound.setOrderDate(inbound.getOrderDate());
        existInbound.setRemark(inbound.getRemark());
        existInbound.setUpdateTime(LocalDateTime.now());
        this.updateById(existInbound);
        
        LambdaQueryWrapper<InventoryInboundDetail> deleteWrapper = new LambdaQueryWrapper<>();
        deleteWrapper.eq(InventoryInboundDetail::getInboundId, inbound.getId());
        detailMapper.delete(deleteWrapper);
        
        List<InventoryInboundDetail> details = inbound.getDetailList();
        if (details != null && !details.isEmpty()) {
            for (InventoryInboundDetail detail : details) {
                detail.setId(null);
                detail.setInboundId(inbound.getId());
                detailMapper.insert(detail);
            }
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean submitInbound(Long id) {
        InventoryInbound inbound = this.getById(id);
        if (inbound == null) {
            throw new ServiceException("Inbound order not found.");
        }
        if (!"DRAFT".equals(inbound.getStatus())) {
            throw new ServiceException("Only draft inbound orders can be submitted.");
        }
        inbound.setStatus("SUBMITTED");
        return this.updateById(inbound);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean auditInbound(InventoryInbound inbound) {
        InventoryInbound existInbound = this.getById(inbound.getId());
        if (existInbound == null) {
            throw new ServiceException("Inbound order not found.");
        }
        if (!"SUBMITTED".equals(existInbound.getStatus())) {
            throw new ServiceException("Only submitted inbound orders can be audited.");
        }

        existInbound.setAuditBy(SecurityUtils.getUsername());
        existInbound.setAuditTime(new Date());
        existInbound.setAuditComment(inbound.getAuditComment());

        if ("APPROVED".equals(inbound.getStatus())) {
            existInbound.setStatus("APPROVED");
            this.updateById(existInbound);

            LambdaQueryWrapper<InventoryInboundDetail> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(InventoryInboundDetail::getInboundId, inbound.getId());
            List<InventoryInboundDetail> details = detailMapper.selectList(queryWrapper);

            InventoryWarehouse warehouse = warehouseService.selectWarehouseByCode(existInbound.getWarehouseCode());
            if (warehouse == null) {
                throw new ServiceException("Warehouse not found: " + existInbound.getWarehouseCode());
            }

            validateInboundCapacity(warehouse, details);

            for (InventoryInboundDetail detail : details) {
                validateInboundDetailExpiry(detail);
                BigDecimal qtyKg = convertToKg(detail.getQty(), detail.getUnit(), "Inbound detail quantity");
                Date prodDate = null;
                Date expDate = detail.getExpireDate() != null ? detail.getExpireDate() : new Date(System.currentTimeMillis() + 365L * 24 * 3600 * 1000);
                coreService.increaseStockWithBatch(
                    detail.getProductId(),
                    existInbound.getWarehouseCode(),
                    detail.getBatchNo(),
                    qtyKg,
                    detail.getQty(),
                    detail.getUnit(),
                    prodDate,
                    expDate,
                    detail.getQualityGrade(),
                    detail.getStockStatus(),
                    detail.getMainCategory(),
                    detail.getSubCategory()
                );
            }
        } else if ("REJECTED".equals(inbound.getStatus())) {
            existInbound.setStatus("REJECTED");
            this.updateById(existInbound);
        } else {
            throw new ServiceException("Invalid audit status.");
        }
        
        return true;
    }

    @Override
    public InventoryInbound selectInboundWithWarehouse(Long id) {
        InventoryInbound inbound = baseMapper.selectInboundWithWarehouse(id);
        if (inbound != null) {
            LambdaQueryWrapper<InventoryInboundDetail> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(InventoryInboundDetail::getInboundId, id);
            List<InventoryInboundDetail> details = detailMapper.selectList(queryWrapper);
            inbound.setDetailList(details);
        }
        return inbound;
    }

    @Override
    public List<InventoryInbound> selectInboundListWithWarehouse(InventoryInbound inbound) {
        return baseMapper.selectInboundListWithWarehouse(inbound);
    }

    private void validateInboundCapacity(InventoryWarehouse warehouse, List<InventoryInboundDetail> details) {
        if (details == null || details.isEmpty()) {
            return;
        }
        BigDecimal capacity = warehouse.getCapacity();
        if (capacity == null || capacity.compareTo(BigDecimal.ZERO) <= 0) {
            return;
        }

        BigDecimal incomingTotalKg = BigDecimal.ZERO;
        for (InventoryInboundDetail detail : details) {
            validateInboundDetailExpiry(detail);
            incomingTotalKg = incomingTotalKg.add(convertToKg(detail.getQty(), detail.getUnit(), "Inbound detail quantity"));
        }

        BigDecimal currentTotalKg = getCurrentWarehouseTotalKg(warehouse.getId());
        if (currentTotalKg.add(incomingTotalKg).compareTo(capacity) > 0) {
            throw new ServiceException("Inbound quantity exceeds warehouse capacity. Capacity: " + capacity + " KG, Current: " + currentTotalKg + " KG, Incoming: " + incomingTotalKg + " KG.");
        }
    }

    private BigDecimal getCurrentWarehouseTotalKg(Long warehouseId) {
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

    private void validateInboundDetailExpiry(InventoryInboundDetail detail) {
        if (detail == null) {
            throw new ServiceException("Inbound detail cannot be null.");
        }
        Date expireDate = detail.getExpireDate();
        if (expireDate != null && expireDate.before(new Date())) {
            throw new ServiceException("Inbound item has expired. Batch: " + safeString(detail.getBatchNo()));
        }
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
