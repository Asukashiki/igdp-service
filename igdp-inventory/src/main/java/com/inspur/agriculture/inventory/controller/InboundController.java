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
        LambdaQueryWrapper<InventoryInbound> wrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.isNotBlank(inboundNo)) {
            wrapper.like(InventoryInbound::getInboundNo, inboundNo);
        }
        if (StringUtils.isNotBlank(type)) {
            wrapper.eq(InventoryInbound::getType, type);
        }
        if (StringUtils.isNotBlank(status)) {
            if (status.contains(",")) {
                wrapper.in(InventoryInbound::getStatus, Arrays.asList(status.split(",")));
            } else {
                wrapper.eq(InventoryInbound::getStatus, status);
            }
        }
        wrapper.orderByDesc(InventoryInbound::getCreateTime);
        
        List<InventoryInbound> list = inboundService.list(wrapper);
        return getDataTable(list);
    }

    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        InventoryInbound inbound = inboundService.getById(id);
        return AjaxResult.success(inbound);
    }

    @PostMapping("/create")
    public AjaxResult create(@RequestBody InventoryInbound inbound) {
        return toAjax(inboundService.createInbound(inbound));
    }

    @PostMapping("/submit")
    public AjaxResult submit(@RequestBody InventoryInbound inbound) {
        return toAjax(inboundService.submitInbound(inbound.getId()));
    }

    @PostMapping("/audit")
    public AjaxResult audit(@RequestBody InventoryInbound inbound) {
        return toAjax(inboundService.auditInbound(inbound));
    }
}
