package com.inspur.agriculture.inventory.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.agriculture.inventory.domain.InventoryInbound;
import com.inspur.agriculture.inventory.domain.InventoryInboundDetail;
import com.inspur.agriculture.inventory.domain.InventoryProduct;
import com.inspur.agriculture.inventory.domain.InventoryStock;
import com.inspur.agriculture.inventory.domain.InventoryWarehouse;
import com.inspur.agriculture.inventory.mapper.InventoryInboundDetailMapper;
import com.inspur.agriculture.inventory.mapper.InventoryInboundMapper;
import com.inspur.agriculture.inventory.mapper.InventoryProductMapper;
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
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

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

    @Autowired
    private InventoryProductMapper productMapper;

    /**
     * 生成批次号 - 规则：BC + 年月日时分秒 + 3位随机数
     */
    private String generateBatchNo() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String timestamp = sdf.format(new Date());
        Random random = new Random();
        int randomNum = random.nextInt(1000);
        return "BC" + timestamp + String.format(Locale.ROOT, "%03d", randomNum);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createInbound(InventoryInbound inbound) {
        if (inbound == null) {
            throw new ServiceException("Inbound order cannot be null.");
        }
        if (inbound.getWarehouseCode() == null || inbound.getWarehouseCode().isEmpty()) {
            throw new ServiceException("Warehouse code cannot be empty.");
        }
        
        // 自动设置入库类型为GENERAL（一般入库）
        inbound.setType("GENERAL");
        
        // 自动设置操作人为当前登录用户
        try {
            String currentUsername = SecurityUtils.getUsername();
            inbound.setOperator(currentUsername);
        } catch (Exception e) {
            // 如果获取不到当前用户，使用默认值
            if (inbound.getOperator() == null || inbound.getOperator().isEmpty()) {
                inbound.setOperator("SYSTEM");
            }
        }
        
        // 自动设置入库时间为当前时间
        inbound.setOrderDate(new Date());
        
        inbound.setStatus("DRAFT");
        inbound.setCreateTime(LocalDateTime.now());
        this.save(inbound);
        
        List<InventoryInboundDetail> details = inbound.getDetailList();
        if (details != null && !details.isEmpty()) {
            for (InventoryInboundDetail detail : details) {
                detail.setInboundId(inbound.getId());
                
                // 自动生成批次号
                if (detail.getBatchNo() == null || detail.getBatchNo().isEmpty()) {
                    detail.setBatchNo(generateBatchNo());
                }
                
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
        
        // 自动设置入库类型为GENERAL（一般入库）
        existInbound.setType(inbound.getType() != null ? inbound.getType() : "GENERAL");

        // 自动设置操作人：如果前端没有传递，则保持原值
        if (inbound.getOperator() != null && !inbound.getOperator().isEmpty()) {
            existInbound.setOperator(inbound.getOperator());
        }

        // 自动设置入库时间：如果前端没有传递，则保持原值
        if (inbound.getOrderDate() != null) {
            existInbound.setOrderDate(inbound.getOrderDate());
        }

        existInbound.setWarehouseCode(inbound.getWarehouseCode() != null ? inbound.getWarehouseCode() : existInbound.getWarehouseCode());
        existInbound.setRemark(inbound.getRemark() != null ? inbound.getRemark() : existInbound.getRemark());
        existInbound.setUpdateTime(LocalDateTime.now());
        this.updateById(existInbound);

        // 删除旧的明细
        LambdaQueryWrapper<InventoryInboundDetail> deleteWrapper = new LambdaQueryWrapper<>();
        deleteWrapper.eq(InventoryInboundDetail::getInboundId, inbound.getId());
        detailMapper.delete(deleteWrapper);

        // 重新插入明细，自动生成批次号
        List<InventoryInboundDetail> details = inbound.getDetailList();
        if (details != null && !details.isEmpty()) {
            for (InventoryInboundDetail detail : details) {
                detail.setId(null);
                detail.setInboundId(inbound.getId());
                
                // 自动生成批次号
                if (detail.getBatchNo() == null || detail.getBatchNo().isEmpty()) {
                    detail.setBatchNo(generateBatchNo());
                }
                
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
            for (InventoryInboundDetail detail : details) {
                detail.setProductId(resolveProductId(detail));
            }

            InventoryWarehouse warehouse = warehouseService.selectWarehouseByCode(existInbound.getWarehouseCode());
            if (warehouse == null) {
                throw new ServiceException("Warehouse not found: " + existInbound.getWarehouseCode());
            }

            // 暂时取消库存容量校验
            // validateInboundCapacity(warehouse, details);


            for (InventoryInboundDetail detail : details) {
                validateInboundDetailExpiry(detail);
                BigDecimal qty = validateQuantity(detail.getQty(), "Inbound detail quantity");
                Date prodDate = null;
                Date expDate = detail.getExpireDate() != null ? detail.getExpireDate() : new Date(System.currentTimeMillis() + 365L * 24 * 3600 * 1000);
                coreService.increaseStockWithBatch(
                    detail.getProductId(),
                    existInbound.getWarehouseCode(),
                    detail.getBatchNo(),
                    qty,
                    detail.getQty(),
                    detail.getUnit(),
                    prodDate,
                    expDate,
                    detail.getQualityGrade(),
                    detail.getStockStatus(),
                    detail.getMainCategory(),
                    detail.getSubCategory(),
                    detail.getProductName()
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteInbound(Long id) {
        InventoryInbound inbound = this.getById(id);
        if (inbound == null) {
            throw new ServiceException("Inbound order not found.");
        }
        if (!"DRAFT".equals(inbound.getStatus()) && !"SUBMITTED".equals(inbound.getStatus())) {
            throw new ServiceException("Only draft or submitted orders can be deleted.");
        }
        // 删除明细
        LambdaQueryWrapper<InventoryInboundDetail> detailWrapper = new LambdaQueryWrapper<>();
        detailWrapper.eq(InventoryInboundDetail::getInboundId, id);
        detailMapper.delete(detailWrapper);
        // 删除主单
        return this.removeById(id);
    }


    private void validateInboundCapacity(InventoryWarehouse warehouse, List<InventoryInboundDetail> details) {
        if (details == null || details.isEmpty()) {
            return;
        }
        BigDecimal capacity = warehouse.getCapacity();
        if (capacity == null || capacity.compareTo(BigDecimal.ZERO) <= 0) {
            return;
        }


        BigDecimal incomingTotalQty = BigDecimal.ZERO;
        for (InventoryInboundDetail detail : details) {
            validateInboundDetailExpiry(detail);
            incomingTotalQty = incomingTotalQty.add(validateQuantity(detail.getQty(), "Inbound detail quantity"));
        }


        BigDecimal currentTotalQty = getCurrentWarehouseTotalQty(warehouse.getId());
        if (currentTotalQty.add(incomingTotalQty).compareTo(capacity) > 0) {
            throw new ServiceException("Inbound quantity exceeds warehouse capacity. Capacity: " + capacity + ", Current: " + currentTotalQty + ", Incoming: " + incomingTotalQty + ".");
        }
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


    private void validateInboundDetailExpiry(InventoryInboundDetail detail) {
        if (detail == null) {
            throw new ServiceException("Inbound detail cannot be null.");
        }
        Date expireDate = detail.getExpireDate();
        if (expireDate != null && expireDate.before(new Date())) {
            throw new ServiceException("Inbound item has expired. Batch: " + safeString(detail.getBatchNo()));
        }
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


    private Long resolveProductId(InventoryInboundDetail detail) {
        if (detail == null) {
            throw new ServiceException("Inbound detail cannot be null.");
        }
        if (detail.getProductId() != null) {
            return detail.getProductId();
        }

        LambdaQueryWrapper<InventoryProduct> wrapper = new LambdaQueryWrapper<>();

        if (detail.getProductName() != null && !detail.getProductName().isEmpty()) {
            wrapper.eq(InventoryProduct::getProductName, detail.getProductName());
        }
        if (detail.getMainCategory() != null && !detail.getMainCategory().isEmpty()) {
            wrapper.eq(InventoryProduct::getMainCategory, detail.getMainCategory());
        }
        if (detail.getSubCategory() != null && !detail.getSubCategory().isEmpty()) {
            wrapper.eq(InventoryProduct::getSubCategory, detail.getSubCategory());
        }

        wrapper.last("limit 1");
        InventoryProduct product = productMapper.selectOne(wrapper);
        if (product == null) {
            throw new ServiceException("Product not found for: " + detail.getMainCategory() + " / " + detail.getSubCategory() + " / " + detail.getProductName());
        }
        return product.getId();
    }
}
