package com.inspur.ucif.service.impl;

import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.inspur.common.config.SsoConfig;
import com.inspur.common.constant.ApiConstants;
import com.inspur.common.constant.Constants;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.core.domain.entity.SysMenu;
import com.inspur.common.core.domain.entity.SysRole;
import com.inspur.common.core.domain.entity.SysUser;
import com.inspur.common.core.domain.model.LoginUser;
import com.inspur.common.core.domain.model.SsoInfo;
import com.inspur.common.utils.LoginHelper;
import com.inspur.framework.manager.AsyncManager;
import com.inspur.framework.manager.factory.AsyncFactory;
import com.inspur.framework.web.service.SysLoginService;
import com.inspur.system.service.ISysMenuService;
import com.inspur.system.service.ISysUserService;
import com.inspur.ucif.constant.GrantTypeConstants;
import com.inspur.ucif.domain.OauthPayload;
import com.inspur.ucif.domain.TokenDto;
import com.inspur.ucif.service.IAuthStrategy;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 系统内置用户认证对接
 *
 * @author liyunlong
 * @date 2023/12/20
 */
@Service("bspAuthStrategy")
@Slf4j
@RefreshScope
public class BspAuthStrategy implements IAuthStrategy {



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
    @Resource
    private BspAccountStrategy bspAccountStrategy;
    @Resource
    private ISysMenuService sysMenuService;

    /**
     * 单点登录 code换取token
     *
     * @param oauthPayload 请求参数：code、redirectUri
     * @return 登录结果
     */
    @Override
    public AjaxResult loginWithCode(OauthPayload oauthPayload) {
        SsoInfo ssoInfo = ssoConfig.getSsoInfo(GrantTypeConstants.BSP_GRANT_TYPE);
        if (null == ssoInfo) {
            return AjaxResult.error("配置信息ssoInf有误");
        }
        String loginUrl = ssoInfo.getServer() + ssoInfo.getTokenApi();
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
        SsoInfo ssoInfo = ssoConfig.getSsoInfo(GrantTypeConstants.BSP_GRANT_TYPE);
        if (null == ssoInfo) {
            return AjaxResult.error("配置信息ssoInf有误");
        }
        //校验验证码
//        sysLoginService.validateCaptcha(payload.getUsername(), payload.getCaptchaCode(), payload.getCaptchaUuid());
        Map<String, Object> params = new HashMap<>(5);
        params.put("username", payload.getUsername());
        params.put("password", payload.getPassword());
        params.put("grant_type", "password");
        params.put("client_id", ssoInfo.getClientId());
        params.put("client_secret", ssoInfo.getClientSecret());
        String result = HttpUtil.post(ssoInfo.getServer() + ssoInfo.getTokenApi(), params);
        log.info("密码模式登录结果：{}", result);
        return handleTokenResult(result, ssoInfo);
    }


    /**
     * 根据单点token获取认证服务中心的登录用户信息
     * 用于封装本地登录用户信息
     */
    public LoginUser getCurrentUser(String token, SsoInfo ssoInfo) {
        String currentUserUrl = ssoInfo.getAccountServer() + ssoInfo.getCurrentUserApi();
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", token);
        // 添加 appId 参数
        Map<String, Object> params = new HashMap<>(1);
        params.put("appId", ssoInfo.getAppId());
        
        String result = HttpRequest.get(currentUserUrl)
                .addHeaders(headers)
                .form(params)
                .execute()
                .body();
        log.info("调用用户中心当前登录用户响应内容：{}", result);
        
        JSONObject retJo = JSON.parseObject(result);
        if (Objects.isNull(retJo)) {
            log.error("调用用户中心返回数据为空");
            throw new RuntimeException("获取用户信息失败");
        }
        
        JSONObject data = retJo.getJSONObject("data");
        if (Objects.isNull(data)) {
            log.error("调用用户中心返回的data为空");
            throw new RuntimeException("获取用户信息失败");
        }
        
        JSONObject userJo = data.getJSONObject("user");
        if (Objects.isNull(userJo)) {
            log.error("调用用户中心返回的user数据为空");
            throw new RuntimeException("获取用户信息失败");
        }
        
        // 转换为SysUser对象
        SysUser sysUser = convertToSysUser(userJo);
        LoginUser loginUser = new LoginUser();
        loginUser.setUser(sysUser);
        loginUser.setUserId(userJo.getString("id"));
        loginUser.setUsername(userJo.getString("username"));
        loginUser.setToken(token);
        loginUser.setDeptId(userJo.getString("organCode"));
        loginUser.setDeptName(userJo.getString("organName"));
        loginUser.setNickname(userJo.getString("name"));
        // 设置角色列表
        String roleStr = userJo.getString("role");
        if (StringUtils.isNotBlank(roleStr)) {
            loginUser.setRoles(new HashSet<>(StrUtil.split(roleStr, StrUtil.C_COMMA)));
        }
        return loginUser;
    }

