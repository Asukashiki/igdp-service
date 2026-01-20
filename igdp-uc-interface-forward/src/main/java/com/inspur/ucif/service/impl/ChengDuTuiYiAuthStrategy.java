package com.inspur.ucif.service.impl;

import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.inspur.common.constant.Constants;
import com.inspur.common.constant.HttpStatus;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.core.domain.entity.SysUser;
import com.inspur.common.core.domain.model.LoginUser;
import com.inspur.common.utils.LoginHelper;
import com.inspur.framework.web.service.SysLoginService;
import com.inspur.system.service.ISysRoleService;
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
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 成都退役用户中心，单点登录对接实现
 *
 * @author liyunlong
 * @version 1.0
 * @date 2024/7/29 17:25
 */
@Service("chengDuTuiYiAuthStrategy")
@Slf4j
@RefreshScope
public class ChengDuTuiYiAuthStrategy implements IAuthStrategy {


    private static final String DEFAULT_GRANT_TYPE = "chengDuTuiYi";


    @Resource
    private ISysUserService userService;
    @Resource
    private ISysRoleService roleService;
    @Resource
    private SysLoginService sysLoginService;
    @Resource
    private SsoConfig ssoConfig;

    private static final String SSO_RESPONSE_CODE = "status";
    private static final Integer SSO_CODE_SUCCESS = 0;
    private static final String SSO_RESPONSE_DATA = "data";
    private static final String SSO_RESPONSE_MSG = "msg";


    /**
     * 单点登录 code换取token
     *
     * @param oauthPayload 请求参数：code、redirectUri
     * @return 登录结果
     */
    @Override
    public AjaxResult loginWithCode(OauthPayload oauthPayload) {
        SsoInfo ssoInfo;
        if (StrUtil.isNotEmpty(oauthPayload.getClientId())) {
            ssoInfo = ssoConfig.getSsoInfoByClientId(oauthPayload.getClientId());
        } else {
            ssoInfo = ssoConfig.getSsoInfo(DEFAULT_GRANT_TYPE);
        }
        log.info("配置的ssoInfo：{}", ssoInfo);
        if (null == ssoInfo) {
            return AjaxResult.error("配置信息ssoInf有误");
        }
        String redirectUri = StringUtils.isNotEmpty(oauthPayload.getRedirectUri()) ? oauthPayload.getRedirectUri() : ssoInfo.getRedirectUri();
        String paramStr = "?grant_type=authorization_code" + "&code=" + oauthPayload.getCode() +
                "&redirect_uri=" + redirectUri;
        String loginUrl = ssoInfo.getServer() + ssoInfo.getTokenApi() + paramStr;
        loginUrl = loginUrl.replace("#", "%23");
        log.info("code换取token地址：{}", loginUrl);
        String authorization = "Basic " + org.apache.commons.codec.binary.Base64.encodeBase64String((ssoInfo.getClientId() + ":" + ssoInfo.getClientSecret()).getBytes(StandardCharsets.UTF_8));
        log.info("code换取token的clientId:{},clientSecret:{},authorization:{}", ssoInfo.getClientId(), ssoInfo.getClientSecret(), authorization);
        Map<String, String> headers = initHeaders(authorization);
        String result = HttpRequest.post(loginUrl).addHeaders(headers).execute().body();
        log.info("code换取token结果：{}", result);
        return handleTokenResult(result);
    }


    @NotNull
    private AjaxResult handleTokenResult(String result) {
        TokenDto tokenDto = handleFromOauth2(result);
        if (null == tokenDto) {
            return AjaxResult.error(HttpStatus.UNAUTHORIZED, "授权登录失败");
        }
        //本地存储token信息
        handleLocalLogin(tokenDto);
        AjaxResult ajaxResult = AjaxResult.success();
        ajaxResult.put(Constants.TOKEN, StpUtil.getTokenValue());
        ajaxResult.put(Constants.ACCESS_TOKEN, StpUtil.getTokenValue());
        ajaxResult.put(Constants.REFRESH_TOKEN, "");
        ajaxResult.put(Constants.EXPIRES_IN, StpUtil.getTokenInfo().getTokenActiveTimeout());
        ajaxResult.put(Constants.REFRESH_EXPIRES_TIME, -1);
        return ajaxResult;
    }

