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
import org.springframework.web.bind.annotation.RequestParam;
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
    public AjaxResult createInbound(@RequestBody InventoryInbound inbound, @RequestParam String flag) {
        validateFlag(flag);
        inbound.setFlag(flag);
        boolean ok = inboundService.createInbound(inbound);
        if (ok) {
            boolean submitted = inboundService.submitInbound(inbound.getId());
            if (!submitted) {
                return AjaxResult.error("Inbound order created but failed to submit for approval.");
            }
            return AjaxResult.success("Inbound order created and submitted for approval.", inbound.getId());
        }
        return AjaxResult.error("Failed to create inbound order.");
    }

    @PostMapping("/outbound")
    public AjaxResult createOutbound(@RequestBody InventoryOutbound outbound, @RequestParam String flag) {
        validateFlag(flag);
        outbound.setFlag(flag);
        boolean ok = outboundService.createOutbound(outbound);
        if (ok) {
            boolean submitted = outboundService.submitOutbound(outbound.getId());
            if (!submitted) {
                return AjaxResult.error("Outbound order created but failed to submit for approval.");
            }
            return AjaxResult.success("Outbound order created and submitted for approval.", outbound.getId());
        }
        return AjaxResult.error("Failed to create outbound order.");
    }

    private void validateFlag(String flag) {
        if (!"0".equals(flag) && !"1".equals(flag)) {
            throw new IllegalArgumentException("flag参数只能为0或1");
        }
    }
}
