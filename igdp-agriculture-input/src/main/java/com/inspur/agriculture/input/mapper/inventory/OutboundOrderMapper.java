package com.inspur.agriculture.input.mapper.inventory;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.agriculture.input.domain.inventory.OutboundOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 出库单 Mapper
 *
 * @author igdp
 */
public interface OutboundOrderMapper extends BaseMapper<OutboundOrder> {

    /**
     * 查询出库单列表
     *
     * @param params 查询条件
     * @return 出库单列表
     */
    List<Map<String, Object>> selectOutboundOrderList(@Param("params") Map<String, Object> params);

    /**
     * 根据出库单ID查询详情
     *
     * @param outboundOrderId 出库单ID
     * @return 出库单详情
     */
    Map<String, Object> selectOutboundOrderById(@Param("outboundOrderId") String outboundOrderId);

    /**
     * 统计出库单数量
     *
     * @param params 查询条件
     * @return 数量
     */
    int countOutboundOrders(@Param("params") Map<String, Object> params);

    /**
     * 根据出库批次号查询出库单
     *
     * @param outboundBatchId 出库批次号
     * @return 出库单
     */
    OutboundOrder selectByOutboundBatchId(@Param("outboundBatchId") String outboundBatchId);

    /**
     * 更新出库单状态
     *
     * @param outboundOrderId 出库单ID
     * @param status          状态
     * @return 影响行数
     */
    int updateOutboundStatus(@Param("outboundOrderId") String outboundOrderId,
                             @Param("status") String status);

    /**
     * 查询待审核出库单数量
     *
     * @param warehouseId 仓库ID(可选)
     * @return 数量
     */
    int countPendingOrders(@Param("warehouseId") String warehouseId);

    /**
     * 按状态统计出库单
     *
     * @param warehouseId 仓库ID(可选)
     * @return 统计结果
     */
    List<Map<String, Object>> countByStatus(@Param("warehouseId") String warehouseId);

    /**
     * 按出库类型统计出库单
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 统计结果
     */
    List<Map<String, Object>> countByType(@Param("startDate") String startDate,
                                           @Param("endDate") String endDate);

    /**
     * 根据出库对象ID查询出库单
     *
     * @param outboundObjectId 出库对象ID
     * @return 出库单列表
     */
    List<OutboundOrder> selectByOutboundObjectId(@Param("outboundObjectId") String outboundObjectId);

    /**
     * 查询分发单列表（用于关联单号下拉框）
     * 返回格式：release_id, release_name, display_text
     *
     * @return 分发单列表
     */
    List<Map<String, Object>> selectReleaseOrderList();

    /**
     * 根据分发单ID查询分发投入品明细
     *
     * @param releaseId 分发单ID
     * @return 分发投入品明细列表
     */
    List<Map<String, Object>> selectReleaseDetailsByReleaseId(@Param("releaseId") String releaseId);

    /**
     * 根据分发单ID查询分发单主表信息
     *
     * @param releaseId 分发单ID
     * @return 分发单主表信息
     */
    Map<String, Object> selectReleaseMainById(@Param("releaseId") String releaseId);

    /**
     * 更新分发单状态
     *
     * @param releaseId 分发单ID
     * @param status 状态
     * @return 影响行数
     */
    int updateReleaseOrderStatus(@Param("releaseId") String releaseId, @Param("status") String status);

    /**
     * 更新Union确认接收单的接收状态
     *
     * @param releaseId 分发单ID
     * @param receiveStatus 接收状态
     * @return 影响行数
     */
    int updateReceiveUnionStatus(@Param("releaseId") String releaseId,
                                  @Param("receiveStatus") String receiveStatus
                                 );


    /**
     * 更新Woreda确认接收单的接收状态
     *
     * @param releaseId 分发单ID
     * @param receiveStatus 接收状态
     * @return 影响行数
     */
    int updateReceiveWoredaStatus(@Param("releaseId") String releaseId,
                                 @Param("receiveStatus") String receiveStatus
                                 );


}
