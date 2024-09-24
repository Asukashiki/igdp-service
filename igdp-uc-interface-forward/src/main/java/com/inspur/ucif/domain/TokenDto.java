package com.inspur.ucif.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * token信息
 * @author liyunlong
 * @date 2023/8/8
 */
@Setter
@Getter
public class TokenDto {
    /**
     * token
     * */
    private String accessToken;
    /**
     * token类型
     * */
    private String tokenType;
    /**
     * 刷新token
     * */
    private String refreshToken;
    /**
     * 刷新token的过期时间
     * */
    private Integer refreshExpiresTime;
    /**
     * 过期时间
     * */
    private Integer expiresIn;
    /**
     * token授权范围
     * */
    private String scope;



    public TokenDto(String accessToken, String tokenType, String refreshToken, int expiresIn, String scope, Integer refreshExpiresTime){
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.refreshToken = refreshToken;
        this.expiresIn = expiresIn;
        this.refreshExpiresTime = refreshExpiresTime;
        this.scope = scope;
    }
}
