package com.inspur.agriculture.inventory.controller;

import com.inspur.agriculture.inventory.domain.InventoryWarningRule;
import com.inspur.agriculture.inventory.service.IInventoryWarningRuleService;
import com.inspur.common.core.controller.BaseController;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.core.page.TableDataInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventory/warning")
public class WarningRuleController extends BaseController {

    @Autowired
    private IInventoryWarningRuleService warningRuleService;

    @PreAuthorize("@ss.hasPermi('inventory:warning:list')")
    @GetMapping("/list")
    public TableDataInfo list(InventoryWarningRule rule) {
        startPage();
        List<InventoryWarningRule> list = warningRuleService.list();
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('inventory:warning:add')")
    @PostMapping
    public AjaxResult add(@RequestBody InventoryWarningRule rule) {
        return toAjax(warningRuleService.save(rule));
    }

    @PreAuthorize("@ss.hasPermi('inventory:warning:edit')")
    @PutMapping
    public AjaxResult edit(@RequestBody InventoryWarningRule rule) {
        return toAjax(warningRuleService.updateById(rule));
    }

    @PreAuthorize("@ss.hasPermi('inventory:warning:remove')")
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable List<Long> ids) {
        return toAjax(warningRuleService.removeByIds(ids));
    }
}
