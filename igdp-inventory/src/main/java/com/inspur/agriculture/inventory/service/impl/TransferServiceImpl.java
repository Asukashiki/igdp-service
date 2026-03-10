package com.inspur.agriculture.inventory.service.impl;

import com.inspur.agriculture.inventory.domain.InventoryTransfer;
import com.inspur.agriculture.inventory.domain.InventoryTransferDetail;
import com.inspur.agriculture.inventory.dto.TransferDTO;
import com.inspur.agriculture.inventory.mapper.TransferDetailMapper;
import com.inspur.agriculture.inventory.mapper.TransferMapper;
import com.inspur.agriculture.inventory.service.ITransferService;
import com.inspur.agriculture.inventory.vo.TransferVO;
import com.inspur.common.exception.ServiceException;
import com.inspur.common.utils.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

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

        String username = getCurrentUsername();
        transfer.setApplicant(username);
        transfer.setCreateBy(username);

        transferMapper.insert(transfer);

        if (dto.getDetailList() != null && !dto.getDetailList().isEmpty()) {
            for (InventoryTransferDetail detail : dto.getDetailList()) {
                detail.setTransferId(transfer.getId());
                detail.setCreateBy(username);
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

        BeanUtils.copyProperties(dto, transfer);
        String username = getCurrentUsername();
        transfer.setUpdateBy(username);

        int result = transferMapper.updateById(transfer);

        if (dto.getDetailList() != null) {
            detailMapper.deleteByTransferId(dto.getId());
            for (InventoryTransferDetail detail : dto.getDetailList()) {
                detail.setTransferId(dto.getId());
                detail.setCreateBy(username);
                detailMapper.insert(detail);
            }
        }

        return result;
    }

    @Override
    public int deleteTransferByIds(Long[] ids) {
        for (Long id : ids) {
            InventoryTransfer transfer = transferMapper.selectById(id);
            if (transfer != null && !"DRAFT".equals(transfer.getStatus())) {
                throw new ServiceException("Only draft orders can be deleted");
            }
            detailMapper.deleteByTransferId(id);
        }
        return transferMapper.deleteBatchIds(java.util.Arrays.asList(ids));
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

        if (approved) {
            transfer.setStatus("APPROVED");
            transfer.setOutTime(new Date());
            transfer.setInTime(new Date());
        } else {
            transfer.setStatus("REJECTED");
        }

        return transferMapper.updateById(transfer);
    }

    private String getCurrentUsername() {
        try {
            return SecurityUtils.getUsername() != null ? SecurityUtils.getUsername() : "admin";
        } catch (Exception e) {
            return "admin";
        }
    }

    private String generateTransferNo() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String dateStr = sdf.format(new Date());
        String random = String.format("%04d", new Random().nextInt(10000));
        return "TF" + dateStr + random;
    }
}
