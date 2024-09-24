package com.inspur.web.controller.system;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.inspur.common.annotation.Log;
import com.inspur.common.core.controller.BaseController;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.core.page.TableDataInfo;
import com.inspur.common.enums.BusinessType;
import com.inspur.system.domain.SysOpenApp;
import com.inspur.system.service.ISysOpenAppService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author liyunlong
 * @version 1.0
 * @ClassName SysOpenAppController
 * @date 2024/7/26 9:52
 */
@RestController
@RequestMapping("/system/open-app")
public class SysOpenAppController extends BaseController {
    @Resource
    private ISysOpenAppService sysOpenAppService;

    /**
     * 列表
     */
    @SaCheckPermission("system:open-app:list")
    @GetMapping("/list")
    public TableDataInfo<?> list(SysOpenApp sysOpenApp) {
        startPage();
        List<SysOpenApp> list = sysOpenAppService.getList(sysOpenApp);
        return getDataTable(list);
    }

    /**
     * 单条数据
     */
    @GetMapping()
    public AjaxResult getById(@RequestParam String appid) {
        SysOpenApp openApp = sysOpenAppService.getById(appid);
        return AjaxResult.success(openApp);
    }

    /**
     * 新增保存
     */
    @SaCheckPermission("system:open-app:add")
    @Log(title = "开放应用管理", businessType = BusinessType.INSERT)
    @PostMapping()
    public AjaxResult add(@RequestBody SysOpenApp sysOpenApp) {
        return sysOpenAppService.addApp(sysOpenApp);
    }

    /**
     * 修改
     */
    @SaCheckPermission("system:open-app:edit")
    @Log(title = "开放应用管理", businessType = BusinessType.UPDATE)
    @PutMapping()
    public AjaxResult update(@RequestBody SysOpenApp sysOpenApp) {
        return sysOpenAppService.editApp(sysOpenApp);
    }

    /**
     * 修改状态
     */
    @SaCheckPermission("system:open-app:edit")
    @Log(title = "开放应用管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestParam("appid") String appid, @RequestParam("status") String status) {
        boolean result = sysOpenAppService.changeStatus(appid, status);
        if (result) {
            return AjaxResult.success();
        } else {
            return AjaxResult.error("修改失败");
        }
    }


    /**
     * 修改锁定状态
     */
    @SaCheckPermission("system:open-app:edit")
    @Log(title = "开放应用管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeInvokeStatus")
    public AjaxResult changeInvokeStatus(@RequestParam("appid") String appid, @RequestParam("status") String status) {
        boolean result = sysOpenAppService.changeInvokeStatus(appid, status);
        if (result) {
            return AjaxResult.success();
        } else {
            return AjaxResult.error("修改失败");
        }
    }

    /**
     * 删除
     */
    @SaCheckPermission("system:open-app:edit")
    @Log(title = "开放应用管理", businessType = BusinessType.UPDATE)
    @DeleteMapping()
    public AjaxResult removeApp(@RequestParam("appid") String appid) {
        boolean result = sysOpenAppService.removeApp(appid);
        if (result) {
            return AjaxResult.success();
        } else {
            return AjaxResult.error("删除失败");
        }
    }


}
