package com.inspur.web.controller.system;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.inspur.common.annotation.Log;
import com.inspur.common.core.controller.BaseController;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.core.domain.entity.SysApp;
import com.inspur.common.enums.BusinessType;
import com.inspur.system.service.ISysAppService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 系统应用相关接口
 *
 * @author liyunlong
 * @version 1.0
 * @ClassName SysAppController
 * @date 2024/6/9 10:27
 */
@RestController
@RequestMapping("/system/app")
public class SysAppController extends BaseController {
    @Resource
    private ISysAppService appService;

    /**
     * 获取所有应用信息列表
     */
    @GetMapping("/all")
    public AjaxResult getAll() {
        List<SysApp> appList = appService.list();
        return success(appList);
    }

    @GetMapping("/{id}")
    public AjaxResult getById(@PathVariable("id") String appId) {
        return success(appService.getById(appId));
    }

    /**
     * 新增保存
     */
    @SaCheckPermission("system:app:add")
    @Log(title = "应用管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysApp app) {

        return success();
    }

    /**
     * 更新
     */
    @SaCheckPermission("system:app:edit")
    @Log(title = "应用管理", businessType = BusinessType.INSERT)
    @PutMapping
    public AjaxResult edit(@RequestBody SysApp app) {
        return success();
    }

    /**
     * 删除
     */
    @SaCheckPermission("system:app:add")
    @Log(title = "应用管理", businessType = BusinessType.INSERT)
    @DeleteMapping("/{id}")
    public AjaxResult remove(@PathVariable("id") String appId) {
        appService.removeById(appId);
        return success();
    }

}
