package com.inspur.agriculture.inventory.controller;

import com.inspur.agriculture.inventory.service.IInventoryStockBatchService;
import com.inspur.common.core.controller.BaseController;
import com.inspur.common.core.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/inventory/stock-batch")
public class InventoryStockBatchController extends BaseController {

    @Autowired
    private IInventoryStockBatchService stockBatchService;

    @GetMapping("/warehouse/{warehouseCode}")
    public AjaxResult listByWarehouse(@PathVariable String warehouseCode) {
        return AjaxResult.success(stockBatchService.listByWarehouseCode(warehouseCode));
    }

    @GetMapping("/detail")
    public AjaxResult detail(@RequestParam String batchNo, @RequestParam String warehouseCode) {
        return AjaxResult.success(stockBatchService.getBatchDetail(batchNo, warehouseCode));
    }
}
