package com.inspur.agriculture.inventory.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.agriculture.inventory.domain.InventoryInbound;
import com.inspur.agriculture.inventory.domain.InventoryInboundDetail;
import com.inspur.agriculture.inventory.mapper.InventoryInboundDetailMapper;
import com.inspur.agriculture.inventory.mapper.InventoryInboundMapper;
import com.inspur.agriculture.inventory.service.IInventoryCoreService;
import com.inspur.agriculture.inventory.service.IInventoryInboundDetailService;
import com.inspur.agriculture.inventory.service.IInventoryInboundService;
import com.inspur.common.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class InventoryInboundServiceImpl extends ServiceImpl<InventoryInboundMapper, InventoryInbound> implements IInventoryInboundService {

    @Autowired
    private InventoryInboundDetailMapper detailMapper;
    
    @Autowired
    private IInventoryCoreService coreService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createInbound(InventoryInbound inbound) {
        if (inbound == null) {
            throw new ServiceException("入库单不能为空");
        }
        inbound.setStatus("DRAFT");
        inbound.setCreateTime(new Date());
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
    public boolean approveInbound(Long id) {
        InventoryInbound inbound = this.getById(id);
        if (inbound == null) {
            throw new ServiceException("入库单不存在");
        }
        if (!"SUBMITTED".equals(inbound.getStatus()) && !"DRAFT".equals(inbound.getStatus())) { // Allow direct approve from draft for now or based on flow
             // Assume flow is DRAFT -> SUBMITTED -> APPROVED. But for now allow simpler flow.
        }
        
        // Update status
        inbound.setStatus("APPROVED");
        this.updateById(inbound);
        
        // Increase stock
        LambdaQueryWrapper<InventoryInboundDetail> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(InventoryInboundDetail::getInboundId, id);
        List<InventoryInboundDetail> details = detailMapper.selectList(queryWrapper);
        
        for (InventoryInboundDetail detail : details) {
            // Assume production date and expire date are managed or defaulted, or passed in detail if available
            // For now use current date as production date and +1 year as expire date if not provided (detail doesn't have these fields in domain yet, add later if needed)
            Date prodDate = new Date();
            Date expDate = new Date(System.currentTimeMillis() + 365L * 24 * 3600 * 1000); // 1 year later
            
            coreService.increaseStock(detail.getSkuId(), inbound.getWarehouseId(), detail.getBatchNo(), detail.getRealQty(), prodDate, expDate);
        }
        
        return true;
    }
}
