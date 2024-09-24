package com.inspur.data.treating.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.inspur.common.annotation.Log;
import com.inspur.common.core.controller.BaseController;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.core.page.TableDataInfo;
import com.inspur.common.enums.BusinessType;
import com.inspur.data.treating.domain.UnifyStatisticsItem;
import com.inspur.data.treating.service.IUnifyStatisticsItemService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author liyunlong
 * @version 1.0
 * @ClassName UnifyStatisticsItemController
 * @date 2024/7/18 16:56
 */
@RestController
@RequestMapping("/data-treating/item")
public class UnifyStatisticsItemController extends BaseController {
    @Resource
    private IUnifyStatisticsItemService unifyStatisticsItemService;

    /**
     * 获取列表
     */
    @SaCheckPermission("data-treating:item:list")
    @GetMapping("/list")
    public TableDataInfo<?> getList(UnifyStatisticsItem query) {
        startPage();
        List<UnifyStatisticsItem> list = unifyStatisticsItemService.getList(query);
        return getDataTable(list);
    }

    /**
     * 添加保存
     */
    @SaCheckPermission("data-treating:item:add")
    @Log(title = "统一统计指标", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult addItem(@Validated @RequestBody UnifyStatisticsItem unifyStatisticsItem) {
        return unifyStatisticsItemService.addItem(unifyStatisticsItem);
    }

    /**
     * 更新保存
     */
    @SaCheckPermission("data-treating:item:edit")
    @Log(title = "统一统计指标", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult editItem(@Validated @RequestBody UnifyStatisticsItem unifyStatisticsItem) {
        return unifyStatisticsItemService.editItem(unifyStatisticsItem);
    }

    /**
     * 获取单条数据
     */
    @GetMapping("/query/{id}")
    public AjaxResult getItem(@PathVariable String id) {
        UnifyStatisticsItem item = unifyStatisticsItemService.getById(id);
        return AjaxResult.success(item);
    }


    /**
     * 删除数据
     */
    @SaCheckPermission("data-treating:item:remove")
    @Log(title = "统一统计指标", businessType = BusinessType.UPDATE)
    @DeleteMapping("/{id}")
    public AjaxResult removeItem(@PathVariable("id") String id) {
        boolean result = unifyStatisticsItemService.removeById(id);
        if (result) {
            return AjaxResult.success("删除成功");
        } else {
            return AjaxResult.error("没有相关数据");
        }
    }

    /**
     * 更新状态
     */
    @SaCheckPermission("data-treating:item:edit")
    @Log(title = "统一统计指标", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus/{id}/{status}")
    public AjaxResult updateStatus(@PathVariable("id") String id, @PathVariable("status") String status) {
        return unifyStatisticsItemService.updateStatus(id, status);
    }


}
