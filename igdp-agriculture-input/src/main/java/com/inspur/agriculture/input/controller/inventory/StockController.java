package com.inspur.agriculture.input.controller.inventory;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.inspur.agriculture.input.dto.inventory.StockLogQueryDTO;
import com.inspur.agriculture.input.dto.inventory.StockQueryDTO;
import com.inspur.agriculture.input.service.inventory.IStockService;
import com.inspur.agriculture.input.service.inventory.IWarehouseService;
import com.inspur.common.core.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 库存管理 Controller
 *
 * @author inspur
 * @date 2025-12-03
 */
@RestController
@RequestMapping("/inventory/stock")
public class StockController {

    @Autowired
    private IStockService stockService;

    @Autowired
    private IWarehouseService warehouseService;

    /**
     * 查询库存列表
     *
     * @param queryDTO 查询条件
     * @param page 页码
     * @param pageSize 每页数量
     * @return 库存列表
     */
    @GetMapping("/query")
    public AjaxResult query(
            StockQueryDTO queryDTO,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer pageSize
    ) {
        try {
            // 构建查询参数
            Map<String, Object> params = new HashMap<>();
            if (queryDTO.getWarehouseId() != null && !queryDTO.getWarehouseId().isEmpty()) {
                params.put("warehouseId", queryDTO.getWarehouseId());
            }
            if (queryDTO.getMaterialId() != null && !queryDTO.getMaterialId().isEmpty()) {
                params.put("materialId", queryDTO.getMaterialId());
            }
            if (queryDTO.getMaterialBatchId() != null && !queryDTO.getMaterialBatchId().isEmpty()) {
                params.put("materialBatchId", queryDTO.getMaterialBatchId());
            }

            // 开启分页
            PageHelper.startPage(page, pageSize);
            List<Map<String, Object>> list = stockService.selectStockList(params);
            PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(list);

            // 构建返回结果
            Map<String, Object> result = new HashMap<>();
            result.put("total", pageInfo.getTotal());
            result.put("page", pageInfo.getPageNum());
            result.put("page_size", pageInfo.getPageSize());
            result.put("items", pageInfo.getList());

            return AjaxResult.success(result);
        } catch (Exception e) {
            return AjaxResult.error("查询库存列表失败: " + e.getMessage());
        }
    }

    /**
     * 查询库存变动日志
     *
     * @param queryDTO 查询条件
     * @param page 页码
     * @param pageSize 每页数量
     * @return 库存变动日志列表
     */
    @GetMapping("/logs")
    public AjaxResult logs(
            StockLogQueryDTO queryDTO,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer pageSize
    ) {
        try {
            // 构建查询参数
            Map<String, Object> params = new HashMap<>();
            if (queryDTO.getWarehouseId() != null && !queryDTO.getWarehouseId().isEmpty()) {
                params.put("warehouseId", queryDTO.getWarehouseId());
            }
            if (queryDTO.getMaterialId() != null && !queryDTO.getMaterialId().isEmpty()) {
                params.put("materialId", queryDTO.getMaterialId());
            }
            if (queryDTO.getOperationType() != null && !queryDTO.getOperationType().isEmpty()) {
                params.put("operationType", queryDTO.getOperationType());
            }
            if (queryDTO.getStartDate() != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                params.put("startDate", sdf.format(queryDTO.getStartDate()));
            }
            if (queryDTO.getEndDate() != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                params.put("endDate", sdf.format(queryDTO.getEndDate()));
            }

            // 开启分页
            PageHelper.startPage(page, pageSize);
            List<Map<String, Object>> list = stockService.selectStockLogList(params);
            PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(list);

            // 构建返回结果
            Map<String, Object> result = new HashMap<>();
            result.put("total", pageInfo.getTotal());
            result.put("page", pageInfo.getPageNum());
            result.put("page_size", pageInfo.getPageSize());
            result.put("items", pageInfo.getList());

            return AjaxResult.success(result);
        } catch (Exception e) {
            return AjaxResult.error("查询库存日志失败: " + e.getMessage());
        }
    }

    /**
     * 检查仓库容量
     *
     * @param warehouseId 仓库ID
     * @return 仓库容量信息
     */
    @GetMapping("/warehouse/{warehouseId}/capacity")
    public AjaxResult checkCapacity(@PathVariable String warehouseId) {
        try {
            // 查询仓库信息
            com.inspur.agriculture.input.vo.inventory.WarehouseVO warehouse =
                warehouseService.getWarehouseById(Long.parseLong(warehouseId));

            if (warehouse == null) {
                return AjaxResult.error("仓库不存在");
            }

            // 构建返回结果
            Map<String, Object> result = new HashMap<>();
            result.put("warehouse_id", warehouseId);
            result.put("warehouse_name", warehouse.getWarehouseName());
            result.put("total_capacity", warehouse.getCapacity());
            result.put("current_usage", warehouse.getUsedCapacity());

            BigDecimal remainingCapacity = warehouse.getCapacity().subtract(warehouse.getUsedCapacity());
            result.put("remaining_capacity", remainingCapacity);

            // 计算使用率百分比
            BigDecimal usagePercentage = BigDecimal.ZERO;
            if (warehouse.getCapacity().compareTo(BigDecimal.ZERO) > 0) {
                usagePercentage = warehouse.getUsedCapacity()
                    .divide(warehouse.getCapacity(), 4, BigDecimal.ROUND_HALF_UP)
                    .multiply(new BigDecimal(100))
                    .setScale(2, BigDecimal.ROUND_HALF_UP);
            }
            result.put("usage_percentage", usagePercentage);

            // 判断状态
            String status = "normal";
            if (usagePercentage.compareTo(new BigDecimal(90)) >= 0) {
                status = "critical";
            } else if (usagePercentage.compareTo(new BigDecimal(80)) >= 0) {
                status = "warning";
            }
            result.put("status", status);

            return AjaxResult.success(result);
        } catch (Exception e) {
            return AjaxResult.error("查询仓库容量失败: " + e.getMessage());
        }
    }

