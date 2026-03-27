package com.inspur.agriculture.inventory.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.agriculture.inventory.domain.InventoryWarehouse;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface InventoryWarehouseMapper extends BaseMapper<InventoryWarehouse> {

    List<InventoryWarehouse> selectWarehouseList(InventoryWarehouse warehouse);

    InventoryWarehouse selectWarehouseById(Long id);

    /**
     * 查询最大序列号
     *
     * @param pattern 编码模式（如：ZY_20250311_%）
     * @return 最大序列号
     */
    Integer selectMaxSequence(String pattern);

    /**
     * 检查编码是否存在
     *
     * @param warehouseCode 仓库编码
     * @param excludeId 排除的ID（编辑时使用）
     * @return 存在数量
     */
    int checkWarehouseCodeExists(@Param("warehouseCode") String warehouseCode, @Param("excludeId") Long excludeId);

    List<InventoryWarehouse> selectWarehouseListByDeptId(@Param("deptId") String deptId);

    List<com.inspur.agriculture.inventory.domain.vo.DeptCategoryStockVO> selectDeptCategoryStock(@Param("deptId") String deptId,
                                                                                                  @Param("mainCategory") String mainCategory,
                                                                                                  @Param("subCategory") String subCategory,
                                                                                                  @Param("productName") String productName);
}
