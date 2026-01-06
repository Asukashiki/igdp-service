package com.inspur.farmland.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.sql.Blob;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("bsp.pub_user")
public class PubUser implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;

    private String account;

    private String name;

    private String password;

    private String userCode;

    private Integer grade;

    private String gender;

    private LocalDate birthday;

    private String identityNum;

    private String phone;

    private String mobile;

    private String email;

    private String position;

    /**
     * 用户类型
     */
    private String typeCode;

    private LocalDateTime lastLoginTime;

    private String roleCode;

    /**
     * 区划编码
     */
    private String regionCode;

    /**
     * 区划名称
     */
    private String regionName;

    /**
     * 大（主）机构编码
     */
    private String orgCode;

    /**
     * 大（主）机构名称
     */
    private String orgName;

    private String orgShortCode;

    private String roleValue;

    /**
     * 配置管理员级别[0非管理员,1超级或平台级管理员,2部委管理员,3省级管理员,4市级管理员,5县区级管理员,6乡镇街道级管理员,7社区级管理员]
     */
    private Integer isAdmin;

    private String status;

    private String address;

    /**
     * 标识用户密码是否修改过，未改过为0，改过为1
     */
    private Integer pwdChanged;

    private LocalDateTime updateTime;

    private String userType;

    private String secretLevel;

    private LocalDateTime pwdLastupdate;

    private Integer userclass;

    private String wxAccount;

    /**
     * 是否已实名校验 1：是；0：否
     */
    private String isReal;

    /**
     * 电子签章
     */
    private Blob elecImg;
}
