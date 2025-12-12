package com.inspur.agriculture.input.service.inventory;

import com.inspur.agriculture.input.domain.inventory.OutboundOrder;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 出库单服务接口
 *
 * @author igdp
 */
public interface IOutboundOrderService {

    /**
     * 查询出库单列表
     *
     * @param params 查询条件
     * @return 出库单列表
     */
    List<Map<String, Object>> selectOutboundOrderList(Map<String, Object> params);

    /**
     * 根据出库单ID查询详情
     *
     * @param outboundOrderId 出库单ID
     * @return 出库单详情
     */
    Map<String, Object> selectOutboundOrderById(String outboundOrderId);

    /**
     * 创建出库单
     *
     * @param outboundOrder 出库单信息
     * @param details       出库明细列表
     * @return 出库单ID
     */
    String createOutboundOrder(OutboundOrder outboundOrder, List<Map<String, Object>> details);

    /**
     * 审核出库单
     *
     * @param outboundOrderId 出库单ID
     * @param auditStatus     审核状态(approved/rejected)
     * @param auditUser       审核人
     * @param auditTime       审核时间
     * @param remark          备注
     * @return 是否成功
     */
    boolean auditOutboundOrder(String outboundOrderId, String auditStatus, String auditUser, Date auditTime, String remark);

    /**
     * 执行出库确认
     * 业务逻辑：
     * 1. 校验出库单状态必须为已审核
     * 2. 校验库存充足性
     * 3. 校验批次有效性（检查过期日期）
     * 4. 按FIFO原则（先进先出）扣减库存
     * 5. 如果单个批次库存不足，自动拆分至多个入库批次
     * 6. 记录批次拆分明细到outbound_batch_split表
     * 7. 更新库存记录
     * 8. 记录库存变动日志
     * 9. 更新出库单状态为completed
     *
     * @param outboundOrderId 出库单ID
     * @param outboundTime    出库时间
     * @param operator        操作人
     * @return 出库结果（包含批次拆分信息）
     */
    Map<String, Object> confirmOutbound(String outboundOrderId, Date outboundTime, String operator);

    /**
     * 取消出库单
     *
     * @param outboundOrderId 出库单ID
     * @param operator        操作人
     * @return 是否成功
     */
    boolean cancelOutboundOrder(String outboundOrderId, String operator);

    /**
     * 统计出库单数量
     *
     * @param params 查询条件
     * @return 数量
     */
    int countOutboundOrders(Map<String, Object> params);

    /**
     * 查询待审核出库单数量
     *
     * @param warehouseId 仓库ID(可选)
     * @return 数量
     */
    int countPendingOrders(String warehouseId);

    /**
     * 按状态统计出库单
     *
     * @param warehouseId 仓库ID(可选)
     * @return 统计结果
     */
    List<Map<String, Object>> countByStatus(String warehouseId);

    /**
     * 按出库类型统计出库单
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 统计结果
     */
    List<Map<String, Object>> countByType(String startDate, String endDate);

    /**
     * 校验库存是否充足
     *
     * @param warehouseId 仓库ID
     * @param materialId  物料ID
     * @param quantity    出库数量
     * @return 校验结果（包含可用库存数量）
     */
    Map<String, Object> validateStock(String warehouseId, String materialId, BigDecimal quantity);

    /**
     * 批量校验库存是否充足
     *
     * @param warehouseId 仓库ID
     * @param details     出库明细列表
     * @return 校验结果（包含不足的物料列表）
     */
    Map<String, Object> validateStockBatch(String warehouseId, List<Map<String, Object>> details);

    /**
     * 查询分发单列表（用于关联单号下拉框）
     * 返回格式：release_id, release_name, display_text (格式: "分发单名称 (分发单编号)")
     *
     * @return 分发单列表
     */
    List<Map<String, Object>> selectReleaseOrderList();

    /**
     * 根据分发单ID获取分发投入品明细并校验库存
     * 校验规则：检查当前选中的仓库中投入品数量是否能够满足分发投入品需求数量
     *
     * @param releaseId   分发单ID
     * @param warehouseId 仓库ID
     * @return 分发投入品明细及库存校验结果
     */
    Map<String, Object> validateReleaseStock(String releaseId, String warehouseId);
}
