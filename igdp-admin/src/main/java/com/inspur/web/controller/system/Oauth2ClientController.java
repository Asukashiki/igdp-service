package com.inspur.web.controller.system;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.inspur.common.annotation.Log;
import com.inspur.common.core.controller.BaseController;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.core.page.TableDataInfo;
import com.inspur.common.enums.BusinessType;
import com.inspur.common.utils.poi.ExcelUtil;
import com.inspur.system.domain.Oauth2Client;
import com.inspur.system.service.IOauth2ClientService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * @author liyunlong
 * @date 2024/1/29
 */
@RestController
@RequestMapping("/system/oauth2Client")
public class Oauth2ClientController extends BaseController {
    @Resource
    private IOauth2ClientService oauth2ClientService;

    /**
     * 查询Oath2单点客户端信息列表
     */
    @SaCheckPermission("system:oauth2Client:list")
    @GetMapping("/list")
    public TableDataInfo list(Oauth2Client oauth2Client)
    {
        startPage();
        List<Oauth2Client> list = oauth2ClientService.selectList(oauth2Client);
        return getDataTable(list);
    }

    /**
     * 导出Oath2单点客户端信息列表
     */
    @SaCheckPermission("system:oauth2Client:export")
    @Log(title = "Oath2单点客户端信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Oauth2Client oauth2Client)
    {
        List<Oauth2Client> list = oauth2ClientService.selectList(oauth2Client);
        ExcelUtil<Oauth2Client> util = new ExcelUtil<>(Oauth2Client.class);
        util.exportExcel(response, list, "Oath2单点客户端信息数据");
    }

    /**
     * 获取Oath2单点客户端信息详细信息
     */
    @SaCheckPermission("system:oauth2Client:query")
    @GetMapping(value = "/{clientId}")
    public AjaxResult getInfo(@PathVariable("clientId") String clientId)
    {
        return success(oauth2ClientService.getById(clientId));
    }

    /**
     * 新增Oath2单点客户端信息
     */
    @SaCheckPermission("system:oauth2Client:add")
    @Log(title = "Oath2单点客户端信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Oauth2Client oauth2Client)
    {
        return oauth2ClientService.addClient(oauth2Client);
    }

    /**
     * 修改Oath2单点客户端信息
     */
    @SaCheckPermission("system:oauth2Client:edit")
    @Log(title = "Oath2单点客户端信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Oauth2Client oauth2Client)
    {
        return oauth2ClientService.updateClient(oauth2Client);
    }

    /**
     * 删除Oath2单点客户端信息
     */
    @SaCheckPermission("system:oauth2Client:remove")
    @Log(title = "Oath2单点客户端信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{clientIds}")
    public AjaxResult remove(@PathVariable String[] clientIds)
    {
        return toAjax(oauth2ClientService.removeByIds(Arrays.asList(clientIds)));
    }

}
