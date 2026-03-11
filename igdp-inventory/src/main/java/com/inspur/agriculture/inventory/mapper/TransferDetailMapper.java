package com.inspur.agriculture.inventory.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.agriculture.inventory.domain.InventoryTransferDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 调拨明细表Mapper
 */
@Mapper
public interface TransferDetailMapper extends BaseMapper<InventoryTransferDetail> {

    List<InventoryTransferDetail> selectByTransferId(@Param("transferId") Long transferId);

    int deleteByTransferId(@Param("transferId") Long transferId);
}
