package com.inspur.agriculture.input.service.inventory;

import com.inspur.agriculture.input.dto.inventory.WarehouseDTO;
import com.inspur.agriculture.input.dto.inventory.WarehouseQueryDTO;
import com.inspur.agriculture.input.vo.inventory.WarehouseVO;

import java.util.List;

/**
 * 仓库 Service接口
 *
 * @author inspur
 * @date 2025-11-26
 */
public interface IWarehouseService {

    /**
     * 查询仓库列表
     *
     * @param queryDTO 查询条件
     * @return 仓库列表
     */
    List<WarehouseVO> getWarehouseList(WarehouseQueryDTO queryDTO);

    /**
     * 根据ID查询仓库详情
     *
     * @param warehouseId 仓库ID
     * @return 仓库详情
     */
    WarehouseVO getWarehouseById(Long warehouseId);

    /**
     * 添加仓库
     *
     * @param dto 仓库信息
     * @return 结果
     */
    int addWarehouse(WarehouseDTO dto);

    /**
     * 更新仓库
     *
     * @param dto 仓库信息
     * @return 结果
     */
    int updateWarehouse(WarehouseDTO dto);

    /**
     * 删除仓库
     *
     * @param warehouseId 仓库ID
     * @return 结果
     */
    int deleteWarehouse(Long warehouseId);
}
