package com.inspur.agriculture.inventory.controller;

import com.inspur.agriculture.inventory.domain.InventoryInbound;
import com.inspur.agriculture.inventory.domain.InventoryOutbound;
import com.inspur.agriculture.inventory.service.IInventoryInboundService;
import com.inspur.agriculture.inventory.service.IInventoryOutboundService;
import com.inspur.common.core.controller.BaseController;
import com.inspur.common.core.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Open API for external systems to create inbound/outbound orders.
 */
@RestController
@RequestMapping("/inventory/open")
public class OpenInventoryController extends BaseController {

    @Autowired
    private IInventoryInboundService inboundService;

    @Autowired
    private IInventoryOutboundService outboundService;

    @PostMapping("/inbound")
    public AjaxResult createInbound(@RequestBody InventoryInbound inbound) {
        boolean ok = inboundService.createInbound(inbound);
        if (ok) {
            return AjaxResult.success("Inbound order created.", inbound.getId());
        }
        return AjaxResult.error("Failed to create inbound order.");
    }

    @PostMapping("/outbound")
    public AjaxResult createOutbound(@RequestBody InventoryOutbound outbound) {
        boolean ok = outboundService.createOutbound(outbound);
        if (ok) {
            return AjaxResult.success("Outbound order created.", outbound.getId());
        }
        return AjaxResult.error("Failed to create outbound order.");
    }
}
