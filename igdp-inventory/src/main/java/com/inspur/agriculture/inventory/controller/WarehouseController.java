package com.inspur.agriculture.inventory.controller;

import com.inspur.agriculture.inventory.domain.InventoryWarehouse;
import com.inspur.agriculture.inventory.domain.InventoryWarehouseOwner;
import com.inspur.agriculture.inventory.service.IInventoryWarehouseService;
import com.inspur.agriculture.inventory.service.IInventoryWarehouseOwnerService;
import com.inspur.common.core.controller.BaseController;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.core.page.TableDataInfo;
import com.inspur.common.utils.SecurityUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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

    /**
     * 查询仓库列表（需要权限）
     */
    @Autowired
    private IInventoryWarehouseOwnerService ownerService;

    @PreAuthorize("@ss.hasPermi('inventory:warehouse:list')")
    @GetMapping("/list")
    public TableDataInfo list(InventoryWarehouse warehouse) {
        startPage();
        if (!SecurityUtils.isSuperAdmin()) {
            String userId = SecurityUtils.getUserId();
            if (userId == null || userId.trim().isEmpty()) {
                return getDataTable(java.util.Collections.emptyList());
            }
            LambdaQueryWrapper<InventoryWarehouseOwner> ownerWrapper = new LambdaQueryWrapper<>();
            ownerWrapper.eq(InventoryWarehouseOwner::getOwnerUserId, userId);
            List<InventoryWarehouseOwner> owners = ownerService.list(ownerWrapper);
            if (owners == null || owners.isEmpty()) {
                return getDataTable(java.util.Collections.emptyList());
            }
            java.util.List<Long> warehouseIds = new java.util.ArrayList<>();
            for (InventoryWarehouseOwner owner : owners) {
                if (owner.getWarehouseId() != null) {
                    warehouseIds.add(owner.getWarehouseId());
                }
            }
            if (warehouseIds.isEmpty()) {
                return getDataTable(java.util.Collections.emptyList());
            }
            warehouse.setIds(warehouseIds);
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
                                 @RequestParam(value = "sub_category", required = false) String subCategory) {
        return AjaxResult.success(warehouseService.selectDeptCategoryStock(deptId, mainCategory, subCategory));
    }



    @PreAuthorize("@ss.hasPermi('inventory:warehouse:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        if (!SecurityUtils.isSuperAdmin()) {
            String userId = SecurityUtils.getUserId();
            if (userId == null || userId.trim().isEmpty()) {
                return AjaxResult.error("无权限查看该仓库");
            }
            LambdaQueryWrapper<InventoryWarehouseOwner> ownerWrapper = new LambdaQueryWrapper<>();
            ownerWrapper.eq(InventoryWarehouseOwner::getWarehouseId, id);
            ownerWrapper.eq(InventoryWarehouseOwner::getOwnerUserId, userId);
            boolean hasOwner = ownerService.count(ownerWrapper) > 0;
            if (!hasOwner) {
                return AjaxResult.error("无权限查看该仓库");
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
                return AjaxResult.error("仓库类型不能为空");
            }

            String warehouseCode = warehouseService.generateWarehouseCode(warehouseType);

            Map<String, Object> result = new HashMap<>();
            result.put("warehouseCode", warehouseCode);

            return AjaxResult.success(result);
        } catch (Exception e) {
            return AjaxResult.error("生成仓库编码失败: " + e.getMessage());
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
            return AjaxResult.error("仓库ID不能为空");
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
            return AjaxResult.error("仓库不存在");
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
