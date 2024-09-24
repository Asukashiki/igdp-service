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
 * 用户对象 sys_user
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
     * 用户ID
     */
    @Excel(name = "用户序号", prompt = "用户编号")
    @TableId(type = IdType.ASSIGN_ID)
    private String userId;

    /**
     * 部门ID
     */
    @Excel(name = "部门编号", type = Type.IMPORT)
    private String deptId;

    /**
     * 用户账号
     */
    @Excel(name = "登录名称")
    @Xss(message = "用户账号不能包含脚本字符")
    @NotBlank(message = "用户账号不能为空")
    @Size(min = 0, max = 30, message = "用户账号长度不能超过30个字符")
    private String userName;

    /**
     * 用户昵称
     */
    @Excel(name = "用户名称")
    @Xss(message = "用户昵称不能包含脚本字符")
    @Size(min = 0, max = 30, message = "用户昵称长度不能超过30个字符")
    private String nickName;

    /**
     * 用户邮箱
     */
    @Excel(name = "用户邮箱")
    @Email(message = "邮箱格式不正确")
    @Size(min = 0, max = 50, message = "邮箱长度不能超过50个字符")
    private String email;

    /**
     * 手机号码
     */
    @Excel(name = "手机号码")
    @Size(min = 0, max = 20, message = "手机号码长度不能超过20个字符")
    private String phoneNumber;

    /**
     * 用户性别
     */
    @Excel(name = "用户性别", readConverterExp = "0=男,1=女,2=未知")
    private String sex;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 密码
     */
    @TableField(select = false)
    private String password;

    /**
     * 帐号状态（0正常 1停用）
     */
    @Excel(name = "帐号状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;

    /**
     * 排序号
     * */
    private Integer sortNumber;

    /**
     * 最后登录IP
     */
    @Excel(name = "最后登录IP", type = Type.EXPORT)
    private String loginIp;

    /**
     * 最后登录时间
     */
    @Excel(name = "最后登录时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss", type = Type.EXPORT)
    private Date loginDate;

    /**
     * 是否可以展示
     * 默认是0，可以展示
     * 1，不展示
     * */
    private String allowedShow;

    /**
     * 部门对象
     */
    @Excels({
            @Excel(name = "部门名称", targetAttr = "deptName", type = Type.EXPORT),
            @Excel(name = "部门负责人", targetAttr = "leader", type = Type.EXPORT)
    })
    private transient SysDept dept;

    /**
     * 角色对象
     */
    private transient List<SysRole> roles;

    /**
     * 角色组
     */
    private  transient String[] roleIds;

    /**
     * 岗位组
     */
    private transient String[] postIds;

    /**
     * 角色ID
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
