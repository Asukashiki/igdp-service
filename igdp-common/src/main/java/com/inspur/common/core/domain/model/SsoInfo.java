package com.inspur.common.core.domain.model;

import lombok.Data;

/**
 * 单点服务配置
 * @author liyunlong
 * @version 1.0
 * @ClassName SsoConfig
 * @date 2024/9/4 17:49
 */
@Data
public class SsoInfo {
    /**
     * 应用 ID
     */
    private String appId;
    private String name;

    /**
     * 客户端id
     * */
    private String clientId;

    private String clientSecret;
    /**
     * 回调地址，多个的话，以英文逗号分割
     * */
    private String redirectUri;

    /**
     * 单点服务
     * */
    private String server;

    /**
     * 账户相关接口的服务地址
     * 例如获取当前用户信息、获取角色、获取组织部门信息登
     * */
    private String accountServer;
    /**
     * 获取token接口
     * */
    private String tokenApi;

    private String refreshTokenApi;

    /**
     * 获取用户信息接口
     * */
    private String currentUserApi;

    private String logoutApi;

    private String checkTokenApi;
    /**
     * 获取菜单信息接口
     */
    private String getMenuByUserApi;
}
