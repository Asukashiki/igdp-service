package com.inspur.ucif.controller;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.inspur.common.constant.Constants;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.ucif.domain.OauthPayload;
import com.inspur.ucif.service.AuthStrategyFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author liyunlong
 * @date 2023/12/20
 */
@RestController
@RequestMapping("/ucif/oauth")
@Slf4j
public class OauthController {

    @Value("${sso.type:''}")
    String grantType;
    @Resource
    private AuthStrategyFactory authStrategyFactory;

    /**
     * code换取token登录接口
     */
    @PostMapping("/codeLogin")
    public AjaxResult loginWithOauth2Code(@RequestBody OauthPayload payload) {
        try {
            String useGrantType = StrUtil.isNotEmpty(payload.getGrantType()) ? payload.getGrantType() : grantType;
            return authStrategyFactory.getClassBySysName(useGrantType).loginWithCode(payload);
        } catch (Exception e) {
            throw new RuntimeException("code单点登录接口发生异常", e);
        }
    }

    /**
     * code换取token登录接口
     */
    @PostMapping("/codeLoginWithForm/{clientId}")
    public AjaxResult codeLoginWithForm(@PathVariable("clientId") String clientId, @RequestParam Map<String, String> params) {
        try {
            String useGrantType = StrUtil.isNotEmpty(params.get("grantType")) ? params.get("grantType") : grantType;
            log.info("单点登录grantType:{},clientId:{}",grantType,clientId);
            OauthPayload payload = new OauthPayload();
            payload.setCode(params.get("code"));
            payload.setRedirectUri(params.get("redirect_uri"));
            payload.setGrantType(params.get("grant_type"));
            payload.setClientId(clientId);
            return authStrategyFactory.getClassBySysName(useGrantType).loginWithCode(payload);
        } catch (Exception e) {
            throw new RuntimeException("code单点登录接口发生异常", e);
        }
    }

    /**
     * 密码模式
     */
    @PostMapping("/passwordLogin")
    public AjaxResult loginWithPassword(@RequestBody OauthPayload payload) {
        try {
            String useGrantType = StrUtil.isNotEmpty(payload.getGrantType()) ? payload.getGrantType() : grantType;
            return authStrategyFactory.getClassBySysName(useGrantType).loginWithPassword(payload);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }


    /**
     * 校验token
     */
    @RequestMapping("/checkToken")
    public AjaxResult checkToken(@RequestParam("accessToken") String accessToken, String useGrantType) throws Exception {
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
        AjaxResult ajaxResult = AjaxResult.success();
        ajaxResult.put(Constants.TOKEN, tokenInfo.getTokenValue());
        ajaxResult.put(Constants.REFRESH_TOKEN, null);
        ajaxResult.put(Constants.EXPIRES_IN, tokenInfo.getTokenTimeout());
        ajaxResult.put(Constants.REFRESH_EXPIRES_TIME, null);
        return ajaxResult;
    }


    /**
     * 刷新token
     */
    @PostMapping("/refreshToken")
    public AjaxResult refreshToken(@RequestBody OauthPayload payload) {
        if (StringUtils.isEmpty(payload.getRefreshToken())) {
            return AjaxResult.error("refreshToken不能为空");
        }
        try {
            String useGrantType = StrUtil.isNotEmpty(payload.getGrantType()) ? payload.getGrantType() : grantType;
            return authStrategyFactory.getClassBySysName(useGrantType).refreshToken(payload.getRefreshToken());
        } catch (Exception e) {
            throw new RuntimeException("刷新token接口发生异常", e);
        }
    }


    /**
     * 退出登录
     */
    @PostMapping("/logout")
    public AjaxResult logout(String useGrantType) {
        try {
            useGrantType = StrUtil.isNotEmpty(useGrantType) ? useGrantType : grantType;
            return authStrategyFactory.getClassBySysName(useGrantType).logout(StpUtil.getTokenValue());
        } catch (Exception e) {
            throw new RuntimeException("用户退出登录接口发生异常", e);
        }
    }


    /**
     * 获取所有组织树
     */
    @GetMapping("/syncOrganization")
    public AjaxResult syncOrganization() {
        try {
            return authStrategyFactory.getClassBySysName("bsp").syncOrganization();
        } catch (Exception e) {
            throw new RuntimeException("同步组织接口发生异常", e);
        }
    }
}
