package com.inspur.agriculture.inventory.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.agriculture.inventory.domain.InventoryOutbound;

import java.util.List;

/**
 * 出库单服务接口
 */
public interface IInventoryOutboundService extends IService<InventoryOutbound> {

    /**
     * 创建出库单
     * @param outbound 出库单
     * @return 结果
     */
    boolean createOutbound(InventoryOutbound outbound);

    /**
     * 更新出库单
     * @param outbound 出库单
     * @return 结果
     */
    boolean updateOutbound(InventoryOutbound outbound);

    /**
     * 提交出库单
     * @param id 出库单ID
     * @return 结果
     */
    boolean submitOutbound(Long id);

    /**
     * 审核出库单
     * @param outbound 包含审核信息的出库单对象
     * @return 结果
     */
    boolean auditOutbound(InventoryOutbound outbound);

    /**
     * 删除出库单
     * @param id 出库单ID
     * @return 结果
     */
    boolean deleteOutbound(Long id);

    /**
     * 查询出库单（包含仓库名称）
     * @param id 出库单ID
     * @return 出库单
     */
    InventoryOutbound selectOutboundWithWarehouse(Long id);

    /**
     * 查询出库单列表（包含仓库名称）
     * @param outbound 查询条件
     * @return 出库单列表
     */
    List<InventoryOutbound> selectOutboundListWithWarehouse(InventoryOutbound outbound);
}
