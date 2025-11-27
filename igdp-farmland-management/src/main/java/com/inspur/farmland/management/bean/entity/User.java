package com.inspur.farmland.management.bean.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户实体类
 *
 * @author inspur
 */
@Data
@TableName("user")
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 用户ID */
    @TableId(value = "USER_ID", type = IdType.ASSIGN_ID)
    private String userId;

    /** 账号（登录名） */
    @TableField("ACCOUNT")
    private String account;

    /** 密码 */
    @TableField("PASSWORD")
    private String password;

    /** 姓名 */
    @TableField("USER_NAME")
    private String userName;

    /** 身份证号 */
    @TableField("ID_CARD")
    private String idCard;

    /** 性别 */
    @TableField("GENDER")
    private String gender;

    /** 手机号 */
    @TableField("PHONE")
    private String phone;

    /** 邮箱 */
    @TableField("EMAIL")
    private String email;

    /** 行政区划代码 */
    @TableField("AD_CODE")
    private String adCode;

    /** 注册时间 */
    @TableField("REG_DATE")
    private Date regDate;

    /** 账号状态 (1:正常, 0:禁用) */
    @TableField("STATUS")
    private Integer status;
}
