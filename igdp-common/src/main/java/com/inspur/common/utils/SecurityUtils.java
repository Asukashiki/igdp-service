package com.inspur.common.utils;

/**
 * 安全工具类
 *
 * @author igdp
 */
public class SecurityUtils {

    /**
     * 获取当前登录用户名
     *
     * @return 用户名
     */
    public static String getUsername() {
        return LoginHelper.getUsername();
    }

    /**
     * 获取当前登录用户ID
     *
     * @return 用户ID
     */
    public static String getUserId() {
        return LoginHelper.getUserId();
    }

    /**
     * 获取当前登录用户部门ID
     *
     * @return 部门ID
     */
    public static String getDeptId() {
        return LoginHelper.getDeptId();
    }

    /**
     * 获取当前登录用户昵称
     *
     * @return 用户昵称
     */
    public static String getNickname() {
        return LoginHelper.getNickname();
    }

    /**
     * 判断是否为超级管理员
     *
     * @return true-是超级管理员，false-不是
     */
    public static boolean isSuperAdmin() {
        return LoginHelper.isSuperAdmin();
    }

    /**
     * 判断指定用户是否为超级管理员
     *
     * @param userId 用户ID
     * @return true-是超级管理员，false-不是
     */
    public static boolean isSuperAdmin(String userId) {
        return LoginHelper.isSuperAdmin(userId);
    }
}
