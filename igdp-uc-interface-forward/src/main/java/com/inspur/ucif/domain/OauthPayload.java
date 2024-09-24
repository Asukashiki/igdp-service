package com.inspur.ucif.domain;

import cn.dev33.satoken.stp.StpUtil;
import lombok.Getter;
import lombok.Setter;

/**
 * @author liyunlong
 * @date 2023/12/13
 */
@Setter
@Getter
public class OauthPayload {
    /**
     * 用户名密码
     * */
    private String username;

    private String password;

    private String grantType;

    private String clientId;

    /**
     * 单点登录的code
     * 用于换取token
     * */
    private String code;

    private String redirectUri;

    private String refreshToken;

    /**
     * 验证码以及uuid
     * */
    private String captchaCode;
    private String captchaUuid;

}
