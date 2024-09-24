package com.inspur.web.controller.monitor;

import cn.dev33.satoken.annotation.SaCheckPermission;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.framework.web.domain.Server;

/**
 * 服务器监控
 * 
 * @author liyunlong
 */
@RestController
@RequestMapping("/monitor/server")
public class ServerController
{
    @SaCheckPermission("monitor:server:list")
    @GetMapping()
    public AjaxResult getInfo() throws Exception
    {
        Server server = new Server();
        server.copyTo();
        return AjaxResult.success(server);
    }
}
