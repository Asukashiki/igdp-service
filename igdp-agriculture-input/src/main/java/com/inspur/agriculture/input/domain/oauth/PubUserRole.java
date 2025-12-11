package com.inspur.agriculture.input.domain.oauth;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户角色关联对象 pub_user_role (oauth2_bsp模式)
 *
 * @author igdp
 */
@Data
@TableName("bsp.pub_user_role")
public class PubUserRole implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 用户ID */
    private String userCode;

    /** 角色ID */
    private String roleCode;

    private String appCode;
}
