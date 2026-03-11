package com.inspur.agriculture.inventory.controller;

import com.inspur.agriculture.inventory.domain.InventoryWarehouse;
import com.inspur.agriculture.inventory.service.IInventoryWarehouseService;
import com.inspur.common.core.controller.BaseController;
import com.inspur.common.core.page.TableDataInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/inventory/warehouse")
public class InventoryWarehouseController extends BaseController {

    @Autowired
    private IInventoryWarehouseService warehouseService;

    @GetMapping("/list")
    public TableDataInfo list(InventoryWarehouse warehouse) {
        startPage();
        List<InventoryWarehouse> list = warehouseService.selectWarehouseList(warehouse);
        return getDataTable(list);
    }
}
