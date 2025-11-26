package com.inspur.agriculture.input.service.inventory.impl;

import com.inspur.agriculture.input.dto.inventory.InventoryQueryDTO;
import com.inspur.agriculture.input.mapper.inventory.InventoryMapper;
import com.inspur.agriculture.input.service.inventory.IInventoryService;
import com.inspur.agriculture.input.vo.inventory.InventoryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 库存 Service实现
 *
 * @author inspur
 * @date 2025-11-26
 */
@Service
public class InventoryServiceImpl implements IInventoryService {

    @Autowired
    private InventoryMapper inventoryMapper;

    @Override
    public List<InventoryVO> getInventoryList(InventoryQueryDTO queryDTO) {
        return inventoryMapper.selectInventoryList(queryDTO);
    }

    @Override
    public InventoryVO getInventoryById(String inventoryId) {
        return inventoryMapper.selectInventoryById(inventoryId);
    }

    @Override
    public List<InventoryVO> getWarningList(Long warehouseId, String warningType) {
        return inventoryMapper.selectWarningList(warehouseId, warningType);
    }

    @Override
    public List<Map<String, Object>> getSummaryByInput(Long warehouseId, String inputType) {
        return inventoryMapper.selectSummaryByInput(warehouseId, inputType);
    }

    @Override
    public List<Map<String, Object>> getSummaryByWarehouse(Long supplierId) {
        return inventoryMapper.selectSummaryByWarehouse(supplierId);
    }
}
