package com.inspur.ucif.service.impl;

import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.core.domain.entity.SysApp;
import com.inspur.system.service.ISysAppService;
import com.inspur.ucif.service.IAppStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author liyunlong
 * @date 2024/4/2
 */
@Service("igdpAppStrategy")
@Slf4j
public class IgdpAppStrategy implements IAppStrategy {
    @Resource
    private ISysAppService sysAppService;
    @Override
    public AjaxResult getAppByAppId(String appId) {
        SysApp sysApp = sysAppService.getById(appId);
        if(null != sysApp){
            return AjaxResult.success(sysApp);
        }else{
            return AjaxResult.error("未查询到相关APP信息");
        }
    }
}
