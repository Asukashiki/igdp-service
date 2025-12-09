package com.inspur.seed.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.seed.domain.entity.C1BreedingTracking;
import com.inspur.seed.service.IC1BreedingTrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/seed/c1-breeding-tracking")
public class C1BreedingTrackingController {

    @Autowired
    private IC1BreedingTrackingService c1BreedingTrackingService;

    @PostMapping("/list")
    public AjaxResult list(@RequestBody Map<String, Object> params) {
        IPage<C1BreedingTracking> page = c1BreedingTrackingService.pageList(params);
        Map<String, Object> result = new HashMap<>();
        result.put("records", page.getRecords());
        result.put("total", page.getTotal());
        return AjaxResult.success(result);
    }

    @GetMapping("/getById/{id}")
    public AjaxResult getById(@PathVariable String id) {
        C1BreedingTracking entity = c1BreedingTrackingService.getDetailById(id);
        if (entity == null) return AjaxResult.error("数据不存在");
        return AjaxResult.success(entity);
    }

    @PostMapping("/add")
    public AjaxResult add(@RequestBody C1BreedingTracking entity) {
        boolean result = c1BreedingTrackingService.add(entity);
        return result ? AjaxResult.success("新增成功") : AjaxResult.error("新增失败");
    }

    @PostMapping("/update")
    public AjaxResult update(@RequestBody C1BreedingTracking entity) {
        if (entity.getId() == null) return AjaxResult.error("ID不能为空");
        boolean result = c1BreedingTrackingService.update(entity);
        return result ? AjaxResult.success("更新成功") : AjaxResult.error("更新失败");
    }

    @PostMapping("/delete")
    public AjaxResult delete(@RequestBody List<String> ids) {
        if (ids == null || ids.isEmpty()) return AjaxResult.error("请选择要删除的数据");
        boolean result = c1BreedingTrackingService.deleteByIds(ids);
        return result ? AjaxResult.success("删除成功") : AjaxResult.error("删除失败");
    }
}
