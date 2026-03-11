package com.inspur.agriculture.inventory.service;

import com.inspur.agriculture.inventory.dto.TransferDTO;
import com.inspur.agriculture.inventory.vo.TransferVO;

import java.util.List;

/**
 * 调拨服务接口
 */
public interface ITransferService {

    List<TransferVO> selectTransferList(TransferDTO dto);

    TransferVO selectTransferById(Long id);

    Long insertTransfer(TransferDTO dto);

    int updateTransfer(TransferDTO dto);

    int deleteTransferByIds(Long[] ids);

    int submitTransfer(Long id);

    int auditTransfer(Long id, Boolean approved, String auditComment);
}
