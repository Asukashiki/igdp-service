package com.inspur.web.controller.system;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.inspur.common.annotation.Log;
import com.inspur.common.core.controller.BaseController;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.core.page.TableDataInfo;
import com.inspur.common.enums.BusinessType;
import com.inspur.system.domain.SysWorkbenchItem;
import com.inspur.system.service.ISysWorkbenchItemService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author liyunlong
 * @version 1.0
 * @ClassName SysWorkbenchItemController
 * @date 2024/6/10 16:55
 */
@RestController
@RequestMapping("/system/workbench/item")
public class SysWorkbenchItemController extends BaseController {
    @Resource
    private ISysWorkbenchItemService workbenchItemService;


    /**
     * 获取列表
     */
    @SaCheckPermission("system:workbench-item:list")
    @GetMapping("/list")
    public TableDataInfo<?> list(SysWorkbenchItem item) {
        startPage();
        List<SysWorkbenchItem> list = workbenchItemService.selectItemList(item);
        return getDataTable(list);
    }


    /**
     * 查询单条
     */
    @SaCheckPermission("system:workbench-item:query")
    @GetMapping(value = {"/{id}"})
    public AjaxResult getInfo(@PathVariable("id") String id) {
        return success(workbenchItemService.getById(Integer.parseInt(id)));
    }

    /**
     * 新增项目
     */
    @SaCheckPermission("system:workbench-item:add")
    @Log(title = "工作台管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysWorkbenchItem item) {
        item.setCreateBy(getUsername());
        item.setCreateTime(LocalDateTime.now());
        item.setUserId(getUserId());
        item.setDeptId(getDeptId());
        return workbenchItemService.addItem(item);
    }

    /**
     * 修改项目
     */
    @SaCheckPermission("system:workbench-item:edit")
    @Log(title = "工作台管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysWorkbenchItem item) {
        item.setUpdateBy(getUsername());
        item.setUpdateTime(LocalDateTime.now());
        return workbenchItemService.updateItem(item);
    }

    /**
     * 删除项目
     */
    @SaCheckPermission("system:workbench-item:remove")
    @Log(title = "工作台管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    public AjaxResult remove(@PathVariable String id) {
        return workbenchItemService.deleteItemById(id);
    }
}
