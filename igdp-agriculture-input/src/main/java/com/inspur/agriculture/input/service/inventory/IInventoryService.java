package com.inspur.agriculture.input.service.inventory;

import com.inspur.agriculture.input.dto.inventory.InventoryQueryDTO;
import com.inspur.agriculture.input.vo.inventory.InventoryVO;

import java.util.List;
import java.util.Map;

/**
 * 库存 Service接口
 *
 * @author inspur
 * @date 2025-11-26
 */
public interface IInventoryService {

    /**
     * 查询库存列表
     *
     * @param queryDTO 查询条件
     * @return 库存列表
     */
    List<InventoryVO> getInventoryList(InventoryQueryDTO queryDTO);

    /**
     * 根据ID查询库存详情
     *
     * @param inventoryId 库存记录ID
     * @return 库存详情
     */
    InventoryVO getInventoryById(String inventoryId);

    /**
     * 查询库存预警列表
     *
     * @param warehouseId  仓库ID
     * @param warningType  预警类型
     * @return 库存预警列表
     */
    List<InventoryVO> getWarningList(Long warehouseId, String warningType);

    /**
     * 按投入品汇总库存
     *
     * @param warehouseId 仓库ID
     * @param inputType   投入品类型
     * @return 汇总列表
     */
    List<Map<String, Object>> getSummaryByInput(Long warehouseId, String inputType);

    /**
     * 按仓库汇总库存
     *
     * @param supplierId 供应商ID
     * @return 汇总列表
     */
    List<Map<String, Object>> getSummaryByWarehouse(Long supplierId);

    /**
     * 查询可用批次列表(用于出库选择)
     *
     * @param warehouseId 仓库ID
     * @param inputId     投入品ID
     * @return 可用批次列表
     */
    List<Map<String, Object>> getAvailableBatchList(Long warehouseId, Long inputId);
}
