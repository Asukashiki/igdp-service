package com.inspur.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.inspur.common.core.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * 开放接口调用的三方应用
 * @author liyunlong
 * @version 1.0
 * @ClassName SysOpenApp
 * @date 2024/6/14 15:04
 */
@TableName("sys_open_app")
@Setter
@Getter
public class SysOpenApp extends BaseEntity {
    @TableId(type = IdType.ASSIGN_ID)
    private String appid;

    /**
     * 应用名称
     * */
    private String appName;

    /**
     * 应用密钥
     * */
    private String appSecret;

    private String callbackLink;

    /**
     * ip黑名单
     * */
    private String blackList;

    private String whiteList;

    /**
     * 1 锁定
     * 0 未锁
     * */
    private String invokeStatus;

    private String status;

    private Long timeOut;

    /**
     * 删除标志
     * 0有效；2已删除
     * */
    @TableLogic
    private String delFlag;


    public static final String INVOKE = "1";
    public static final String UN_INVOKE = "0";

}
