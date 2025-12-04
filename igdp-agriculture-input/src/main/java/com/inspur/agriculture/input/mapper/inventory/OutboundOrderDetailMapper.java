package com.inspur.agriculture.input.mapper.inventory;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.agriculture.input.domain.inventory.OutboundOrderDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 出库单明细 Mapper
 *
 * @author igdp
 */
public interface OutboundOrderDetailMapper extends BaseMapper<OutboundOrderDetail> {

    /**
     * 查询出库单明细列表
     *
     * @param outboundOrderId 出库单ID
     * @return 明细列表
     */
    List<Map<String, Object>> selectDetailsByOrderId(@Param("outboundOrderId") String outboundOrderId);

    /**
     * 根据明细ID查询详情
     *
     * @param detailId 明细ID
     * @return 明细详情
     */
    Map<String, Object> selectDetailById(@Param("detailId") String detailId);

    /**
     * 批量插入出库单明细
     *
     * @param details 明细列表
     * @return 影响行数
     */
    int batchInsert(@Param("details") List<OutboundOrderDetail> details);

    /**
     * 根据物料ID查询出库明细
     *
     * @param materialId 物料ID
     * @param startDate  开始日期
     * @param endDate    结束日期
     * @return 明细列表
     */
    List<Map<String, Object>> selectByMaterialId(@Param("materialId") String materialId,
                                                   @Param("startDate") String startDate,
                                                   @Param("endDate") String endDate);

    /**
     * 根据批次ID查询出库明细
     *
     * @param materialBatchId 批次ID
     * @return 明细列表
     */
    List<OutboundOrderDetail> selectByBatchId(@Param("materialBatchId") String materialBatchId);

    /**
     * 统计出库总数量
     *
     * @param materialId  物料ID
     * @param warehouseId 仓库ID(可选)
     * @param startDate   开始日期(可选)
     * @param endDate     结束日期(可选)
     * @return 总数量
     */
    Map<String, Object> sumOutboundQuantity(@Param("materialId") String materialId,
                                             @Param("warehouseId") String warehouseId,
                                             @Param("startDate") String startDate,
                                             @Param("endDate") String endDate);

    /**
     * 查询出库明细（含批次拆分信息）
     *
     * @param outboundOrderId 出库单ID
     * @return 明细列表
     */
    List<Map<String, Object>> selectDetailsWithBatchSplits(@Param("outboundOrderId") String outboundOrderId);
}
