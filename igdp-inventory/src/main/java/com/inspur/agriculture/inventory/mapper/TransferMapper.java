package com.inspur.agriculture.inventory.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.agriculture.inventory.domain.InventoryTransfer;
import com.inspur.agriculture.inventory.dto.TransferDTO;
import com.inspur.agriculture.inventory.vo.TransferVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 调拨主表Mapper
 */
@Mapper
public interface TransferMapper extends BaseMapper<InventoryTransfer> {

    List<TransferVO> selectTransferList(TransferDTO dto);

    TransferVO selectTransferById(@Param("id") Long id);
}
