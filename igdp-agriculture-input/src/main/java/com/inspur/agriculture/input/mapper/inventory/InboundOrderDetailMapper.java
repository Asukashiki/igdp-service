package com.inspur.agriculture.input.mapper.inventory;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.agriculture.input.domain.inventory.InboundOrderDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 入库单明细 Mapper
 *
 * @author igdp
 */
public interface InboundOrderDetailMapper extends BaseMapper<InboundOrderDetail> {

    /**
     * 查询入库单明细列表
     *
     * @param inboundOrderId 入库单ID
     * @return 明细列表
     */
    List<Map<String, Object>> selectDetailsByOrderId(@Param("inboundOrderId") String inboundOrderId);

    /**
     * 根据明细ID查询详情
     *
     * @param detailId 明细ID
     * @return 明细详情
     */
    Map<String, Object> selectDetailById(@Param("detailId") String detailId);

    /**
     * 批量插入入库单明细
     *
     * @param details 明细列表
     * @return 影响行数
     */
    int batchInsert(@Param("details") List<InboundOrderDetail> details);

    /**
     * 根据物料ID查询入库明细
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
     * 根据批次ID查询入库明细
     *
     * @param materialBatchId 批次ID
     * @return 明细
     */
    InboundOrderDetail selectByBatchId(@Param("materialBatchId") String materialBatchId);

    /**
     * 统计入库总数量
     *
     * @param materialId  物料ID
     * @param warehouseId 仓库ID(可选)
     * @param startDate   开始日期(可选)
     * @param endDate     结束日期(可选)
     * @return 总数量
     */
    Map<String, Object> sumInboundQuantity(@Param("materialId") String materialId,
                                            @Param("warehouseId") String warehouseId,
                                            @Param("startDate") String startDate,
                                            @Param("endDate") String endDate);

    /**
     * 查询即将过期的入库明细
     *
     * @param days        天数
     * @param warehouseId 仓库ID(可选)
     * @return 明细列表
     */
    List<Map<String, Object>> selectExpiringSoon(@Param("days") Integer days,
                                                  @Param("warehouseId") String warehouseId);
}
