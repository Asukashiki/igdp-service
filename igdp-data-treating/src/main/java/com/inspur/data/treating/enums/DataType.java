package com.inspur.data.treating.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.inspur.common.enums.BaseEnum;
import lombok.Getter;

/**
 * 数据类型枚举量
 * @author liyunlong
 * @version 1.0
 * @ClassName DataType
 * @date 2024/7/17 14:26
 */
@Getter
public enum DataType implements BaseEnum {
    /**
     * 用户总量
     * */
    USER_TOTAL("user_total","用户总量"),
    /**
     * 访问量
     * */
    VISIT_NUM("visit_num","访问量"),
    /**
     * 失败访问量
     * */
    VISIT_NUM_ERROR("visit_num_error","访问失败量"),
    /**
     * 活跃用户数
     * */
    ACTIVE_USER_NUM("active_user_num","活跃用户数"),
    /**
     * 访问用户量
     * */
    VISIT_USER_NUM("visit_user_num","访问用户量"),
    /**
     * 登录次数
     * */
    LOGIN_NUM("login_num","登录次数"),
    /**
     * 登录用户数
     * */
    LOGIN_USER_NUM("login_user_num","登录用户数"),
    /**
     * 业务办理量
     * */
    BUSINESS_MANAGEMENT_NUM("business_management_num","业务办理量"),
    /**
     * 新注册用户数
     */
    REGISTER_USER_NUM("register_user_num","新注册用户数"),
    /**
     * 新增用户量
     */
    NUMBER_ONLINE_USERS("number_online_users","当前在线人数"),
    /**
     * 平均响应时间
     */
    RESPONSE_TIME("response_time","平均响应时间"),

    /**
     * 通用总量
     * */
    TOTAL("total","总量"),

    ;


    @EnumValue
    private final String code;
    private final String description;

     DataType(String code,String description){
        this.code = code;
        this.description = description;
    }
}
