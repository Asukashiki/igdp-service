package com.inspur.agriculture.inventory.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.agriculture.inventory.domain.InventoryOutbound;
import com.inspur.agriculture.inventory.domain.InventoryOutboundDetail;
import com.inspur.agriculture.inventory.mapper.InventoryOutboundDetailMapper;
import com.inspur.agriculture.inventory.mapper.InventoryOutboundMapper;
import com.inspur.agriculture.inventory.service.IInventoryCoreService;
import com.inspur.agriculture.inventory.service.IInventoryOutboundDetailService;
import com.inspur.agriculture.inventory.service.IInventoryOutboundService;
import com.inspur.common.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class InventoryOutboundServiceImpl extends ServiceImpl<InventoryOutboundMapper, InventoryOutbound> implements IInventoryOutboundService {

    @Autowired
    private InventoryOutboundDetailMapper detailMapper;
    
    @Autowired
    private IInventoryCoreService coreService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createOutbound(InventoryOutbound outbound) {
        if (outbound == null) {
            throw new ServiceException("出库单不能为空");
        }
        outbound.setStatus("DRAFT");
        outbound.setCreateTime(new Date());
        this.save(outbound);
        
        List<InventoryOutboundDetail> details = outbound.getDetailList();
        if (details != null && !details.isEmpty()) {
            for (InventoryOutboundDetail detail : details) {
                detail.setOutboundId(outbound.getId());
                detailMapper.insert(detail);
                
                // 锁定库存
                coreService.lockStock(detail.getSkuId(), outbound.getWarehouseId(), detail.getApplyQty());
            }
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean approveOutbound(Long id) {
        InventoryOutbound outbound = this.getById(id);
        if (outbound == null) {
            throw new ServiceException("出库单不存在");
        }
        if (!"SUBMITTED".equals(outbound.getStatus()) && !"DRAFT".equals(outbound.getStatus())) {
            // Assume flow allows direct approve or submitted
        }
        
        // Update status
        outbound.setStatus("APPROVED");
        this.updateById(outbound);
        
        // Deduct stock
        LambdaQueryWrapper<InventoryOutboundDetail> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(InventoryOutboundDetail::getOutboundId, id);
        List<InventoryOutboundDetail> details = detailMapper.selectList(queryWrapper);
        
        for (InventoryOutboundDetail detail : details) {
            coreService.reduceStock(detail.getSkuId(), outbound.getWarehouseId(), detail.getBatchNo(), detail.getApplyQty());
        }
        
        return true;
    }
}
