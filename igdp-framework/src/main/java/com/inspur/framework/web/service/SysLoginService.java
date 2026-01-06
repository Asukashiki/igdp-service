package com.inspur.framework.web.service;

import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.digest.MD5;
import com.inspur.common.core.domain.entity.SysRole;
import com.inspur.common.utils.LoginHelper;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.inspur.common.constant.CacheConstants;
import com.inspur.common.constant.Constants;
import com.inspur.common.constant.UserConstants;
import com.inspur.common.core.domain.entity.SysUser;
import com.inspur.common.core.domain.model.LoginUser;
import com.inspur.common.core.redis.RedisCache;
import com.inspur.common.exception.user.BlackListException;
import com.inspur.common.exception.user.CaptchaException;
import com.inspur.common.exception.user.CaptchaExpireException;
import com.inspur.common.exception.user.UserNotExistsException;
import com.inspur.common.exception.user.UserPasswordNotMatchException;
import com.inspur.common.utils.DateUtils;
import com.inspur.common.utils.MessageUtils;
import com.inspur.common.utils.StringUtils;
import com.inspur.common.utils.ip.IpUtils;
import com.inspur.framework.manager.AsyncManager;
import com.inspur.framework.manager.factory.AsyncFactory;
import com.inspur.system.service.ISysConfigService;
import com.inspur.system.service.ISysUserService;

import java.util.stream.Collectors;


/**
 * 登录校验方法
 *
 * @author liyunlong
 */
@Component
public class SysLoginService {

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysConfigService configService;
    @Autowired
    private SysPermissionService permissionService;

    /**
     * 登录验证
     *
     * @param username 用户名
     * @param password 密码
     * @param code     验证码
     * @param uuid     唯一标识
     * @return 结果
     */
    public String login(String username, String password, String code, String uuid) {
        // 验证码校验
        validateCaptcha(username, code, uuid);
        // 登录前置校验
        loginPreCheck(username, password);
        SysUser sysUser = userService.selectUserByUserName(username);
        // 用户不存在
        if (sysUser == null) {
            AsyncManager.me().execute(AsyncFactory.recordLoginInfo(null, username, Constants.LOGIN_FAIL, MessageUtils.message("user.not.exists")));
            throw new UserNotExistsException();
        }
        // 用户已停用
        if (UserConstants.USER_DISABLE.equals(sysUser.getStatus())) {
            AsyncManager.me().execute(AsyncFactory.recordLoginInfo(sysUser.getUserId(), username, Constants.LOGIN_FAIL, MessageUtils.message("user.blocked")));
            throw new UserNotExistsException();
        }
        String userId = sysUser.getUserId();
        AsyncManager.me().execute(AsyncFactory.recordLoginInfo(userId, username, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success")));
        recordLoginInfo(userId);
        // 生成token
        LoginHelper.login(buildLoginUser(sysUser), new SaLoginModel());
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
        return tokenInfo.getTokenValue();
    }

    /**
     * 校验验证码
     *
     * @param username 用户名
     * @param code     验证码
     * @param uuid     唯一标识
     */
    public void validateCaptcha(String username, String code, String uuid) {
        boolean captchaEnabled = configService.selectCaptchaEnabled();
        if (captchaEnabled) {
            String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + StringUtils.nvl(uuid, "");
            String captcha = redisCache.getCacheObject(verifyKey);
            redisCache.deleteObject(verifyKey);
            if (captcha == null) {
                AsyncManager.me().execute(AsyncFactory.recordLoginInfo(null,username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.expire")));
                throw new CaptchaExpireException();
            }
            if (!code.equalsIgnoreCase(captcha)) {
                AsyncManager.me().execute(AsyncFactory.recordLoginInfo(null,username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.error")));
                throw new CaptchaException();
            }
        }
    }

    /**
     * 登录前置校验
     *
     * @param username 用户名
     * @param password 用户密码
     */
    public void loginPreCheck(String username, String password) {
        // 用户名或密码为空 错误
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            AsyncManager.me().execute(AsyncFactory.recordLoginInfo(null,username, Constants.LOGIN_FAIL, MessageUtils.message("not.null")));
            throw new UserNotExistsException();
        }
        // 密码如果不在指定范围内 错误
        if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
                || password.length() > UserConstants.PASSWORD_MAX_LENGTH) {
            AsyncManager.me().execute(AsyncFactory.recordLoginInfo(null,username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
            throw new UserPasswordNotMatchException();
        }
        // 用户名不在指定范围内 错误
        if (username.length() < UserConstants.USERNAME_MIN_LENGTH
                || username.length() > UserConstants.USERNAME_MAX_LENGTH) {
            AsyncManager.me().execute(AsyncFactory.recordLoginInfo(null,username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
            throw new UserPasswordNotMatchException();
        }
        // IP黑名单校验
        String blackStr = configService.selectConfigByKey("sys.login.blackIPList");
        if (IpUtils.isMatchedIp(blackStr, IpUtils.getIpAddr())) {
            AsyncManager.me().execute(AsyncFactory.recordLoginInfo(null,username, Constants.LOGIN_FAIL, MessageUtils.message("login.blocked")));
            throw new BlackListException();
        }
    }

    /**
     * 记录登录信息
     *
     * @param userId 用户ID
     */
    public void recordLoginInfo(String userId) {
        if(StrUtil.isNotEmpty(userId)){
            SysUser sysUser = new SysUser();
            sysUser.setUserId(userId);
            sysUser.setLoginIp(IpUtils.getIpAddr());
            sysUser.setLoginDate(DateUtils.getNowDate());
            userService.updateUserProfile(sysUser);
        }
    }

    public LoginUser buildLoginUser(SysUser sysUser) {

        LoginUser loginUser = new LoginUser();
        loginUser.setUser(sysUser);
        loginUser.setUserId(sysUser.getUserId());
        loginUser.setDeptId(sysUser.getDeptId());
        loginUser.setUsername(sysUser.getUserName());
        loginUser.setNickname(sysUser.getNickName());
        loginUser.setPermissions(permissionService.getMenuPermission(sysUser));
        loginUser.setDeptName(ObjectUtils.isEmpty(sysUser.getDept()) ? "" : sysUser.getDept().getDeptName());
        if (null != sysUser.getRoles()) {
            loginUser.setRoles(sysUser.getRoles().stream().map(SysRole::getRoleId).collect(Collectors.toSet()));
        }
        return loginUser;
    }

    /**
     * 第三方用户同步
     */
    public void syncThirdUser(SysUser sysUser) {
        if (sysUser != null) {
            // 插入用户
            SysUser user = userService.selectUserById(sysUser.getUserId());
            if (ObjectUtils.isEmpty(user)) {
                sysUser.setPassword(SmUtil.sm3(MD5.create().digestHex(Constants.INIT_PASSWORD)));
                userService.insertUser(sysUser);
            }else {
                // 更新用户
                userService.updateUser(sysUser);
            }
        }
    }
}
