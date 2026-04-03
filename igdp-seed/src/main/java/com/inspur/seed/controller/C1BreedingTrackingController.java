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

    @PostMapping("/submit/{id}")
    public AjaxResult submit(@PathVariable String id) {
        boolean result = c1BreedingTrackingService.submit(id);
        return result ? AjaxResult.success("提交审核成功") : AjaxResult.error("提交审核失败");
    }

    @PostMapping("/approve")
    public AjaxResult approve(@RequestBody Map<String, String> params) {
        boolean result = c1BreedingTrackingService.approve(params.get("id"), params.get("auditComment"));
        return result ? AjaxResult.success("审核通过") : AjaxResult.error("审核失败");
    }

    @PostMapping("/reject")
    public AjaxResult reject(@RequestBody Map<String, String> params) {
        boolean result = c1BreedingTrackingService.reject(params.get("id"), params.get("auditComment"));
        return result ? AjaxResult.success("审核驳回成功") : AjaxResult.error("审核驳回失败");
    }
}
