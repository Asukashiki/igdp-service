package com.inspur.agriculture.inventory.controller;

import com.inspur.agriculture.inventory.domain.InventoryStock;
import com.inspur.agriculture.inventory.service.IInventoryStockService;
import com.inspur.common.core.controller.BaseController;
import com.inspur.common.core.page.TableDataInfo;
import com.inspur.agriculture.inventory.domain.InventoryStockBatch;
import com.inspur.agriculture.inventory.service.IInventoryStockBatchService;
import com.inspur.common.core.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("inventoryStockController")
@RequestMapping("/inventory/stock")
public class InventoryStockController extends BaseController {

    @Autowired
    private IInventoryStockService stockService;

    @Autowired
    private IInventoryStockBatchService stockBatchService;

    @PreAuthorize("@ss.hasPermi('inventory:stock:query')")
    @GetMapping("/list")
    public TableDataInfo list(InventoryStock stock) {
        startPage();
        List<InventoryStock> list = stockService.selectStockList(stock);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('inventory:stock:query')")
    @GetMapping("/detail")
    public AjaxResult detail(@RequestParam Long id) {
        return AjaxResult.success(stockService.selectStockById(id));
    }

    @PreAuthorize("@ss.hasPermi('inventory:stock:query')")
    @GetMapping("/batch/list")
    public TableDataInfo batchList(InventoryStockBatch batch) {
        startPage();
        List<InventoryStockBatch> list = stockBatchService.selectBatchList(batch);
        return getDataTable(list);
    }
}
