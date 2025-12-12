package com.inspur.common.core.domain.entity;

import java.util.Date;
import java.util.List;
import javax.validation.constraints.*;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.inspur.common.core.domain.BaseEntity;
import com.inspur.common.xss.Xss;
import com.inspur.common.constant.UserConstants;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.inspur.common.annotation.Excel;
import com.inspur.common.annotation.Excel.Type;
import com.inspur.common.annotation.Excels;
import org.apache.ibatis.annotations.Select;

/**
 * User object sys_user
 *
 * @author liyunlong
 */
@TableName("sys_user")
@Setter
@Getter
@ToString
public class SysUser extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * User ID
     */
    @Excel(name = "User ID", prompt = "User number")
    @TableId(type = IdType.ASSIGN_ID)
    private String userId;

    /**
     * Department ID
     */
    @Excel(name = "Department No.", type = Type.IMPORT)
    private String deptId;

    /**
     * User account
     */
    @Excel(name = "Login Name")
    @Xss(message = "User account cannot contain script characters")
    @NotBlank(message = "User account cannot be empty")
    @Size(min = 0, max = 30, message = "User account length cannot exceed 30 characters")
    private String userName;

    /**
     * User nickname
     */
    @Excel(name = "User Name")
    @Xss(message = "User nickname cannot contain script characters")
    @Size(min = 0, max = 30, message = "User nickname length cannot exceed 30 characters")
    private String nickName;

    /**
     * User email
     */
    @Excel(name = "User Email")
    @Email(message = "Email format is incorrect")
    @Size(min = 0, max = 50, message = "Email length cannot exceed 50 characters")
    private String email;

    /**
     * Phone number
     */
    @Excel(name = "Phone Number")
    @Size(min = 0, max = 20, message = "Phone number length cannot exceed 20 characters")
    private String phoneNumber;

    /**
     * ID Card Number
     */
    @Excel(name = "ID Card Number")
    @Size(min = 0, max = 20, message = "ID Card Number length cannot exceed 20 characters")
    private String idCard;

    /**
     * User gender
     */
    @Excel(name = "User Gender", readConverterExp = "0=Male,1=Female,2=Unknown")
    private String sex;

    /**
     * User avatar
     */
    private String avatar;

    /**
     * Password
     */
    @TableField(select = false)
    private String password;

    /**
     * Account status (0 normal 1 disabled)
     */
    @Excel(name = "Account Status", readConverterExp = "0=Normal,1=Disabled")
    private String status;

    /**
     * Delete flag (0 represents existence, 2 represents deletion)
     */
    private String delFlag;

    /**
     * Sort number
     */
    private Integer sortNumber;

    /**
     * Last login IP
     */
    @Excel(name = "Last Login IP", type = Type.EXPORT)
    private String loginIp;

    /**
     * Last login time
     */
    @Excel(name = "Last Login Time", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss", type = Type.EXPORT)
    private Date loginDate;

    /**
     * Whether it can be displayed
     * Default is 0, can be displayed
     * 1, not displayed
     */
    private String allowedShow;

    /**
     * Department object
     */
    @Excels({
            @Excel(name = "Department Name", targetAttr = "deptName", type = Type.EXPORT),
            @Excel(name = "Department Head", targetAttr = "leader", type = Type.EXPORT)
    })
    private transient SysDept dept;

    /**
     * Role object
     */
    private transient List<SysRole> roles;

    /**
     * Role group
     */
    private  transient String[] roleIds;

    /**
     * Post group
     */
    private transient String[] postIds;

    /**
     * Role ID
     */
    private transient String roleId;

    private transient String roleKey;

    private transient String deptName;

    public SysUser() {

    }

    public SysUser(String userId) {
        this.userId = userId;
    }

    public boolean isAdmin() {
        return isAdmin(this.userId);
    }

    public static boolean isAdmin(String userId) {
        return StrUtil.isNotEmpty(userId) && UserConstants.SUPER_ADMIN_ID.equals(userId);
    }

}
