package com.inspur.common.utils;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.context.model.SaStorage;
import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.ObjectUtil;
import com.inspur.common.core.domain.model.LoginUser;
import com.inspur.common.constant.UserConstants;

/**
 * @author liyunlong
 * @date 2023/12/19
 */
public class LoginHelper {

    public static final String LOGIN_USER_KEY = "loginUser";
    public static final String USER_ID_KEY = "userId";
    public static final String USERNAME = "username";
    public static final String DEPT_ID_KEY = "deptId";

    public static void login(LoginUser loginUser, SaLoginModel model) {

        SaStorage storage = SaHolder.getStorage();
        storage.set(LOGIN_USER_KEY, loginUser);
        storage.set(USER_ID_KEY, loginUser.getUserId());
        storage.set(USERNAME, loginUser.getUsername());
        storage.set(DEPT_ID_KEY, loginUser.getDeptId());
        if(ObjectUtil.isNull(model)){
            model = new SaLoginModel();
        }
        StpUtil.login(loginUser.getUserId(),
                model.setExtra(USER_ID_KEY, loginUser.getUserId())
                        .setExtra(DEPT_ID_KEY, loginUser.getDeptId()));
        StpUtil.getSession().set(LOGIN_USER_KEY, loginUser);
    }


    /**
     * 获取用户(多级缓存)
     */
    public static LoginUser getLoginUser() {
        LoginUser loginUser = (LoginUser) SaHolder.getStorage().get(LOGIN_USER_KEY);
        if (loginUser != null) {
            return loginUser;
        }
        SaSession session = StpUtil.getSession();
        if (ObjectUtil.isNull(session)) {
            return null;
        }
        loginUser = (LoginUser) session.get(LOGIN_USER_KEY);
        SaHolder.getStorage().set(LOGIN_USER_KEY, loginUser);
        return loginUser;
    }

    public static void updateLoginUser(LoginUser loginUser){
        SaHolder.getStorage().set(LOGIN_USER_KEY,loginUser);
        StpUtil.getSession().set(LOGIN_USER_KEY, loginUser);
    }

    /**
     * 获取用户基于token
     */
    public static LoginUser getLoginUser(String token) {
        Object loginId = StpUtil.getLoginIdByToken(token);
        SaSession session = StpUtil.getSessionByLoginId(loginId);
        if (ObjectUtil.isNull(session)) {
            return null;
        }
        return (LoginUser) session.get(LOGIN_USER_KEY);
    }

    /**
     * 获取用户id
     */
    public static String getUserId() {
        LoginUser loginUser = getLoginUser();
        if (null != loginUser) {
            return loginUser.getUserId();
        } else {
            return null;
        }
    }

    /**
     * 获取部门ID
     */
    public static String getDeptId() {
        LoginUser loginUser = getLoginUser();
        if (null != loginUser) {
            return loginUser.getDeptId();
        } else {
            return null;
        }
    }

    /**
     * 获取用户账户
     */
    public static String getUsername() {
        LoginUser loginUser = getLoginUser();
        if (null != loginUser) {
            return loginUser.getUsername();
        } else {
            return null;
        }
    }

    /**
     * 获取用户昵称姓名
     */
    public static String getNickname() {
        LoginUser loginUser = getLoginUser();
        if (null != loginUser) {
            return loginUser.getNickname();
        } else {
            return null;
        }
    }

    /**
     * 是否为超级管理员
     *
     * @param userId 用户ID
     * @return 结果
     */
    public static boolean isSuperAdmin(String userId) {
        return UserConstants.SUPER_ADMIN_ID.equals(userId);
    }

    public static boolean isSuperAdmin() {
        return isSuperAdmin(getUserId());
    }

}
