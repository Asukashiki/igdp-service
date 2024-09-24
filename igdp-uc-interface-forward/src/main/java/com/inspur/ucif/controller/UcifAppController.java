package com.inspur.ucif.controller;

import com.inspur.common.core.domain.AjaxResult;
import com.inspur.ucif.service.IAppStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


/**
 * 菜单应用相关接口
 * @author liyunlong
 * @date 2024/4/2
 */
@RestController
@RequestMapping("/ucif/app")
public class UcifAppController {

    @Value("${sso.type:''}")
    String grantType;

    @Resource
    private IAppStrategy appStrategy;

    /**
     * 根据appId获取菜单信息
     * 主要使用ssoServer以及appServer等相关信息
     * */
    @GetMapping("getAppByAppId/{appId}")
    public AjaxResult getAppByAppId(@PathVariable("appId")String appId){
        return appStrategy.getInstance(grantType).getAppByAppId(appId);
    }
}
