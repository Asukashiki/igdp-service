package com.inspur.agriculture.input.mapper.inventory;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.agriculture.input.domain.inventory.Warehouse;
import com.inspur.agriculture.input.dto.inventory.WarehouseQueryDTO;
import com.inspur.agriculture.input.vo.inventory.WarehouseVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * 仓库 Mapper
 *
 * @author inspur
 * @date 2025-11-26
 */
@Mapper
public interface WarehouseMapper extends BaseMapper<Warehouse> {

    /**
     * 查询仓库列表
     *
     * @param query 查询条件
     * @return 仓库列表
     */
    List<WarehouseVO> selectWarehouseList(@Param("query") WarehouseQueryDTO query);

    /**
     * 根据ID查询仓库详情
     *
     * @param warehouseId 仓库ID
     * @return 仓库详情
     */
    WarehouseVO selectWarehouseById(@Param("warehouseId") Long warehouseId);

    /**
     * 检查仓库编号是否存在
     *
     * @param warehouseCode 仓库编号
     * @param excludeId     排除的仓库ID
     * @return 数量
     */
    int checkWarehouseCodeExists(@Param("warehouseCode") String warehouseCode,
                                  @Param("excludeId") Long excludeId);

    /**
     * 更新仓库已用容量
     *
     * @param warehouseId    仓库ID
     * @param capacityChange 容量变化(正数为增加,负数为减少)
     * @return 影响行数
     */
    int updateUsedCapacity(@Param("warehouseId") Long warehouseId,
                           @Param("capacityChange") BigDecimal capacityChange);

    /**
     * 更新仓库已用容积（L）
     *
     * @param warehouseId        仓库ID
     * @param warehouseAreaChange 容积变化(正数为增加,负数为减少)
     * @return 影响行数
     */
    int updateUsedWarehouseArea(@Param("warehouseId") Long warehouseId,
                                 @Param("warehouseAreaChange") BigDecimal warehouseAreaChange);
}
