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

import java.time.LocalDateTime;
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
        outbound.setCreateTime(LocalDateTime.now());
        this.save(outbound);
        
        List<InventoryOutboundDetail> details = outbound.getDetailList();
        if (details != null && !details.isEmpty()) {
            for (InventoryOutboundDetail detail : details) {
                detail.setOutboundId(outbound.getId());
                detailMapper.insert(detail);
                
                // 锁定库存
                coreService.lockStock(detail.getProductId(), outbound.getWarehouseId(), detail.getQty());
            }
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean submitOutbound(Long id) {
        InventoryOutbound outbound = this.getById(id);
        if (outbound == null) {
            throw new ServiceException("出库单不存在");
        }
        if (!"DRAFT".equals(outbound.getStatus())) {
            throw new ServiceException("只有草稿状态的出库单可以提交");
        }
        outbound.setStatus("SUBMITTED");
        return this.updateById(outbound);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean auditOutbound(InventoryOutbound outbound) {
        InventoryOutbound existOutbound = this.getById(outbound.getId());
        if (existOutbound == null) {
            throw new ServiceException("出库单不存在");
        }
        if (!"SUBMITTED".equals(existOutbound.getStatus())) {
            throw new ServiceException("只有待审批的出库单可以审批");
        }

        existOutbound.setAuditBy(outbound.getAuditBy());
        existOutbound.setAuditTime(new Date());
        existOutbound.setAuditComment(outbound.getAuditComment());

        LambdaQueryWrapper<InventoryOutboundDetail> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(InventoryOutboundDetail::getOutboundId, outbound.getId());
        List<InventoryOutboundDetail> details = detailMapper.selectList(queryWrapper);

        if ("APPROVED".equals(outbound.getStatus())) {
            existOutbound.setStatus("APPROVED");
            this.updateById(existOutbound);

            // Deduct stock
            for (InventoryOutboundDetail detail : details) {
                coreService.reduceStock(detail.getProductId(), existOutbound.getWarehouseId(), detail.getBatchNo(), detail.getQty());
            }
        } else if ("REJECTED".equals(outbound.getStatus())) {
            existOutbound.setStatus("REJECTED");
            this.updateById(existOutbound);

            // Release locked stock
            for (InventoryOutboundDetail detail : details) {
                coreService.releaseStock(detail.getProductId(), existOutbound.getWarehouseId(), detail.getQty());
            }
        } else {
            throw new ServiceException("无效的审批状态");
        }
        
        return true;
    }
}
