package com.inspur.agriculture.input.mapper.inventory;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.agriculture.input.domain.inventory.StockLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 库存变动日志 Mapper
 *
 * @author igdp
 */
public interface StockLogMapper extends BaseMapper<StockLog> {

    /**
     * 查询库存变动日志列表
     *
     * @param params 查询条件
     * @return 日志列表
     */
    List<Map<String, Object>> selectStockLogList(@Param("params") Map<String, Object> params);

    /**
     * 根据关联单号查询日志
     *
     * @param referenceOrderId 关联单号
     * @return 日志列表
     */
    List<StockLog> selectByReferenceOrderId(@Param("referenceOrderId") String referenceOrderId);

    /**
     * 根据物料ID查询日志
     *
     * @param materialId 物料ID
     * @param startDate  开始日期
     * @param endDate    结束日期
     * @return 日志列表
     */
    List<Map<String, Object>> selectByMaterialId(@Param("materialId") String materialId,
                                                  @Param("startDate") String startDate,
                                                  @Param("endDate") String endDate);

    /**
     * 根据仓库ID查询日志
     *
     * @param warehouseId 仓库ID
     * @param startDate   开始日期
     * @param endDate     结束日期
     * @return 日志列表
     */
    List<Map<String, Object>> selectByWarehouseId(@Param("warehouseId") String warehouseId,
                                                   @Param("startDate") String startDate,
                                                   @Param("endDate") String endDate);

    /**
     * 统计库存变动量
     *
     * @param warehouseId   仓库ID(可选)
     * @param materialId    物料ID(可选)
     * @param operationType 操作类型(可选)
     * @param startDate     开始日期
     * @param endDate       结束日期
     * @return 统计结果
     */
    Map<String, Object> sumChangeQuantity(@Param("warehouseId") String warehouseId,
                                           @Param("materialId") String materialId,
                                           @Param("operationType") String operationType,
                                           @Param("startDate") String startDate,
                                           @Param("endDate") String endDate);

    /**
     * 按日期统计库存变动
     *
     * @param warehouseId 仓库ID(可选)
     * @param startDate   开始日期
     * @param endDate     结束日期
     * @return 统计结果列表
     */
    List<Map<String, Object>> countByDate(@Param("warehouseId") String warehouseId,
                                           @Param("startDate") String startDate,
                                           @Param("endDate") String endDate);

    /**
     * 按操作类型统计
     *
     * @param warehouseId 仓库ID(可选)
     * @param startDate   开始日期
     * @param endDate     结束日期
     * @return 统计结果
     */
    List<Map<String, Object>> countByOperationType(@Param("warehouseId") String warehouseId,
                                                    @Param("startDate") String startDate,
                                                    @Param("endDate") String endDate);

    /**
     * 批量插入日志
     *
     * @param logs 日志列表
     * @return 影响行数
     */
    int batchInsert(@Param("logs") List<StockLog> logs);
}
