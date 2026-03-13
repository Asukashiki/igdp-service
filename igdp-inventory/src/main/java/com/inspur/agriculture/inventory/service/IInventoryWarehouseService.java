package com.inspur.agriculture.inventory.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.agriculture.inventory.domain.InventoryWarehouse;

import java.util.List;

/**
 * 仓库服务接口
 */
public interface IInventoryWarehouseService extends IService<InventoryWarehouse> {

    /**
     * 查询仓库列表
     *
     * @param warehouse 查询条件
     * @return 仓库列表
     */
    List<InventoryWarehouse> selectWarehouseList(InventoryWarehouse warehouse);

    /**
     * 根据ID查询仓库
     *
     * @param id 主键ID
     * @return 仓库信息
     */
    InventoryWarehouse selectWarehouseById(Long id);

    /**
     * 新增仓库
     *
     * @param warehouse 仓库信息
     * @return 结果
     */
    boolean createWarehouse(InventoryWarehouse warehouse);

    /**
     * 更新仓库
     *
     * @param warehouse 仓库信息
     * @return 结果
     */
    boolean updateWarehouse(InventoryWarehouse warehouse);

    /**
     * 删除仓库
     *
     * @param id 主键ID
     * @return 结果
     */
    boolean deleteWarehouse(Long id);

    /**
     * 根据仓库编码查询仓库
     *
     * @param warehouseCode 仓库编码
     * @return 仓库信息
     */
    InventoryWarehouse selectWarehouseByCode(String warehouseCode);

    /**
     * 生成仓库编码
     *
     * @param warehouseType 仓库类型（ZY/LM/HZS/QY）
     * @return 生成的仓库编码
     */
    String generateWarehouseCode(String warehouseType);
}