    private TokenDto handleFromOauth2(String tokenData) {
        if (StringUtils.isEmpty(tokenData)) {
            return null;
        }
        JSONObject tokenObject = JSONUtil.parseObj(tokenData);
        String accessToken = tokenObject.getStr("access_token");
        if (StringUtils.isEmpty(accessToken)) {
            accessToken = tokenObject.getStr("accessToken");
        }
        if (StringUtils.isEmpty(accessToken)) {
            return null;
        }
        String tokenType = tokenObject.getStr("token_type");
        String scope = tokenObject.getStr("scope");
        Integer expiresIn = tokenObject.getInt("expires_in");
        if (null == expiresIn) {
            expiresIn = tokenObject.getInt("expiresTime");
        }
        String jti = tokenObject.getStr("jti");
        return new TokenDto(accessToken, tokenType, null, expiresIn, scope, null);
    }

    /**
     * 本地存储token信息
     */
    private void handleLocalLogin(TokenDto tokenDto) {

        LoginUser currentUser = getCurrentUser(tokenDto.getAccessToken());
        // 本地存储token以及用户信息
        SysUser sysUser = userService.selectUserById(currentUser.getUserId());
        if (null == sysUser) {
            userService.registerByLoginUser(currentUser);
            sysUser = userService.selectUserById(currentUser.getUserId());
        } else {
            //更新用户的信息
            boolean shouldUpdate = false;
            if (!sysUser.getDeptId().equals(currentUser.getDeptId())) {
                shouldUpdate = true;
                sysUser.setDeptId(currentUser.getDeptId());
            }
            if (!sysUser.getUserName().equals(currentUser.getUsername())) {
                shouldUpdate = true;
                sysUser.setUserName(currentUser.getUsername());
            }
            if (!sysUser.getNickName().equals(currentUser.getNickname())) {
                shouldUpdate = true;
                sysUser.setNickName(currentUser.getNickname());
            }
            if (shouldUpdate) {
                sysUser.setUpdateTime(LocalDateTime.now());
                userService.updateById(sysUser);
            }
        }
        //封装角色信息
        LoginUser loginUser = sysLoginService.buildLoginUser(sysUser);
        SaLoginModel saLoginModel = new SaLoginModel();
        //设置token与oauth2响应token一致
        saLoginModel.setToken(tokenDto.getAccessToken());
        saLoginModel.setTimeout(tokenDto.getExpiresIn());
        LoginHelper.login(loginUser, saLoginModel);
        sysLoginService.recordLoginInfo(sysUser.getUserId());
    }

    @Override
    public AjaxResult loginWithPassword(OauthPayload payload) {
        return null;
    }

    public LoginUser getCurrentUser(String token) {
        SsoInfo ssoInfo = ssoConfig.getSsoInfo(DEFAULT_GRANT_TYPE);
        String currentUserUrl = ssoInfo.getAccountServer() + ssoInfo.getCurrentUserApi();
        //根据token获取用户信息
        if (!token.startsWith(Constants.TOKEN_PREFIX)) {
            token = Constants.TOKEN_PREFIX + token;
        }
        Map<String, String> headers = initHeaders(token);
        log.info("调用用中心当前登录用户请求头：{}", headers);
        String result = HttpRequest.get(currentUserUrl).addHeaders(headers).execute().body();
        log.info("调用用户中心当前登录用户响应内容：{}", result);
        JSONObject retJo = JSONUtil.parseObj(result);
        if (!retJo.getInt(SSO_RESPONSE_CODE).equals(SSO_CODE_SUCCESS)) {
            throw new RuntimeException("获取用户信息失败");
        }
        JSONObject userJo = retJo.getJSONObject("data");
        LoginUser loginUser = new LoginUser();
        loginUser.setUserId(userJo.getStr("ucUid"));
        loginUser.setUsername(userJo.getStr("loginName"));
        loginUser.setNickname(userJo.getStr("nickName"));
        loginUser.setToken(token);
        JSONObject organObject = userJo.getJSONObject("oran");
        loginUser.setDeptId(organObject.getStr("organId"));
        loginUser.setDeptName(organObject.getStr("organName"));
        return loginUser;
    }

    @Override
    public AjaxResult checkToken(String token) {
        return null;
    }

    @Override
    public AjaxResult refreshToken(String refreshToken) {
        return null;
    }

    @Override
    public AjaxResult logout(String token) {
        return null;
    }

    private Map<String, String> initHeaders(String token) {
        Map<String, String> headers = new HashMap<>(1);
//        headers.put("Content-Type", "application/json;charset=UTF-8");
        if (StringUtils.isNotEmpty(token)) {
            headers.put("Authorization", token);
        }
        return headers;
    }

    @Override
    public AjaxResult syncOrganization() {
        return null;
    }
}
