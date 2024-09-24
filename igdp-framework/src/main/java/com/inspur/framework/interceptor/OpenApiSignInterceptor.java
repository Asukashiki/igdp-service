package com.inspur.framework.interceptor;

import com.alibaba.fastjson2.JSON;
import com.inspur.common.constant.Constants;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.utils.http.HttpHelper;
import com.inspur.common.utils.ip.IpUtils;
import com.inspur.common.utils.sign.SignUtil;
import com.inspur.framework.manager.AsyncManager;
import com.inspur.framework.manager.factory.AsyncFactory;
import com.inspur.system.domain.SysOpenApp;
import com.inspur.system.service.ISysOpenAppService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 对外接口前面拦截处理
 *
 * @author liyunlong
 * @version 1.0
 * @ClassName ApiSignInterceptor
 * @date 2024/6/14 14:58
 */
public class OpenApiSignInterceptor implements HandlerInterceptor {

    /**
     * 调用者身份唯一标识
     */
    private static final String APP_ID = "appid";
    /**
     * 时间戳
     */
    private static final String TIMESTAMP = "timestamp";
    /**
     * 签名
     */
    private static final String SIGN = "sign";
    /**
     * 随机值
     */
    private static final String NONCE = "nonce";


    @Resource
    private ISysOpenAppService openAppService;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (checkSign(request, response)) {
            return HandlerInterceptor.super.preHandle(request, response, handler);
        }
        return false;
    }

    /**
     * 验证签名
     */
    private boolean checkSign(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType("application/json");
        response.setCharacterEncoding("utf8");
        String ip = IpUtils.getIpAddr(request);
        String appId = request.getHeader(APP_ID);
        String timestamp = request.getHeader(TIMESTAMP);
        String nonce = request.getHeader(NONCE);
        String sign = request.getHeader(SIGN);
        String body = HttpHelper.getBodyString(request);
        if (!StringUtils.isNotBlank(appId)) {
            response.getWriter().write(JSON.toJSONString(AjaxResult.error("appid无效")));
            // 保存日志
            AsyncManager.me().execute(AsyncFactory.recordOpenApiLog(request, "错误信息:appid无效"));
            return false;
        }
        if (StringUtils.isBlank(sign)) {
            response.getWriter().write(JSON.toJSONString(AjaxResult.error("签名无效")));
            AsyncManager.me().execute(AsyncFactory.recordOpenApiLog(request, "错误信息:签名无效"));
            return false;
        }
        SysOpenApp openApp = openAppService.getById(appId);
        if (openApp == null) {
            response.getWriter().write(JSON.toJSONString(AjaxResult.error("appid不存在")));
            AsyncManager.me().execute(AsyncFactory.recordOpenApiLog(request, "错误信息:appId不存在"));
            return false;
        }
        if (StringUtils.isNotBlank(openApp.getBlackList())) {
            for (String bIp : openApp.getBlackList().split(",")) {
                //黑名单
                if (bIp.equals(ip)) {
                    response.getWriter().write(JSON.toJSONString(AjaxResult.error("拒绝请求")));
                    AsyncManager.me().execute(AsyncFactory.recordOpenApiLog(request, "错误信息:黑名单拒绝请求"));
                    return false;
                }
            }
        }
        if (StringUtils.isNotBlank(openApp.getWhiteList())) {
            boolean flag = false;
            for (String bIp : openApp.getWhiteList().split(",")) {
                //白名单
                if (bIp.equals(ip)) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                response.getWriter().write(JSON.toJSONString(AjaxResult.error("拒绝请求")));
                AsyncManager.me().execute(AsyncFactory.recordOpenApiLog(request, "错误信息:白名单未符合拒绝请求"));
                return false;
            }
        }

        if (SysOpenApp.INVOKE.equals(openApp.getInvokeStatus())) {
            response.getWriter().write(JSON.toJSONString(AjaxResult.error("访问权限已被冻结")));
            AsyncManager.me().execute(AsyncFactory.recordOpenApiLog(request, "错误信息:访问权限已被冻结"));
            return false;
        }
        if (!Constants.STATUS_VALID.equals(openApp.getStatus())) {
            response.getWriter().write(JSON.toJSONString(AjaxResult.error("接口异常,暂停访问")));
            AsyncManager.me().execute(AsyncFactory.recordOpenApiLog(request, "错误信息:接口异常,暂停访问"));
            return false;
        }

        if (!StringUtils.isNotBlank(timestamp)) {
            response.getWriter().write(JSON.toJSONString(AjaxResult.error("时间戳无效")));
            AsyncManager.me().execute(AsyncFactory.recordOpenApiLog(request, "错误信息:时间戳无效"));
            return false;
        } else if (openApp.getTimeOut() != null) {
            if (System.currentTimeMillis() - Long.parseLong(timestamp) > openApp.getTimeOut() * 1000) {
                response.getWriter().write(JSON.toJSONString(AjaxResult.error("请求已过期")));
                AsyncManager.me().execute(AsyncFactory.recordOpenApiLog(request, "错误信息:请求已过期"));
                return false;
            }
        }
        Map<String, Object> hashMap = handleParamMap(request, body);
        hashMap.put(APP_ID, appId);
        hashMap.put(TIMESTAMP, timestamp);
        if (StringUtils.isNotBlank(nonce)) {
            hashMap.put(NONCE, nonce);
        }
        String secretKey = openApp.getAppSecret();
        if (!SignUtil.signValidate(hashMap, secretKey, sign)) {
            response.getWriter().write(JSON.toJSONString(AjaxResult.error("认证失败")));
            response.getWriter().write(JSON.toJSONString(AjaxResult.error("错误信息:认证失败")));
            return false;
        }
        return true;
    }

    private Map<String, Object> handleParamMap(HttpServletRequest request, String body) {
        Map<String, Object> hashMap = new HashMap<>();
        //获取url后边拼接的参数
        String queryStrings = request.getQueryString();
        if (queryStrings != null) {
            for (String queryString : queryStrings.split("&")) {
                String[] param = queryString.split("=");
                if (param.length == 2) {
                    hashMap.put(param[0], param[1]);
                }
            }
        }
        if (StringUtils.isNotBlank(body)) {
            Map<String, Object> map = JSON.parseObject(body);
            if (map != null) {
                hashMap.putAll(map);
            }
        }
        return hashMap;
    }
}
