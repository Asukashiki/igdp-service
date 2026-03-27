package com.inspur.seed.controller;

import com.inspur.common.core.controller.BaseController;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.core.page.TableDataInfo;
import com.inspur.seed.domain.MultiplierReport;
import com.inspur.seed.service.IMultiplierReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Multiplier Report Controller
 */
@RestController
@RequestMapping("/seed/multiplier-report")
public class MultiplierReportController extends BaseController {

    @Autowired
    private IMultiplierReportService multiplierReportService;

    @GetMapping("/list")
    public TableDataInfo list(MultiplierReport query) {
        startPage();
        List<MultiplierReport> list = multiplierReportService.selectList(query);
        return getDataTable(list);
    }

    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        return AjaxResult.success(multiplierReportService.selectById(id));
    }

    @PostMapping
    public AjaxResult add(@RequestBody MultiplierReport report) {
        return toAjax(multiplierReportService.create(report));
    }

    @PutMapping
    public AjaxResult edit(@RequestBody MultiplierReport report) {
        return toAjax(multiplierReportService.edit(report));
    }

    @DeleteMapping("/{id}")
    public AjaxResult remove(@PathVariable Long id) {
        return toAjax(multiplierReportService.remove(id));
    }
}