    /**
     * 查询库存详情
     *
     * @param id 库存ID
     * @return 库存详情
     */
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable String id) {
        try {
            Map<String, Object> stock = stockService.selectStockById(id);
            if (stock == null) {
                return AjaxResult.error("库存记录不存在");
            }
            return AjaxResult.success(stock);
        } catch (Exception e) {
            return AjaxResult.error("查询库存详情失败: " + e.getMessage());
        }
    }

    /**
     * 统计仓库总库存
     *
     * @param warehouseId 仓库ID
     * @return 总库存统计
     */
    @GetMapping("/warehouse/{warehouseId}/sum")
    public AjaxResult sumWarehouseStock(@PathVariable String warehouseId) {
        try {
            Map<String, Object> stats = stockService.sumWarehouseStock(warehouseId);
            return AjaxResult.success(stats);
        } catch (Exception e) {
            return AjaxResult.error("统计仓库库存失败: " + e.getMessage());
        }
    }

    /**
     * 统计物料库存
     *
     * @param materialId 物料ID
     * @return 物料库存统计
     */
    @GetMapping("/material/{materialId}/sum")
    public AjaxResult sumMaterialStock(@PathVariable String materialId) {
        try {
            Map<String, Object> stats = stockService.sumMaterialStock(materialId);
            return AjaxResult.success(stats);
        } catch (Exception e) {
            return AjaxResult.error("统计物料库存失败: " + e.getMessage());
        }
    }

    /**
     * 查询库存不足的物料
     *
     * @param warehouseId 仓库ID（可选）
     * @param minQuantity 最小库存数量
     * @return 库存不足列表
     */
    @GetMapping("/low-stock")
    public AjaxResult lowStock(
            @RequestParam(required = false) String warehouseId,
            @RequestParam(defaultValue = "10") BigDecimal minQuantity
    ) {
        try {
            List<Map<String, Object>> list = stockService.selectLowStock(warehouseId, minQuantity);
            return AjaxResult.success(list);
        } catch (Exception e) {
            return AjaxResult.error("查询库存不足物料失败: " + e.getMessage());
        }
    }

    /**
     * 查询即将过期的库存
     *
     * @param days 天数（多少天内过期）
     * @param warehouseId 仓库ID（可选）
     * @return 即将过期列表
     */
    @GetMapping("/expiring-soon")
    public AjaxResult expiringSoon(
            @RequestParam(defaultValue = "30") Integer days,
            @RequestParam(required = false) String warehouseId
    ) {
        try {
            List<Map<String, Object>> list = stockService.selectExpiringSoon(days, warehouseId);
            return AjaxResult.success(list);
        } catch (Exception e) {
            return AjaxResult.error("查询即将过期库存失败: " + e.getMessage());
        }
    }

    /**
     * 查询已过期的库存
     *
     * @param warehouseId 仓库ID（可选）
     * @return 已过期列表
     */
    @GetMapping("/expired")
    public AjaxResult expired(@RequestParam(required = false) String warehouseId) {
        try {
            List<Map<String, Object>> list = stockService.selectExpired(warehouseId);
            return AjaxResult.success(list);
        } catch (Exception e) {
            return AjaxResult.error("查询已过期库存失败: " + e.getMessage());
        }
    }

    /**
     * 检查库存是否充足
     *
     * @param warehouseId 仓库ID
     * @param materialId 物料ID
     * @param quantity 需要数量
     * @return 是否充足
     */
    @GetMapping("/check-sufficient")
    public AjaxResult checkSufficient(
            @RequestParam String warehouseId,
            @RequestParam String materialId,
            @RequestParam BigDecimal quantity
    ) {
        try {
            boolean sufficient = stockService.checkStockSufficient(warehouseId, materialId, quantity);
            Map<String, Object> result = new HashMap<>();
            result.put("sufficient", sufficient);
            result.put("warehouse_id", warehouseId);
            result.put("material_id", materialId);
            result.put("required_quantity", quantity);
            return AjaxResult.success(result);
        } catch (Exception e) {
            return AjaxResult.error("检查库存失败: " + e.getMessage());
        }
    }
}
