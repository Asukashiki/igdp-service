package com.inspur.agriculture.inventory.controller;

import com.inspur.agriculture.inventory.domain.InventoryWarehouse;
import com.inspur.agriculture.inventory.service.IInventoryWarehouseService;
import com.inspur.common.core.controller.BaseController;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.core.page.TableDataInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @PreAuthorize("@ss.hasPermi('inventory:warehouse:list')")
    @GetMapping("/list")
    public TableDataInfo list(InventoryWarehouse warehouse) {
        startPage();
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

    @PreAuthorize("@ss.hasPermi('inventory:warehouse:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        return AjaxResult.success(warehouseService.selectWarehouseById(id));
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

    @PreAuthorize("@ss.hasPermi('inventory:warehouse:remove')")
    @DeleteMapping("/{id}")
    public AjaxResult remove(@PathVariable Long id) {
        return toAjax(warehouseService.deleteWarehouse(id));
    }
}
