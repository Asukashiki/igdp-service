package com.inspur.agriculture.input.mapper.inventory;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.agriculture.input.domain.inventory.Inventory;
import com.inspur.agriculture.input.dto.inventory.InventoryQueryDTO;
import com.inspur.agriculture.input.vo.inventory.InventoryVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 库存 Mapper
 *
 * @author inspur
 * @date 2025-11-26
 */
public interface InventoryMapper extends BaseMapper<Inventory> {

    /**
     * 查询库存列表
     *
     * @param query 查询条件
     * @return 库存列表
     */
    List<InventoryVO> selectInventoryList(@Param("query") InventoryQueryDTO query);

    /**
     * 根据ID查询库存详情
     *
     * @param inventoryId 库存记录ID
     * @return 库存详情
     */
    InventoryVO selectInventoryById(@Param("inventoryId") String inventoryId);

    /**
     * 查询库存预警列表(临期和过期)
     *
     * @param warehouseId  仓库ID
     * @param warningType  预警类型: all-全部/nearExpiry-临期/expired-过期
     * @return 库存预警列表
     */
    List<InventoryVO> selectWarningList(@Param("warehouseId") Long warehouseId,
                                        @Param("warningType") String warningType);

    /**
     * 按投入品汇总库存
     *
     * @param warehouseId 仓库ID
     * @param inputType   投入品类型
     * @return 汇总列表
     */
    List<Map<String, Object>> selectSummaryByInput(@Param("warehouseId") Long warehouseId,
                                                    @Param("inputType") String inputType);

    /**
     * 按仓库汇总库存
     *
     * @param supplierId 供应商ID
     * @return 汇总列表
     */
    List<Map<String, Object>> selectSummaryByWarehouse(@Param("supplierId") Long supplierId);

    /**
     * 查询或创建库存记录
     *
     * @param inputId     投入品ID
     * @param batchNo     批次号
     * @param warehouseId 仓库ID
     * @return 库存记录
     */
    Inventory selectOrCreateInventory(@Param("inputId") Long inputId,
                                      @Param("batchNo") String batchNo,
                                      @Param("warehouseId") Long warehouseId);

    /**
     * 更新库存数量
     *
     * @param inventoryId    库存记录ID
     * @param quantityChange 数量变化(正数为增加,负数为减少)
     * @return 影响行数
     */
    int updateQuantity(@Param("inventoryId") String inventoryId,
                       @Param("quantityChange") Integer quantityChange);

    /**
     * 生成库存记录ID
     *
     * @return 库存记录ID
     */
    String generateInventoryId();

    /**
     * 更新库存状态(临期/过期)
     *
     * @return 影响行数
     */
    int updateStockStatus();

    /**
     * 查询可用批次列表(用于出库选择)
     *
     * @param warehouseId 仓库ID
     * @param inputId     投入品ID
     * @return 可用批次列表
     */
    List<Map<String, Object>> selectAvailableBatchList(@Param("warehouseId") Long warehouseId,
                                                        @Param("inputId") Long inputId);
}
