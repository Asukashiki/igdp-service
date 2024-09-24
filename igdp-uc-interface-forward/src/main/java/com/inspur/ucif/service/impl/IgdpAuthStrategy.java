package com.inspur.ucif.service.impl;

import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.inspur.common.constant.ApiConstants;
import com.inspur.common.constant.Constants;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.core.domain.entity.SysUser;
import com.inspur.common.core.domain.model.LoginUser;
import com.inspur.common.utils.LoginHelper;
import com.inspur.framework.manager.AsyncManager;
import com.inspur.framework.manager.factory.AsyncFactory;
import com.inspur.framework.web.service.SysLoginService;
import com.inspur.system.service.ISysUserService;
import com.inspur.common.config.SsoConfig;
import com.inspur.ucif.domain.OauthPayload;
import com.inspur.common.core.domain.model.SsoInfo;
import com.inspur.ucif.domain.TokenDto;
import com.inspur.ucif.service.IAuthStrategy;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

/**
 * 系统内置用户认证对接
 *
 * @author liyunlong
 * @date 2023/12/20
 */
@Service("igdpAuthStrategy")
@Slf4j
@RefreshScope
public class IgdpAuthStrategy implements IAuthStrategy {

    private static final String DEFAULT_GRANT_TYPE = "igdp";

    private static final String SSO_RESPONSE_CODE = "code";
    private static final Integer SSO_CODE_SUCCESS = 200;
    private static final String SSO_RESPONSE_DATA = "data";
    private static final String SSO_RESPONSE_MSG = "msg";

    @Resource
    private ISysUserService userService;
    @Resource
    private SysLoginService sysLoginService;
    @Resource
    private SsoConfig ssoConfig;

    /**
     * 单点登录 code换取token
     *
     * @param oauthPayload 请求参数：code、redirectUri
     * @return 登录结果
     */
    @Override
    public AjaxResult loginWithCode(OauthPayload oauthPayload) {
        SsoInfo ssoInfo = ssoConfig.getSsoInfo(DEFAULT_GRANT_TYPE);
        if (null == ssoInfo) {
            return AjaxResult.error("配置信息ssoInf有误");
        }
        String loginUrl = ssoInfo.getServer() + ApiConstants.SSO_DEFAULT_TOKEN;
        Map<String, Object> params = new HashMap<>(5);
        params.put("code", oauthPayload.getCode());
        params.put("grant_type", "authorization_code");
        params.put("client_id", ssoInfo.getClientId());
        params.put("client_secret", ssoInfo.getClientSecret());
        params.put("redirect_uri", oauthPayload.getRedirectUri());
        String result = HttpUtil.post(loginUrl, params);
        log.info("code换取token结果：{}", result);
        return handleTokenResult(result, ssoInfo);
    }

    @Override
    public AjaxResult loginWithPassword(OauthPayload payload) {
        SsoInfo ssoInfo = ssoConfig.getSsoInfo(DEFAULT_GRANT_TYPE);
        if (null == ssoInfo) {
            return AjaxResult.error("配置信息ssoInf有误");
        }
        //校验验证码
        sysLoginService.validateCaptcha(payload.getUsername(), payload.getCaptchaCode(), payload.getCaptchaUuid());
        Map<String, Object> params = new HashMap<>(5);
        params.put("username", payload.getUsername());
        params.put("password", payload.getPassword());
        params.put("grant_type", "password");
        params.put("client_id", ssoInfo.getClientId());
        params.put("client_secret", ssoInfo.getClientSecret());
        String result = HttpUtil.post(ssoInfo.getServer() + ApiConstants.SSO_DEFAULT_TOKEN, params);
        log.info("密码模式登录结果：{}", result);
        return handleTokenResult(result, ssoInfo);
    }


    /**
     * 根据单点token获取认证服务中心的登录用户信息
     * 用于封装本地登录用户信息
     */
    public LoginUser getCurrentUser(String token, SsoInfo ssoInfo) {
        String currentUserUrl = ssoInfo.getAccountServer() + ApiConstants.SSO_DEFAULT_CURRENT_USER;
        Map<String, String> headers = initHeaders(token);
        String result = HttpRequest.get(currentUserUrl).addHeaders(headers).execute().body();
        log.info("调用用户中心当前登录用户响应内容：{}", result);
        JSONObject retJo = JSON.parseObject(result);
        JSONObject userJo = retJo.getJSONObject("data");
        LoginUser loginUser = new LoginUser();
        loginUser.setUserId(userJo.getString("uid"));
        loginUser.setUsername(userJo.getString("username"));
        loginUser.setToken(token);
        loginUser.setDeptId(userJo.getString("organId"));
        loginUser.setDeptName(userJo.getString("organName"));
        loginUser.setNickname(userJo.getString("nickname"));
        return loginUser;
    }

    @Override
    public AjaxResult checkToken(String token) {
        return null;
    }

    @Override
    public AjaxResult refreshToken(String refreshToken) {
        SsoInfo ssoInfo = ssoConfig.getSsoInfo(DEFAULT_GRANT_TYPE);
        if (null == ssoInfo) {
            return AjaxResult.error("配置信息ssoInf有误");
        }
        String refreshTokenUrl = ssoInfo.getServer() + ApiConstants.SSO_DEFAULT_REFRESH_TOKEN;
        Map<String, Object> params = new HashMap<>(4);
        params.put("grant_type", "refresh_token");
        params.put("refresh_token", refreshToken);
        params.put("client_id", ssoInfo.getClientId());
        params.put("client_secret", ssoInfo.getClientId());
        String result = HttpUtil.post(refreshTokenUrl, params);
        log.info("调用用户中心刷新token响应内容：{}", result);
        JSONObject retJo = JSON.parseObject(result);
        checkRequestResult(retJo);
        TokenDto tokenDto = handleFromOauth2(retJo.getString(SSO_RESPONSE_DATA));
        //获取当前登录用户信息，并保存
        if (null != tokenDto) {
            //本地存储token信息
            handleLocalLogin(tokenDto, ssoInfo);
        }
        return handleTokenResult(result, ssoInfo);
    }

