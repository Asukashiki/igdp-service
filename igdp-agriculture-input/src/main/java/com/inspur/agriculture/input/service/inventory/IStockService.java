package com.inspur.agriculture.input.service.inventory;

import com.inspur.agriculture.input.domain.inventory.Stock;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 库存服务接口
 *
 * @author igdp
 */
public interface IStockService {

    /**
     * 查询库存列表
     *
     * @param params 查询条件
     * @return 库存列表
     */
    List<Map<String, Object>> selectStockList(Map<String, Object> params);

    /**
     * 根据库存ID查询详情
     *
     * @param id 库存ID
     * @return 库存详情
     */
    Map<String, Object> selectStockById(String id);

    /**
     * 根据仓库和物料查询库存
     *
     * @param warehouseId 仓库ID
     * @param materialId  物料ID
     * @return 库存列表
     */
    List<Stock> selectByWarehouseAndMaterial(String warehouseId, String materialId);

    /**
     * 根据批次查询库存
     *
     * @param warehouseId     仓库ID
     * @param materialId      物料ID
     * @param materialBatchId 批次ID
     * @return 库存
     */
    Stock selectByBatch(String warehouseId, String materialId, String materialBatchId);

    /**
     * 更新库存数量（入库）
     *
     * @param warehouseId     仓库ID
     * @param materialId      物料ID
     * @param materialBatchId 批次ID
     * @param quantity        数量
     * @param expiryDate      过期日期
     * @param qrCode          二维码
     * @param operator        操作人
     * @return 是否成功
     */
    boolean updateStockInbound(String warehouseId, String materialId, String materialBatchId,
                               BigDecimal quantity, java.util.Date expiryDate, String qrCode, String operator);

    /**
     * 更新库存数量（出库）
     *
     * @param warehouseId     仓库ID
     * @param materialId      物料ID
     * @param materialBatchId 批次ID
     * @param quantity        数量
     * @param operator        操作人
     * @return 是否成功
     */
    boolean updateStockOutbound(String warehouseId, String materialId, String materialBatchId,
                                BigDecimal quantity, String operator);

    /**
     * 统计仓库总库存
     *
     * @param warehouseId 仓库ID
     * @return 总库存数量
     */
    Map<String, Object> sumWarehouseStock(String warehouseId);

    /**
     * 统计物料库存
     *
     * @param materialId 物料ID
     * @return 库存统计
     */
    Map<String, Object> sumMaterialStock(String materialId);

    /**
     * 查询库存不足的物料
     *
     * @param warehouseId 仓库ID(可选)
     * @param minQuantity 最小库存数量
     * @return 库存不足列表
     */
    List<Map<String, Object>> selectLowStock(String warehouseId, BigDecimal minQuantity);

    /**
     * 查询即将过期的库存
     *
     * @param days        天数
     * @param warehouseId 仓库ID(可选)
     * @return 即将过期列表
     */
    List<Map<String, Object>> selectExpiringSoon(Integer days, String warehouseId);

    /**
     * 查询已过期的库存
     *
     * @param warehouseId 仓库ID(可选)
     * @return 已过期列表
     */
    List<Map<String, Object>> selectExpired(String warehouseId);

    /**
     * 按FIFO规则查询可用库存
     *
     * @param warehouseId 仓库ID
     * @param materialId  物料ID
     * @param quantity    需要数量
     * @return 可用库存列表（按入库时间排序）
     */
    List<Stock> selectAvailableStockFIFO(String warehouseId, String materialId, BigDecimal quantity);

    /**
     * 查询库存变动日志
     *
     * @param params 查询条件
     * @return 日志列表
     */
    List<Map<String, Object>> selectStockLogList(Map<String, Object> params);

    /**
     * 检查库存是否充足
     *
     * @param warehouseId 仓库ID
     * @param materialId  物料ID
     * @param quantity    需要数量
     * @return 是否充足
     */
    boolean checkStockSufficient(String warehouseId, String materialId, BigDecimal quantity);
}
