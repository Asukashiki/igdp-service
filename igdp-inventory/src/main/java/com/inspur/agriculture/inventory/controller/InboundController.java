package com.inspur.agriculture.inventory.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.inspur.agriculture.inventory.domain.InventoryInbound;
import com.inspur.agriculture.inventory.service.IInventoryInboundService;
import com.inspur.common.core.controller.BaseController;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.core.page.TableDataInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/inventory/inbound")
public class InboundController extends BaseController {

    @Autowired
    private IInventoryInboundService inboundService;

    @GetMapping("/list")
    public TableDataInfo list(InventoryInbound inbound,
                              @RequestParam(required = false) String inboundNo,
                              @RequestParam(required = false) String type,
                              @RequestParam(required = false) String status) {
        startPage();
        
        InventoryInbound query = new InventoryInbound();
        query.setInboundNo(inboundNo);
        query.setType(type);
        query.setStatus(status);
        
        List<InventoryInbound> list = inboundService.selectInboundListWithWarehouse(query);
        return getDataTable(list);
    }

    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        InventoryInbound inbound = inboundService.selectInboundWithWarehouse(id);
        return AjaxResult.success(inbound);
    }

    @PostMapping("/create")
    public AjaxResult create(@RequestBody InventoryInbound inbound) {
        return toAjax(inboundService.createInbound(inbound));
    }

    @PutMapping
    public AjaxResult update(@RequestBody InventoryInbound inbound) {
        return toAjax(inboundService.updateInbound(inbound));
    }

    @PostMapping("/submit")
    public AjaxResult submit(@RequestBody InventoryInbound inbound) {
        return toAjax(inboundService.submitInbound(inbound.getId()));
    }

    @PostMapping("/audit")
    public AjaxResult audit(@RequestBody InventoryInbound inbound) {
        return toAjax(inboundService.auditInbound(inbound));
    }

    @DeleteMapping("/{id}")
    public AjaxResult delete(@PathVariable Long id) {
        return toAjax(inboundService.deleteInbound(id));
    }
}
