package com.inspur.agriculture.input.mapper.inventory;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.agriculture.input.domain.inventory.InboundOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 入库单 Mapper
 *
 * @author igdp
 */
public interface InboundOrderMapper extends BaseMapper<InboundOrder> {

    /**
     * 查询入库单列表
     *
     * @param params 查询条件
     * @return 入库单列表
     */
    List<Map<String, Object>> selectInboundOrderList(@Param("params") Map<String, Object> params);

    /**
     * 根据入库单ID查询详情
     *
     * @param inboundOrderId 入库单ID
     * @return 入库单详情
     */
    Map<String, Object> selectInboundOrderById(@Param("inboundOrderId") String inboundOrderId);

    /**
     * 统计入库单数量
     *
     * @param params 查询条件
     * @return 数量
     */
    int countInboundOrders(@Param("params") Map<String, Object> params);

    /**
     * 根据入库批次号查询入库单
     *
     * @param inboundBatchId 入库批次号
     * @return 入库单
     */
    InboundOrder selectByInboundBatchId(@Param("inboundBatchId") String inboundBatchId);

    /**
     * 更新入库单状态
     *
     * @param inboundOrderId 入库单ID
     * @param status         状态
     * @return 影响行数
     */
    int updateInboundStatus(@Param("inboundOrderId") String inboundOrderId,
                            @Param("status") String status);

    /**
     * 查询待审核入库单数量
     *
     * @param warehouseId 仓库ID(可选)
     * @return 数量
     */
    int countPendingOrders(@Param("warehouseId") String warehouseId);

    /**
     * 按状态统计入库单
     *
     * @param warehouseId 仓库ID(可选)
     * @return 统计结果
     */
    List<Map<String, Object>> countByStatus(@Param("warehouseId") String warehouseId);

    /**
     * 按入库类型统计入库单
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 统计结果
     */
    List<Map<String, Object>> countByType(@Param("startDate") String startDate,
                                           @Param("endDate") String endDate);

    /**
     * 查询分发单列表（用于关联单号下拉框）
     * 返回格式：release_id, release_name, display_text
     *
     * @return 分发单列表
     */
    List<Map<String, Object>> selectReleaseOrderList();
}
