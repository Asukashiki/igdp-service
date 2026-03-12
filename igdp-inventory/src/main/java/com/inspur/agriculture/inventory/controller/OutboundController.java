 package com.inspur.agriculture.inventory.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.inspur.agriculture.inventory.domain.InventoryOutbound;
import com.inspur.agriculture.inventory.service.IInventoryOutboundService;
import com.inspur.common.core.controller.BaseController;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.core.page.TableDataInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/inventory/outbound")
public class OutboundController extends BaseController {

    @Autowired
    private IInventoryOutboundService outboundService;

    @GetMapping("/list")
    public TableDataInfo list(InventoryOutbound outbound,
                              @RequestParam(required = false) String outboundNo,
                              @RequestParam(required = false) String type,
                              @RequestParam(required = false) String status) {
        startPage();
        
        InventoryOutbound query = new InventoryOutbound();
        query.setOutboundNo(outboundNo);
        query.setType(type);
        query.setStatus(status);
        
        List<InventoryOutbound> list = outboundService.selectOutboundListWithWarehouse(query);
        return getDataTable(list);
    }

    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        InventoryOutbound outbound = outboundService.selectOutboundWithWarehouse(id);
        return AjaxResult.success(outbound);
    }

    @PostMapping("/create")
    public AjaxResult create(@RequestBody InventoryOutbound outbound) {
        return toAjax(outboundService.createOutbound(outbound));
    }

    @PutMapping
    public AjaxResult update(@RequestBody InventoryOutbound outbound) {
        return toAjax(outboundService.updateOutbound(outbound));
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
