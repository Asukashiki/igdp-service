package com.inspur.common.core.domain.model;

/**
 * 用户注册对象
 * 
 * @author liyunlong
 */
public class RegisterBody extends LoginBody
{
    /**
     * 真实姓名
     */
    private String nickName;

    /**
     * 性别（0男 1女 2未知）
     */
    private String sex;

    /**
     * 身份证号
     */
    private String idCard;

    /**
     * 部门ID（行政区划）
     */
    private String deptId;

    /**
     * 手机号码
     */
    private String phoneNumber;

    /**
     * 邮箱
     */
    private String email;

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
