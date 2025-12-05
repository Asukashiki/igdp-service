package com.inspur.agriculture.input.mapper.inventory;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.agriculture.input.domain.inventory.Stock;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 库存 Mapper
 *
 * @author igdp
 */
public interface StockMapper extends BaseMapper<Stock> {

    /**
     * 查询库存列表
     *
     * @param params 查询条件
     * @return 库存列表
     */
    List<Map<String, Object>> selectStockList(@Param("params") Map<String, Object> params);

    /**
     * 根据库存ID查询详情
     *
     * @param id 库存ID
     * @return 库存详情
     */
    Map<String, Object> selectStockById(@Param("id") String id);

    /**
     * 根据仓库ID和物料ID查询库存
     *
     * @param warehouseId 仓库ID
     * @param materialId  物料ID
     * @return 库存列表
     */
    List<Stock> selectByWarehouseAndMaterial(@Param("warehouseId") String warehouseId,
                                               @Param("materialId") String materialId);

    /**
     * 根据批次ID查询库存
     *
     * @param warehouseId     仓库ID
     * @param materialId      物料ID
     * @param materialBatchId 批次ID
     * @return 库存
     */
    Stock selectByBatch(@Param("warehouseId") String warehouseId,
                        @Param("materialId") String materialId,
                        @Param("materialBatchId") String materialBatchId);

    /**
     * 更新库存数量（入库）
     *
     * @param warehouseId     仓库ID
     * @param materialId      物料ID
     * @param materialBatchId 批次ID
     * @param quantity        数量
     * @return 影响行数
     */
    int updateStockInbound(@Param("warehouseId") String warehouseId,
                           @Param("materialId") String materialId,
                           @Param("materialBatchId") String materialBatchId,
                           @Param("quantity") BigDecimal quantity);

    /**
     * 更新库存数量（出库）
     *
     * @param warehouseId     仓库ID
     * @param materialId      物料ID
     * @param materialBatchId 批次ID
     * @param quantity        数量
     * @return 影响行数
     */
    int updateStockOutbound(@Param("warehouseId") String warehouseId,
                            @Param("materialId") String materialId,
                            @Param("materialBatchId") String materialBatchId,
                            @Param("quantity") BigDecimal quantity);

    /**
     * 统计仓库总库存
     *
     * @param warehouseId 仓库ID
     * @return 总库存数量
     */
    Map<String, Object> sumWarehouseStock(@Param("warehouseId") String warehouseId);

    /**
     * 统计物料库存
     *
     * @param materialId 物料ID
     * @return 库存统计
     */
    Map<String, Object> sumMaterialStock(@Param("materialId") String materialId);

    /**
     * 查询库存不足的物料
     *
     * @param warehouseId    仓库ID(可选)
     * @param minQuantity    最小库存数量
     * @return 库存不足列表
     */
    List<Map<String, Object>> selectLowStock(@Param("warehouseId") String warehouseId,
                                              @Param("minQuantity") BigDecimal minQuantity);

    /**
     * 查询即将过期的库存
     *
     * @param days        天数
     * @param warehouseId 仓库ID(可选)
     * @return 即将过期列表
     */
    List<Map<String, Object>> selectExpiringSoon(@Param("days") Integer days,
                                                  @Param("warehouseId") String warehouseId);

    /**
     * 查询已过期的库存
     *
     * @param warehouseId 仓库ID(可选)
     * @return 已过期列表
     */
    List<Map<String, Object>> selectExpired(@Param("warehouseId") String warehouseId);

    /**
     * 按FIFO规则查询可用库存
     *
     * @param warehouseId 仓库ID
     * @param materialId  物料ID
     * @param quantity    需要数量
     * @return 可用库存列表（按入库时间排序）
     */
    List<Stock> selectAvailableStockFIFO(@Param("warehouseId") String warehouseId,
                                          @Param("materialId") String materialId,
                                          @Param("quantity") BigDecimal quantity);
}
