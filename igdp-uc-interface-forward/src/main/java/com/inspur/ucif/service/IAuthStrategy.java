package com.inspur.ucif.service;

import com.inspur.common.core.domain.AjaxResult;
import com.inspur.ucif.domain.OauthPayload;

/**
 * 授权策略
 * @author liyunlong
 * @date 2023/12/20
 */
public interface IAuthStrategy {


    /**
     * 单点登录 code换取token
     * @param oauthPayload 请求参数：code、redirectUri
     * @return 登录结果
     * */
    AjaxResult loginWithCode(OauthPayload oauthPayload);

    /**
     * 密码模式登录
     * @param payload 登录参数
     * @return 登录结果
     * */
    AjaxResult loginWithPassword(OauthPayload payload);

    /**
     * checkToken
     * 校验token是否有效
     * @param token token内容
     *
     * @return 结果：true有效；false 无效
     * */
    AjaxResult checkToken(String token);
    /**
     * 刷新token
     * @param refreshToken 刷新token
     * @return 刷新后token
     * */
    AjaxResult refreshToken(String refreshToken);
    /**
     * 退出登录
     * @param token 要退出登录的token
     * @return 结果
     * */
    AjaxResult logout(String token);

    /**
     * 获取所有菜单树
     * @return 菜单树
     * */
    AjaxResult syncOrganization();
}
