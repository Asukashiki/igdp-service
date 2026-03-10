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

import java.time.LocalDateTime;
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
    public boolean submitInbound(Long id) {
        InventoryInbound inbound = this.getById(id);
        if (inbound == null) {
            throw new ServiceException("入库单不存在");
        }
        if (!"DRAFT".equals(inbound.getStatus())) {
            throw new ServiceException("只有草稿状态的入库单可以提交");
        }
        inbound.setStatus("SUBMITTED");
        return this.updateById(inbound);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean auditInbound(InventoryInbound inbound) {
        InventoryInbound existInbound = this.getById(inbound.getId());
        if (existInbound == null) {
            throw new ServiceException("入库单不存在");
        }
        if (!"SUBMITTED".equals(existInbound.getStatus())) {
            throw new ServiceException("只有待审批的入库单可以审批");
        }

        existInbound.setAuditBy(inbound.getAuditBy());
        existInbound.setAuditTime(new Date());
        existInbound.setAuditComment(inbound.getAuditComment());

        if ("APPROVED".equals(inbound.getStatus())) {
            existInbound.setStatus("APPROVED");
            this.updateById(existInbound);

            // Increase stock
            LambdaQueryWrapper<InventoryInboundDetail> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(InventoryInboundDetail::getInboundId, inbound.getId());
            List<InventoryInboundDetail> details = detailMapper.selectList(queryWrapper);

            for (InventoryInboundDetail detail : details) {
                Date prodDate = new Date();
                Date expDate = detail.getExpireDate() != null ? detail.getExpireDate() : new Date(System.currentTimeMillis() + 365L * 24 * 3600 * 1000); 
                coreService.increaseStock(detail.getSkuId(), existInbound.getWarehouseId(), detail.getBatchNo(), detail.getRealQty(), prodDate, expDate, detail.getQualityGrade(), detail.getStockStatus());
            }
        } else if ("REJECTED".equals(inbound.getStatus())) {
            existInbound.setStatus("REJECTED");
            this.updateById(existInbound);
        } else {
            throw new ServiceException("无效的审批状态");
        }
        
        return true;
    }
}
