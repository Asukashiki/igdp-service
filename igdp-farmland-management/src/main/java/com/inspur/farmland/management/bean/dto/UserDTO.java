package com.inspur.farmland.management.bean.dto;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户DTO
 *
 * @author inspur
 */
@Data
public class UserDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private String userName;

    private String nickName;

    private String userType;

    private String email;

    private String phonenumber;

    private String sex;

    private String avatar;

    private String password;

    private String status;

    private String loginIp;

    private Date loginDate;

    private String createBy;

    private Date createTime;

    private String updateBy;

    private Date updateTime;

    private String remark;
}
