package com.inspur.ucif.service.impl;

import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.inspur.common.constant.ApiConstants;
import com.inspur.common.constant.Constants;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.core.domain.entity.SysUser;
import com.inspur.common.core.domain.model.LoginUser;
import com.inspur.common.utils.LoginHelper;
import com.inspur.framework.web.service.SysLoginService;
import com.inspur.system.service.ISysConfigService;
import com.inspur.system.service.ISysUserService;
import com.inspur.ucif.domain.OauthPayload;
import com.inspur.ucif.service.IAuthStrategy;
import com.inspur.ucif.utils.GdzfwHAUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author liyunlong
 * @version 1.0
 * @ClassName GdzfwAuthStrategy
 * @date 2024/5/11 17:26
 */
@Service("gdzfwAuthStrategy")
@Slf4j
public class GdzfwAuthStrategy implements IAuthStrategy {

    @Resource
    private ISysConfigService configService;
    @Resource
    private ISysUserService userService;
    @Resource
    private SysLoginService sysLoginService;

    private static final String SSO_RESPONSE_CODE = "code";
    private static final Integer SSO_CODE_SUCCESS = 200;
    private static final String SSO_RESPONSE_DATA = "data";
    private static final String SSO_RESPONSE_MSG = "msg";


    public String getServer(){
        String server = configService.selectConfigByKey("sso.gdzfw.server");
        if(StrUtil.isEmpty(server)){
            throw new RuntimeException("获取广东政法委用户中心地址为空");
        }
        return server;
    }

    public String getAppId(){
        String appId = configService.selectConfigByKey("sso.gdzfw.appId");
        if(StrUtil.isEmpty(appId)){
            throw new RuntimeException("获取广东政法委用户中心AppId为空");
        }
        return appId;
    }
    public String getSecret(){
        String secret = configService.selectConfigByKey("sso.gdzfw.secret");
        if(StrUtil.isEmpty(secret)){
            throw new RuntimeException("获取广东政法委用户中心Secret为空");
        }
        return secret;
    }

    @Override
    public AjaxResult loginWithCode(OauthPayload oauthPayload) {
        String loginUrl = getServer() + ApiConstants.GDZFW_SSO_TOKEN;
        String appid = getAppId();
        String secret = getSecret();
        JSONObject params = new JSONObject();
        params.set("token", oauthPayload.getCode());
        params.set("appid", appid);
        String message = "appid=" + appid + "token=" + oauthPayload.getCode();
        log.info("code换取token的message：{}",message);
        log.info("code换取token的secret：{}",secret);
        String sign = GdzfwHAUtils.encode(secret, message);
        Map<String, String> headers = initHeaders(sign,null);
        log.info("code换取token参数：{}", params);
        log.info("code换取token请求头：{}", headers);
        String result = HttpRequest.post(loginUrl).addHeaders(headers).body(params.toString()).execute().body();
        log.info("code换取token的请求地址：{}", loginUrl);
        log.info("code换取token结果：{}", result);
        return handleTokenResult(result);
    }


    @NotNull
    private AjaxResult handleTokenResult(String result) {
        JSONObject retJo = JSONUtil.parseObj(result);
        if (!retJo.getInt(SSO_RESPONSE_CODE).equals(SSO_CODE_SUCCESS)) {
            return AjaxResult.error(401, retJo.getStr(SSO_RESPONSE_MSG));
        }
        JSONObject resultObject = JSONUtil.parseObj(retJo.getObj(SSO_RESPONSE_DATA));
        AjaxResult ajaxResult = AjaxResult.success();
        //本地存储token信息
        handleLocalLogin(resultObject);
        ajaxResult.put(Constants.TOKEN, StpUtil.getTokenValue());
        ajaxResult.put(Constants.REFRESH_TOKEN, "");
        ajaxResult.put(Constants.EXPIRES_IN, StpUtil.getTokenInfo().getTokenActiveTimeout());
        ajaxResult.put(Constants.REFRESH_EXPIRES_TIME, -1);
        return ajaxResult;
    }

    /**
     * 本地存储token信息
     */
    private void handleLocalLogin(JSONObject userObject) {
        LoginUser currentUser = handleLoginUser(userObject);
        // 本地存储token以及用户信息
        SysUser sysUser = userService.selectUserByUserName(currentUser.getUsername());
        if (null == sysUser) {
            userService.registerByLoginUser(currentUser);
            sysUser = userService.selectUserByUserName(currentUser.getUsername());
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
        //igdp-auth用户与igdp是同一库，所以不需要进行本地是否存在用户信息的判断，其他三方系统则根据需求进行本地用户新建
        //封装角色信息
        LoginUser loginUser = sysLoginService.buildLoginUser(sysUser);
        SaLoginModel saLoginModel = new SaLoginModel();
        LoginHelper.login(loginUser, saLoginModel);
        sysLoginService.recordLoginInfo(sysUser.getUserId());
    }

    @Override
    public AjaxResult loginWithPassword(OauthPayload payload) {
        return null;
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


    public Map<String, String> initHeaders(String sign, Long timestamp) {
        Map<String, String> headers = new HashMap<>(1);
        headers.put("Content-Type", "application/json");
        if (null != timestamp) {
            headers.put("timestamp", String.valueOf(timestamp));
        }
        headers.put("sign", sign);
        return headers;
    }

    private LoginUser handleLoginUser(JSONObject userObject) {
        LoginUser loginUser = new LoginUser();
        loginUser.setUserId(userObject.getStr("userId"));
        loginUser.setUsername(userObject.getStr("uname"));
        loginUser.setDeptId(userObject.getStr("deptId"));
        loginUser.setDeptName(userObject.getStr("deptName"));
        loginUser.setNickname(userObject.getStr("cname"));
        SysUser sysUser = new SysUser();
        sysUser.setUserId(userObject.getStr("userId"));
        sysUser.setUserName(userObject.getStr("uname"));
        sysUser.setDeptId(userObject.getStr("deptId"));
        sysUser.setNickName(userObject.getStr("cname"));
        sysUser.setPhoneNumber(userObject.getStr("phone"));
        sysUser.setEmail(userObject.getStr("email"));
        sysUser.setSex(userObject.getStr("sex"));
        loginUser.setUser(sysUser);
        return loginUser;
    }

    @Override
    public AjaxResult syncOrganization() {
        return null;
    }
}
