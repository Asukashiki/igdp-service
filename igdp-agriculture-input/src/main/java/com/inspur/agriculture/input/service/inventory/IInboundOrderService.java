package com.inspur.agriculture.input.service.inventory;

import com.inspur.agriculture.input.domain.inventory.InboundOrder;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 入库单服务接口
 *
 * @author igdp
 */
public interface IInboundOrderService {

    /**
     * 查询入库单列表
     *
     * @param params 查询条件
     * @return 入库单列表
     */
    List<Map<String, Object>> selectInboundOrderList(Map<String, Object> params);

    /**
     * 根据入库单ID查询详情
     *
     * @param inboundOrderId 入库单ID
     * @return 入库单详情
     */
    Map<String, Object> selectInboundOrderById(String inboundOrderId);

    /**
     * 创建入库单
     *
     * @param inboundOrder 入库单信息
     * @param details      入库明细列表
     * @return 入库单ID
     */
    String createInboundOrder(InboundOrder inboundOrder, List<Map<String, Object>> details);

    /**
     * 审核入库单
     *
     * @param inboundOrderId 入库单ID
     * @param auditStatus    审核状态(approved/rejected)
     * @param auditUser      审核人
     * @param auditTime      审核时间
     * @param remark         备注
     * @return 是否成功
     */
    boolean auditInboundOrder(String inboundOrderId, String auditStatus, String auditUser, Date auditTime, String remark);

    /**
     * 执行入库确认
     * 业务逻辑：
     * 1. 校验入库单状态必须为已审核
     * 2. 校验仓库是否存在且容量充足
     * 3. 校验投入品存在性
     * 4. 为每个明细生成批次号和二维码
     * 5. 更新或创建库存记录
     * 6. 记录库存变动日志
     * 7. 更新入库单状态为completed
     *
     * @param inboundOrderId 入库单ID
     * @param inboundTime    入库时间
     * @param operator       操作人
     * @return 入库结果
     */
    Map<String, Object> confirmInbound(String inboundOrderId, Date inboundTime, String operator);

    /**
     * 取消入库单
     *
     * @param inboundOrderId 入库单ID
     * @param operator       操作人
     * @return 是否成功
     */
    boolean cancelInboundOrder(String inboundOrderId, String operator);

    /**
     * 统计入库单数量
     *
     * @param params 查询条件
     * @return 数量
     */
    int countInboundOrders(Map<String, Object> params);

    /**
     * 查询待审核入库单数量
     *
     * @param warehouseId 仓库ID(可选)
     * @return 数量
     */
    int countPendingOrders(String warehouseId);

    /**
     * 按状态统计入库单
     *
     * @param warehouseId 仓库ID(可选)
     * @return 统计结果
     */
    List<Map<String, Object>> countByStatus(String warehouseId);

    /**
     * 按入库类型统计入库单
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 统计结果
     */
    List<Map<String, Object>> countByType(String startDate, String endDate);
}