    @Override
    public AjaxResult logout(String token) {
        SsoInfo ssoInfo = ssoConfig.getSsoInfo(DEFAULT_GRANT_TYPE);
        if (null == ssoInfo) {
            return AjaxResult.error("配置信息ssoInf有误");
        }
        String logoutUrl = ssoInfo.getServer() + ApiConstants.SSO_DEFAULT_LOGOUT;
        Map<String, String> headers = initHeaders(token);
        String result = HttpRequest.post(logoutUrl).addHeaders(headers).body("access_token", token).execute().body();
        log.info("调用用户中心登出接口响应内容：{}", result);
        long tokenTimeout = StpUtil.getTokenTimeout(token);
        if (tokenTimeout == -1 || tokenTimeout > 0) {
            String userId = LoginHelper.getUserId();
            String username = LoginHelper.getUsername();
            // 记录用户退出日志
            AsyncManager.me().execute(AsyncFactory.recordLoginInfo(userId, username, Constants.LOGOUT, "退出成功"));
        }
        StpUtil.logout();
        return AjaxResult.success();
    }


    @NotNull
    private AjaxResult handleTokenResult(String result, SsoInfo ssoInfo) {
        JSONObject retJo = JSON.parseObject(result);
        if (null == retJo) {
            return AjaxResult.error(401, "请求失败");
        }
        if (!retJo.getInteger(SSO_RESPONSE_CODE).equals(SSO_CODE_SUCCESS)) {
            return AjaxResult.error(401, retJo.getString(SSO_RESPONSE_MSG));
        }
        TokenDto tokenDto = handleFromOauth2(retJo.getString(SSO_RESPONSE_DATA));
        if (null != tokenDto) {
            AjaxResult ajaxResult = AjaxResult.success();
            //本地存储token信息
            handleLocalLogin(tokenDto, ssoInfo);
            ajaxResult.put(Constants.TOKEN, tokenDto.getAccessToken());
            ajaxResult.put(Constants.ACCESS_TOKEN, tokenDto.getAccessToken());
            ajaxResult.put(Constants.REFRESH_TOKEN, tokenDto.getRefreshToken());
            ajaxResult.put(Constants.EXPIRES_IN, tokenDto.getExpiresIn());
            ajaxResult.put(Constants.REFRESH_EXPIRES_TIME, tokenDto.getRefreshExpiresTime());
            return ajaxResult;
        }
        return AjaxResult.error(401, "解析单点登录token失败");
    }

    /**
     * 本地存储token信息
     */
    private void handleLocalLogin(TokenDto tokenDto, SsoInfo ssoInfo) {
        LoginUser currentUser = getCurrentUser(tokenDto.getAccessToken(), ssoInfo);
        // 本地存储token以及用户信息
        SysUser sysUser = userService.selectUserById(currentUser.getUserId());
        if (null == sysUser) {
            sysUser = userService.registerByLoginUser(currentUser);
        }
        //igdp-auth用户与igdp是同一库，所以不需要进行本地是否存在用户信息的判断，其他三方系统则根据需求进行本地用户新建
        LoginUser loginUser = sysLoginService.buildLoginUser(sysUser);
        SaLoginModel saLoginModel = new SaLoginModel();
        //设置token与oauth2响应token一致
        saLoginModel.setToken(tokenDto.getAccessToken());
        saLoginModel.setTimeout(tokenDto.getExpiresIn());
        LoginHelper.login(loginUser, saLoginModel);
        sysLoginService.recordLoginInfo(sysUser.getUserId());
    }


    private TokenDto handleFromOauth2(String tokenData) {
        if (StringUtils.isEmpty(tokenData)) {
            return null;
        }
        JSONObject tokenObject = JSONObject.parseObject(tokenData);
        String accessToken = tokenObject.getString("access_token");
        if (StringUtils.isEmpty(accessToken)) {
            accessToken = tokenObject.getString("accessToken");
        }
        if (StringUtils.isEmpty(accessToken)) {
            return null;
        }
        String refreshToken = tokenObject.getString("refresh_token");
        if (StringUtils.isEmpty(refreshToken)) {
            refreshToken = tokenObject.getString("refreshToken");
        }
        String tokenType = tokenObject.getString("token_type");
        String scope = tokenObject.getString("scope");
        Integer expiresIn = tokenObject.getInteger("expires_in");
        if (null == expiresIn) {
            expiresIn = tokenObject.getInteger("expiresTime");
        }
        Integer refreshExpiresTime = tokenObject.getInteger("refreshExpiresTime");
        return new TokenDto(accessToken, tokenType, refreshToken, expiresIn, scope, refreshExpiresTime);
    }

    private void checkRequestResult(JSONObject retJo) {
        if (null == retJo) {
            throw new RuntimeException("请求失败");
        }
        if (!retJo.getInteger(SSO_RESPONSE_CODE).equals(SSO_CODE_SUCCESS)) {
            throw new RuntimeException(retJo.getString(SSO_RESPONSE_MSG));
        }
    }


    private Map<String, String> initHeaders(String token) {
        Map<String, String> headers = new HashMap<>(1);
        headers.put("Content-Type", "application/json;charset=UTF-8");
        if (StringUtils.isNotEmpty(token)) {
            headers.put("satoken-server", token);
        }
        return headers;
    }
}
