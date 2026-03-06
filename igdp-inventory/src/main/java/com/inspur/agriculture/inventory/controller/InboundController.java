package com.inspur.agriculture.inventory.controller;

import com.inspur.agriculture.inventory.domain.InventoryInbound;
import com.inspur.agriculture.inventory.service.IInventoryInboundService;
import com.inspur.common.core.controller.BaseController;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.core.page.TableDataInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventory/inbound")
public class InboundController extends BaseController {

    @Autowired
    private IInventoryInboundService inboundService;

    @GetMapping("/list")
    public TableDataInfo list(InventoryInbound inbound) {
        startPage();
        // Simple query
        List<InventoryInbound> list = inboundService.list();
        return getDataTable(list);
    }

    @PostMapping("/create")
    public AjaxResult create(@RequestBody InventoryInbound inbound) {
        return toAjax(inboundService.createInbound(inbound));
    }

    @PostMapping("/approve")
    public AjaxResult approve(@RequestBody InventoryInbound inbound) {
        // Assume ID is passed
        return toAjax(inboundService.approveInbound(inbound.getId()));
    }
}
