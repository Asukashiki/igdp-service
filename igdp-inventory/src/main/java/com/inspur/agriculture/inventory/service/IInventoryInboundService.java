package com.inspur.agriculture.inventory.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.agriculture.inventory.domain.InventoryInbound;

import java.util.List;

/**
 * 入库单服务接口
 */
public interface IInventoryInboundService extends IService<InventoryInbound> {
    
    /**
     * 创建入库单
     * @param inbound 入库单
     * @return 结果
     */
    boolean createInbound(InventoryInbound inbound);

    /**
     * 更新入库单
     * @param inbound 入库单
     * @return 结果
     */
    boolean updateInbound(InventoryInbound inbound);

    /**
     * 提交入库单
     * @param id 入库单ID
     * @return 结果
     */
    boolean submitInbound(Long id);

    /**
     * 审核入库单
     * @param inbound 包含审核信息的入库单对象
     * @return 结果
     */
    boolean auditInbound(InventoryInbound inbound);

    /**
     * 查询入库单（包含仓库名称）
     * @param id 入库单ID
     * @return 入库单
     */
    InventoryInbound selectInboundWithWarehouse(Long id);

    /**
     * 查询入库单列表（包含仓库名称）
     * @param inbound 查询条件
     * @return 入库单列表
     */
    List<InventoryInbound> selectInboundListWithWarehouse(InventoryInbound inbound);
}
