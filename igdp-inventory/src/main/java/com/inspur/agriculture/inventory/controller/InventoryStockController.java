package com.inspur.agriculture.inventory.controller;

import com.inspur.agriculture.inventory.domain.InventoryStock;
import com.inspur.agriculture.inventory.service.IInventoryStockService;
import com.inspur.common.core.controller.BaseController;
import com.inspur.common.core.page.TableDataInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/inventory/stock")
public class InventoryStockController extends BaseController {

    @Autowired
    private IInventoryStockService stockService;

    @GetMapping("/list")
    public TableDataInfo list(InventoryStock stock) {
        startPage();
        List<InventoryStock> list = stockService.selectStockList(stock);
        return getDataTable(list);
    }

    @GetMapping("/detail/{warehouseId}")
    public TableDataInfo listByWarehouse(@PathVariable Long warehouseId) {
        InventoryStock stock = new InventoryStock();
        stock.setWarehouseId(warehouseId);
        startPage();
        List<InventoryStock> list = stockService.selectStockList(stock);
        return getDataTable(list);
    }
}
