package com.inspur.farmland.management.bean.vo;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户VO
 *
 * @author inspur
 */
@Data
public class UserVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private String userName;

    private String nickName;

    private String email;

    private String phonenumber;

    private String sex;

    private String status;

    private Date loginDate;

    private Date createTime;
}
