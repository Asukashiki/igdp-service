package com.inspur.common.core.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.inspur.common.core.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * 应用信息
 * @author liyunlong
 * @date 2024/4/2
 */
@Setter
@Getter
public class SysApp extends BaseEntity {

    /**
     * 引入appId概念
     * */
    @TableId(type = IdType.ASSIGN_ID)
    private String appId;

    private String name;

    private String appIcon;

    private Long sortNumber;

    /**
     * 对应的单点client_id
     * */
    private String clientId;

    private String clientSecret;

    /**
     * 所用的单点服务
     * */
    private String ssoServer;

    /**
     * 应用的访问路径前缀
     * 类似：http://ip:port/icd/
     * */
    private String appServer;

    /**
     * 服务的回调地址
     * */
    private String callBack;


}
