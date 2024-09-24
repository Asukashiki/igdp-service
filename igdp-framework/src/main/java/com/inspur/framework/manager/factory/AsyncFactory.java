package com.inspur.framework.manager.factory;

import java.time.LocalDateTime;
import java.util.TimerTask;

import com.inspur.common.utils.http.HttpHelper;
import com.inspur.system.domain.SysOpenApiLog;
import com.inspur.system.service.ISysOpenApiLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.inspur.common.constant.Constants;
import com.inspur.common.utils.LogUtils;
import com.inspur.common.utils.ServletUtils;
import com.inspur.common.utils.StringUtils;
import com.inspur.common.utils.ip.AddressUtils;
import com.inspur.common.utils.ip.IpUtils;
import com.inspur.common.utils.spring.SpringUtils;
import com.inspur.system.domain.SysLoginInfo;
import com.inspur.system.domain.SysOperLog;
import com.inspur.system.service.ISysLoginInfoService;
import com.inspur.system.service.ISysOperLogService;
import eu.bitwalker.useragentutils.UserAgent;

import javax.servlet.http.HttpServletRequest;

/**
 * 异步工厂（产生任务用）
 *
 * @author liyunlong
 */
public class AsyncFactory {
    private static final Logger sys_user_logger = LoggerFactory.getLogger("sys-user");

    /**
     * 记录登录信息
     *
     * @param username 用户名
     * @param status   状态
     * @param message  消息
     * @param args     列表
     * @return 任务task
     */
    public static TimerTask recordLoginInfo(final String userId, final String username, final String status, final String message,
                                            final Object... args) {
        final UserAgent userAgent = UserAgent.parseUserAgentString(ServletUtils.getRequest().getHeader("User-Agent"));
        final String ip = IpUtils.getIpAddr();
        return new TimerTask() {
            @Override
            public void run() {
                String address = AddressUtils.getRealAddressByIP(ip);
                StringBuilder s = new StringBuilder();
                s.append(LogUtils.getBlock(ip));
                s.append(address);
                s.append(LogUtils.getBlock(username));
                s.append(LogUtils.getBlock(status));
                s.append(LogUtils.getBlock(message));
                // 打印信息到日志
                sys_user_logger.info(s.toString(), args);
                // 获取客户端操作系统
                String os = userAgent.getOperatingSystem().getName();
                // 获取客户端浏览器
                String browser = userAgent.getBrowser().getName();
                // 封装对象
                SysLoginInfo loginInfo = new SysLoginInfo();
                loginInfo.setUserId(userId);
                loginInfo.setUserName(username);
                loginInfo.setIpaddr(ip);
                loginInfo.setLoginLocation(address);
                loginInfo.setBrowser(browser);
                loginInfo.setOs(os);
                loginInfo.setMsg(message);
                // 日志状态
                if (StringUtils.equalsAny(status, Constants.LOGIN_SUCCESS, Constants.LOGOUT, Constants.REGISTER)) {
                    loginInfo.setStatus(Constants.SUCCESS);
                } else if (Constants.LOGIN_FAIL.equals(status)) {
                    loginInfo.setStatus(Constants.FAIL);
                }
                // 插入数据
                SpringUtils.getBean(ISysLoginInfoService.class).insertLoginInfo(loginInfo);
            }
        };
    }

    /**
     * 操作日志记录
     *
     * @param operLog 操作日志信息
     * @return 任务task
     */
    public static TimerTask recordOper(final SysOperLog operLog) {
        return new TimerTask() {
            @Override
            public void run() {
                // 远程查询操作地点
                operLog.setOperLocation(AddressUtils.getRealAddressByIP(operLog.getOperIp()));
                SpringUtils.getBean(ISysOperLogService.class).insertOperlog(operLog);
            }
        };
    }

    /**
     * 三方接口请求日志记录
     */
    public static TimerTask recordOpenApiLog(final HttpServletRequest request, final String result) {
        return new TimerTask() {
            @Override
            public void run() {
                String appId = request.getHeader("appId");
                String nonce = request.getHeader("nonce");
                String sign = request.getHeader("sign");
                String body = HttpHelper.getBodyString(request);
                String ip = IpUtils.getIpAddr(request);
                SysOpenApiLog apiLog = new SysOpenApiLog();
                apiLog.setAppId(appId);
                apiLog.setApiPath(request.getRequestURI());
                apiLog.setBody(body);
                apiLog.setIp(ip);
                apiLog.setResult(result);
                apiLog.setCreateTime(LocalDateTime.now());
                SpringUtils.getBean(ISysOpenApiLogService.class).save(apiLog);
            }
        };
    }
}
