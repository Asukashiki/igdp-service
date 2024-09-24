package com.inspur.web.controller.system;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.inspur.common.annotation.Log;
import com.inspur.common.core.controller.BaseController;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.core.page.TableDataInfo;
import com.inspur.common.enums.BusinessType;
import com.inspur.system.domain.SysFastItem;
import com.inspur.system.service.ISysFastItemService;
import com.inspur.system.service.ISysUserFastItemService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author liyunlong
 * @version 1.0
 * @ClassName SysFastItemController
 * @date 2024/6/14 18:03
 */
@RestController
@RequestMapping("/system/fast-item")
public class SysFastItemController extends BaseController {
    @Resource
    private ISysFastItemService fastItemService;
    @Resource
    private ISysUserFastItemService userFastItemService;

    /**
     * 列表
     */
    @SaCheckPermission("system:fast-item:list")
    @GetMapping("/list")
    public TableDataInfo<?> list(SysFastItem fastItem) {

        startPage();
        List<SysFastItem> list = fastItemService.getList(fastItem);
        return getDataTable(list);
    }

    /**
     * 获取单条信息
     */
    @SaCheckPermission("system:fast-item:query")
    @GetMapping("/{id}")
    public AjaxResult get(@PathVariable("id") String id) {
        return AjaxResult.success(fastItemService.getById(id));
    }

    /**
     * 新增
     */
    @SaCheckPermission("system:fast-item:add")
    @Log(title = "快速发起", businessType = BusinessType.INSERT)
    @PostMapping()
    public AjaxResult addItem(@RequestBody SysFastItem item) {
        return fastItemService.addItem(item);
    }

    /**
     * 修改项目
     */
    @SaCheckPermission("system:fast-item:edit")
    @Log(title = "快速发起", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysFastItem item) {
        item.setUpdateBy(getUsername());
        item.setUpdateTime(LocalDateTime.now());
        return fastItemService.updateItem(item);
    }

    /**
     * 删除用户
     */
    @SaCheckPermission("system:fast-item:remove")
    @Log(title = "快速发起", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    public AjaxResult remove(@PathVariable String id) {
        return fastItemService.deleteItem(id);
    }

    /**
     * 获取项目下拉选择
     */
    @GetMapping("/optionsSelect")
    public AjaxResult optionsSelect() {
        List<SysFastItem> list = fastItemService.getList(new SysFastItem());
        return AjaxResult.success(list);
    }
}
