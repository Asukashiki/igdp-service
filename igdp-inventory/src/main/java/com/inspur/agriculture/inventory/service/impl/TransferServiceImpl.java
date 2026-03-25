package com.inspur.agriculture.inventory.service.impl;

import com.inspur.agriculture.inventory.domain.InventoryTransfer;
import com.inspur.agriculture.inventory.domain.InventoryTransferDetail;
import com.inspur.agriculture.inventory.domain.InventoryOutbound;
import com.inspur.agriculture.inventory.domain.InventoryOutboundDetail;
import com.inspur.agriculture.inventory.domain.InventoryInbound;
import com.inspur.agriculture.inventory.domain.InventoryInboundDetail;
import com.inspur.agriculture.inventory.dto.TransferDTO;
import com.inspur.agriculture.inventory.mapper.TransferDetailMapper;
import com.inspur.agriculture.inventory.mapper.TransferMapper;
import com.inspur.agriculture.inventory.service.IInventoryInboundService;
import com.inspur.agriculture.inventory.service.IInventoryOutboundService;
import com.inspur.agriculture.inventory.service.ITransferService;
import com.inspur.agriculture.inventory.vo.TransferVO;
import com.inspur.common.exception.ServiceException;
import com.inspur.common.utils.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * 调拨服务实现
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TransferServiceImpl implements ITransferService {

    @Autowired
    private TransferMapper transferMapper;

    @Autowired
    private TransferDetailMapper detailMapper;

    @Autowired
    private IInventoryOutboundService outboundService;

    @Autowired
    private IInventoryInboundService inboundService;

    @Override
    public List<TransferVO> selectTransferList(TransferDTO dto) {
        return transferMapper.selectTransferList(dto);
    }

    @Override
    public TransferVO selectTransferById(Long id) {
        TransferVO vo = transferMapper.selectTransferById(id);
        if (vo != null) {
            vo.setDetailList(detailMapper.selectByTransferId(id));
        }
        return vo;
    }

    @Override
    public Long insertTransfer(TransferDTO dto) {
        InventoryTransfer transfer = new InventoryTransfer();
        BeanUtils.copyProperties(dto, transfer);

        transfer.setTransferNo(generateTransferNo());
        transfer.setApplyDate(new Date());
        transfer.setStatus("DRAFT");
        
        // 自动设置调拨类型为"库存预警"
        transfer.setTransferType("STOCK_WARNING");
        
        // 自动设置申请人和申请人归属部门为当前用户
        String username = getCurrentUsername();
        transfer.setApplicant(username);
        transfer.setDepartment(getCurrentUserDepartment());
        transfer.setCreateBy(username);

        if (dto.getExpectedDate() != null) {
            transfer.setExpectedDate(dto.getExpectedDate());
        }
        if (dto.getOutWarehouseCode() != null) {
            transfer.setOutWarehouseCode(dto.getOutWarehouseCode());
        }
        if (dto.getInWarehouseCode() != null) {
            transfer.setInWarehouseCode(dto.getInWarehouseCode());
        }
        if (dto.getRemark() != null) {
            transfer.setRemark(dto.getRemark());
        }

        transferMapper.insert(transfer);

        if (dto.getDetailList() != null && !dto.getDetailList().isEmpty()) {
            for (InventoryTransferDetail detail : dto.getDetailList()) {
                detail.setTransferId(transfer.getId());
                detail.setCreateBy(username);
                detail.setCreateTime(LocalDateTime.now());
                // product_id 可以为空，不需要特殊处理
                detailMapper.insert(detail);
            }
        }

        return transfer.getId();
    }

    @Override
    public int updateTransfer(TransferDTO dto) {
        InventoryTransfer transfer = transferMapper.selectById(dto.getId());
        if (transfer == null) {
            throw new ServiceException("Transfer order not found");
        }
        if (!"DRAFT".equals(transfer.getStatus()) && !"SUBMITTED".equals(transfer.getStatus())) {
            throw new ServiceException("Only draft or submitted orders can be updated");
        }

        // 只更新前端传递的字段，保持其他字段原值
        if (dto.getTransferType() != null) {
            transfer.setTransferType(dto.getTransferType());
        }
        if (dto.getExpectedDate() != null) {
            transfer.setExpectedDate(dto.getExpectedDate());
        }
        if (dto.getOutWarehouseCode() != null) {
            transfer.setOutWarehouseCode(dto.getOutWarehouseCode());
        }
        if (dto.getInWarehouseCode() != null) {
            transfer.setInWarehouseCode(dto.getInWarehouseCode());
        }
        if (dto.getRemark() != null) {
            transfer.setRemark(dto.getRemark());
        }
        if (dto.getApplicant() != null) {
            transfer.setApplicant(dto.getApplicant());
        }
        if (dto.getDepartment() != null) {
            transfer.setDepartment(dto.getDepartment());
        }

        String username = getCurrentUsername();
        transfer.setUpdateBy(username);

        int result = transferMapper.updateById(transfer);

        if (dto.getDetailList() != null) {
            detailMapper.deleteByTransferId(dto.getId());
            for (InventoryTransferDetail detail : dto.getDetailList()) {
                detail.setTransferId(dto.getId());
                detail.setCreateBy(username);
                detail.setCreateTime(LocalDateTime.now());
                // product_id 可以为空，不需要特殊处理
                detailMapper.insert(detail);
            }
        }

        return result;
    }

    @Override
    public int deleteTransferByIds(Long[] ids) {
        int count = 0;
        for (Long id : ids) {
            InventoryTransfer transfer = transferMapper.selectById(id);
            if (transfer != null && !"DRAFT".equals(transfer.getStatus())) {
                throw new ServiceException("Only draft orders can be deleted");
            }
            detailMapper.deleteByTransferId(id);
            count += transferMapper.deleteById(id);
        }
        return count;
    }

    @Override
    public int submitTransfer(Long id) {
        InventoryTransfer transfer = transferMapper.selectById(id);
        if (transfer == null) {
            throw new ServiceException("Transfer order not found");
        }
        if (!"DRAFT".equals(transfer.getStatus())) {
            throw new ServiceException("Only draft orders can be submitted");
        }

        List<InventoryTransferDetail> details = detailMapper.selectByTransferId(id);
        if (details == null || details.isEmpty()) {
            throw new ServiceException("Please add at least one detail");
        }

        transfer.setStatus("SUBMITTED");
        String username = getCurrentUsername();
        transfer.setUpdateBy(username);
        transfer.setUpdateTime(LocalDateTime.now());

        return transferMapper.updateById(transfer);
    }

    @Override
    public int auditTransfer(Long id, Boolean approved, String auditComment) {
        InventoryTransfer transfer = transferMapper.selectById(id);
        if (transfer == null) {
            throw new ServiceException("Transfer order not found");
        }
        if (!"SUBMITTED".equals(transfer.getStatus())) {
            throw new ServiceException("Only submitted orders can be audited");
        }

        String username = getCurrentUsername();
        transfer.setAuditBy(username);
        transfer.setAuditTime(new Date());
        transfer.setAuditComment(auditComment);
        transfer.setUpdateBy(username);
        transfer.setUpdateTime(LocalDateTime.now());

        if (approved) {
            transfer.setStatus("APPROVED");
            transfer.setOutTime(new Date());
            transfer.setInTime(new Date());

            List<InventoryTransferDetail> details = detailMapper.selectByTransferId(id);
            if (details == null || details.isEmpty()) {
                throw new ServiceException("Transfer details cannot be empty");
            }

            // Create outbound order (from out-warehouse)
            InventoryOutbound outbound = buildOutboundFromTransfer(transfer, details, username);
            outboundService.createOutbound(outbound);
            outboundService.submitOutbound(outbound.getId());

            InventoryOutbound auditOutbound = new InventoryOutbound();
            auditOutbound.setId(outbound.getId());
            auditOutbound.setStatus("APPROVED");
            auditOutbound.setAuditBy(username);
            auditOutbound.setAuditComment(auditComment != null ? auditComment : "Transfer approved");
            outboundService.auditOutbound(auditOutbound);

            // Create inbound order (to in-warehouse)
            InventoryInbound inbound = buildInboundFromTransfer(transfer, details, username);
            inboundService.createInbound(inbound);
            inboundService.submitInbound(inbound.getId());

            InventoryInbound auditInbound = new InventoryInbound();
            auditInbound.setId(inbound.getId());
            auditInbound.setStatus("APPROVED");
            auditInbound.setAuditComment(auditComment != null ? auditComment : "Transfer approved");
            inboundService.auditInbound(auditInbound);

            transfer.setRelatedOutboundId(outbound.getId());
            transfer.setRelatedInboundId(inbound.getId());
        } else {
            transfer.setStatus("REJECTED");
        }

        return transferMapper.updateById(transfer);
    }

    private InventoryOutbound buildOutboundFromTransfer(InventoryTransfer transfer, List<InventoryTransferDetail> details, String username) {
        InventoryOutbound outbound = new InventoryOutbound();
        outbound.setOutboundNo("TF-OUT-" + transfer.getTransferNo());
        outbound.setWarehouseCode(transfer.getOutWarehouseCode());
        outbound.setWarehouseName(transfer.getOutWarehouseName());
        outbound.setType("TRANSFER");
        outbound.setReceiverType("WAREHOUSE");
        outbound.setReceiver(transfer.getInWarehouseName());
        outbound.setBizNo(transfer.getTransferNo());
        outbound.setOperator(username);
        outbound.setOrderDate(new Date());
        outbound.setRemark("Auto created from transfer");
        outbound.setDetailList(buildOutboundDetails(details));
        return outbound;
    }

    private List<InventoryOutboundDetail> buildOutboundDetails(List<InventoryTransferDetail> details) {
        return details.stream().map(detail -> {
            InventoryOutboundDetail outboundDetail = new InventoryOutboundDetail();
            outboundDetail.setProductId(detail.getProductId());
            outboundDetail.setProductName(detail.getProductName());
            outboundDetail.setMainCategory(detail.getMainCategory());
            outboundDetail.setSubCategory(detail.getSubCategory());
            outboundDetail.setBatchNo(detail.getBatchNo());
            outboundDetail.setSupplier(detail.getSupplier());
            outboundDetail.setQty(detail.getQty());
            outboundDetail.setUnit(detail.getUnit());
            outboundDetail.setExpireDate(detail.getExpireDate());
            return outboundDetail;
        }).collect(Collectors.toList());
    }

    private InventoryInbound buildInboundFromTransfer(InventoryTransfer transfer, List<InventoryTransferDetail> details, String username) {
        InventoryInbound inbound = new InventoryInbound();
        inbound.setInboundNo("TF-IN-" + transfer.getTransferNo());
        inbound.setWarehouseCode(transfer.getInWarehouseCode());
        inbound.setWarehouseName(transfer.getInWarehouseName());
        inbound.setType("TRANSFER");
        inbound.setBizNo(transfer.getTransferNo());
        inbound.setOperator(username);
        inbound.setOrderDate(new Date());
        inbound.setRemark("Auto created from transfer");
        inbound.setDetailList(buildInboundDetails(details));
        return inbound;
    }

    private List<InventoryInboundDetail> buildInboundDetails(List<InventoryTransferDetail> details) {
        return details.stream().map(detail -> {
            InventoryInboundDetail inboundDetail = new InventoryInboundDetail();
            inboundDetail.setProductId(detail.getProductId());
            inboundDetail.setProductName(detail.getProductName());
            inboundDetail.setMainCategory(detail.getMainCategory());
            inboundDetail.setSubCategory(detail.getSubCategory());
            inboundDetail.setBatchNo(detail.getBatchNo());
            inboundDetail.setSupplier(detail.getSupplier());
            inboundDetail.setQty(detail.getQty());
            inboundDetail.setUnit(detail.getUnit());
            inboundDetail.setExpireDate(detail.getExpireDate());
            return inboundDetail;
        }).collect(Collectors.toList());
    }

    private String getCurrentUsername() {
        try {
            return SecurityUtils.getUsername() != null ? SecurityUtils.getUsername() : "admin";
        } catch (Exception e) {
            return "admin";
        }
    }

    private String getCurrentUserDepartment() {
        try {
            return SecurityUtils.getDeptName() != null ? SecurityUtils.getDeptName() : "";
        } catch (Exception e) {
            return "";
        }
    }

    private String generateTransferNo() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String dateStr = sdf.format(new Date());
        String random = String.format("%04d", new Random().nextInt(10000));
        return "TF" + dateStr + random;
    }
}
