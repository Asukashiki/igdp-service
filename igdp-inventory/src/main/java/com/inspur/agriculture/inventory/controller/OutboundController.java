package com.inspur.agriculture.inventory.controller;

import com.inspur.agriculture.inventory.domain.InventoryOutbound;
import com.inspur.agriculture.inventory.service.IInventoryOutboundService;
import com.inspur.common.core.controller.BaseController;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.core.page.TableDataInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventory/outbound")
public class OutboundController extends BaseController {

    @Autowired
    private IInventoryOutboundService outboundService;

    @GetMapping("/list")
    public TableDataInfo list(InventoryOutbound outbound) {
        startPage();
        List<InventoryOutbound> list = outboundService.list();
        return getDataTable(list);
    }

    @PostMapping("/create")
    public AjaxResult create(@RequestBody InventoryOutbound outbound) {
        return toAjax(outboundService.createOutbound(outbound));
    }

    @PostMapping("/submit")
    public AjaxResult submit(@RequestBody InventoryOutbound outbound) {
        return toAjax(outboundService.submitOutbound(outbound.getId()));
    }

    @PostMapping("/audit")
    public AjaxResult audit(@RequestBody InventoryOutbound outbound) {
        return toAjax(outboundService.auditOutbound(outbound));
    }
}
