package com.inspur.agriculture.inventory.controller;

import com.inspur.agriculture.inventory.domain.InventoryCheckOrder;
import com.inspur.agriculture.inventory.service.IInventoryCheckOrderService;
import com.inspur.common.core.controller.BaseController;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.core.page.TableDataInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventory/check")
public class CheckOrderController extends BaseController {

    @Autowired
    private IInventoryCheckOrderService checkOrderService;

    @PreAuthorize("@ss.hasPermi('inventory:check:list')")
    @GetMapping("/list")
    public TableDataInfo list(InventoryCheckOrder checkOrder) {
        startPage();
        List<InventoryCheckOrder> list = checkOrderService.list();
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('inventory:check:add')")
    @PostMapping("/create")
    public AjaxResult create(@RequestBody InventoryCheckOrder checkOrder) {
        return toAjax(checkOrderService.createCheckOrder(checkOrder));
    }

    @PreAuthorize("@ss.hasPermi('inventory:check:record')")
    @PostMapping("/record")
    public AjaxResult record(@RequestBody InventoryCheckOrder checkOrder) {
        return toAjax(checkOrderService.recordCheckResult(checkOrder));
    }

    @PreAuthorize("@ss.hasPermi('inventory:check:audit')")
    @PostMapping("/audit")
    public AjaxResult audit(@RequestBody InventoryCheckOrder checkOrder) {
        return toAjax(checkOrderService.auditCheckOrder(checkOrder));
    }
}