    /**
     * 将用户中心返回的用户信息转换为SysUser对象
     * 
     * @param userJo 用户中心返回的用户JSON对象
     * @return SysUser对象
     */
    private SysUser convertToSysUser(JSONObject userJo) {
        SysUser sysUser = new SysUser();
        
        // 设置用户ID
        sysUser.setUserId(userJo.getString("id"));
        
        // 设置用户名
        sysUser.setUserName(userJo.getString("username"));
        
        // 设置昵称
        sysUser.setNickName(userJo.getString("name"));
        
        // 设置部门ID
        sysUser.setDeptId(userJo.getString("organCode"));
        // 设置部门名称
        sysUser.setDeptName(userJo.getString("organName"));
        
        // 设置手机号
        String mobile = userJo.getString("mobile");
        if (StringUtils.isNotBlank(mobile)) {
            sysUser.setPhoneNumber(mobile);
        }
        
        // 设置邮箱
        String email = userJo.getString("email");
        if (StringUtils.isNotBlank(email)) {
            sysUser.setEmail(email);
        }
        
        // 设置状态（默认正常）
        sysUser.setStatus("0");
        
        // 设置删除标志（默认存在）
        sysUser.setDelFlag("0");
     
        return sysUser;
    }

    @Override
    public AjaxResult checkToken(String token) {
        return null;
    }

    @Override
    public AjaxResult refreshToken(String refreshToken) {
        SsoInfo ssoInfo = ssoConfig.getSsoInfo(GrantTypeConstants.BSP_GRANT_TYPE);
        if (null == ssoInfo) {
            return AjaxResult.error("配置信息ssoInf有误");
        }
        String refreshTokenUrl = ssoInfo.getServer() + ssoInfo.getRefreshTokenApi();
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
        SsoInfo ssoInfo = ssoConfig.getSsoInfo(GrantTypeConstants.BSP_GRANT_TYPE);
        if (null == ssoInfo) {
            return AjaxResult.error("配置信息ssoInf有误");
        }
        String logoutUrl = ssoInfo.getServer() + ssoInfo.getLogoutApi();
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

        // 从 BSP获取当前用户信息
        List<SysMenu> menuTree = bspAccountStrategy.getMenuTree(tokenDto.getAccessToken());
        // 从菜单中读取用户权限
        Set<String> permissions = sysMenuService.selectMenuPermsByMenuTree(menuTree);
        
        // 设置用户权限
        if (Objects.nonNull(permissions) && !permissions.isEmpty()) {
            currentUser.setPermissions(permissions);
        }
        
        SaLoginModel saLoginModel = new SaLoginModel();
        //设置token与oauth2响应token一致
        saLoginModel.setToken(tokenDto.getAccessToken());
        saLoginModel.setTimeout(tokenDto.getExpiresIn());
        LoginHelper.login(currentUser, saLoginModel);
        sysLoginService.recordLoginInfo(currentUser.getUserId());
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
