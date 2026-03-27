package com.inspur.agriculture.inventory.controller;

import com.inspur.agriculture.inventory.domain.InventoryWarehouse;
import com.inspur.agriculture.inventory.service.IInventoryWarehouseService;
import com.inspur.common.core.controller.BaseController;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.core.page.TableDataInfo;
import com.inspur.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 仓库管理 Controller
 */
@RestController("inventoryWarehouseManageController")
@RequestMapping("/inventory/warehouse-manage")
public class WarehouseController extends BaseController {

    @Autowired
    private IInventoryWarehouseService warehouseService;

    @PreAuthorize("@ss.hasPermi('inventory:warehouse:list')")
    @GetMapping("/list")
    public TableDataInfo list(InventoryWarehouse warehouse) {
        startPage();
        if (!SecurityUtils.isSuperAdmin()) {
            String username = SecurityUtils.getUsername();
            if (username == null || username.trim().isEmpty()) {
                return getDataTable(java.util.Collections.emptyList());
            }
            warehouse.setCreateBy(username);
        }
        List<InventoryWarehouse> list = warehouseService.selectWarehouseList(warehouse);
        return getDataTable(list);
    }

    /**
     * 查询仓库选项列表（不需要权限，供下拉选择使用）
     */
    @GetMapping("/options")
    public AjaxResult getOptions(InventoryWarehouse warehouse) {
        List<InventoryWarehouse> list = warehouseService.selectWarehouseList(warehouse);
        return AjaxResult.success(list);
    }

    @GetMapping("/list-by-dept")
    public AjaxResult listByDept(@RequestParam("dept_id") String deptId,
                                 @RequestParam(value = "main_category", required = false) String mainCategory,
                                 @RequestParam(value = "sub_category", required = false) String subCategory,
                                 @RequestParam(value = "product", required = false) String product,
                                 @RequestParam(value = "productName", required = false) String productNameCamel,
                                 @RequestParam(value = "ProductName", required = false) String productName) {
        String queryProductName = firstNonBlank(productName, productNameCamel, product);
        return AjaxResult.success(warehouseService.selectDeptCategoryStock(deptId, mainCategory, subCategory, queryProductName));
    }

    private String firstNonBlank(String... values) {
        if (values == null) {
            return null;
        }
        for (String value : values) {
            if (value != null) {
                String trimmedValue = value.trim();
                if (!trimmedValue.isEmpty()) {
                    return trimmedValue;
                }
            }
        }
        return null;
    }



    @PreAuthorize("@ss.hasPermi('inventory:warehouse:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        if (!SecurityUtils.isSuperAdmin()) {
            String username = SecurityUtils.getUsername();
            if (username == null || username.trim().isEmpty()) {
                return AjaxResult.error("No permission to view this warehouse.");
            }
            InventoryWarehouse warehouse = warehouseService.selectWarehouseById(id);
            if (warehouse == null) {
                return AjaxResult.error("Warehouse not found.");
            }
            if (!username.equals(warehouse.getCreateBy())) {
                return AjaxResult.error("No permission to view this warehouse.");
            }
        }
        return AjaxResult.success(warehouseService.selectWarehouseById(id));
    }

    /**
     * 生成仓库编码
     */
    @PostMapping("/generate-code")
    public AjaxResult generateCode(@RequestBody Map<String, String> params) {
        try {
            String warehouseType = params.get("warehouseType");
            if (warehouseType == null || warehouseType.isEmpty()) {
                return AjaxResult.error("Warehouse type cannot be empty.");
            }

            String warehouseCode = warehouseService.generateWarehouseCode(warehouseType);

            Map<String, Object> result = new HashMap<>();
            result.put("warehouseCode", warehouseCode);

            return AjaxResult.success(result);
        } catch (Exception e) {
            return AjaxResult.error("Failed to generate warehouse code: " + e.getMessage());
        }
    }

    @PreAuthorize("@ss.hasPermi('inventory:warehouse:add')")
    @PostMapping
    public AjaxResult add(@RequestBody InventoryWarehouse warehouse) {
        return toAjax(warehouseService.createWarehouse(warehouse));
    }

    @PreAuthorize("@ss.hasPermi('inventory:warehouse:edit')")
    @PutMapping
    public AjaxResult edit(@RequestBody InventoryWarehouse warehouse) {
        return toAjax(warehouseService.updateWarehouse(warehouse));
    }

    @PreAuthorize("@ss.hasPermi('inventory:warehouse:edit')")
    @PutMapping("/warning")
    public AjaxResult updateWarning(@RequestBody Map<String, Object> params) {
        Object idValue = params.get("id");
        if (idValue == null) {
            return AjaxResult.error("Warehouse ID cannot be empty.");
        }

        Object maxStockValue = params.get("maxStock");
        if (maxStockValue == null) {
            maxStockValue = params.get("max_stock");
        }
        BigDecimal maxStock = null;
        if (maxStockValue instanceof Number) {
            maxStock = new BigDecimal(maxStockValue.toString());
        } else if (maxStockValue instanceof String) {
            String value = ((String) maxStockValue).trim();
            if (!value.isEmpty()) {
                maxStock = new BigDecimal(value);
            }
        }

        Long warehouseId = Long.valueOf(idValue.toString());
        InventoryWarehouse warehouse = warehouseService.selectWarehouseById(warehouseId);
        if (warehouse == null) {
            return AjaxResult.error("Warehouse not found.");
        }
        warehouse.setMaxStock(maxStock);
        return toAjax(warehouseService.updateWarehouse(warehouse));
    }

    @PreAuthorize("@ss.hasPermi('inventory:warehouse:remove')")
    @DeleteMapping("/{id}")
    public AjaxResult remove(@PathVariable Long id) {
        return toAjax(warehouseService.deleteWarehouse(id));
    }




    
}
