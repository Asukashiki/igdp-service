package com.inspur.common.core.domain.entity;

import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.inspur.common.core.domain.BaseEntity;

/**
 * 部门表 sys_dept
 * 
 * @author liyunlong
 */
@TableName("sys_dept")
@Setter
@Getter
public class SysDept extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 部门ID */
    @TableId(type = IdType.ASSIGN_ID)
    private String deptId;

    /** 父部门ID */
    private String parentId;

    /** 祖级列表 */
    private String ancestors;

    /** 部门名称 */
    // 部门名称不能为空
    @NotBlank(message = "Department name cannot be empty")
    // 部门名称长度不能超过1000个字符
    @Size(min = 0, max = 1000, message = "Department name length cannot exceed 1000 characters")
    private String deptName;

    /** 类型: D=部门, A=区划 */
    private String deptType;

    /** 显示顺序 */
    // 显示顺序不能为空
    @NotNull(message = "Display order cannot be empty")
    private Integer orderNum;

    /** 负责人 */
    private String leader;

    /** 联系电话 */
    // 联系电话长度不能超过11个字符
    @Size(min = 0, max = 11, message = "Contact phone number length cannot exceed 11 characters")
    private String phone;

    /** 邮箱 */
    // 邮箱格式不正确
    @Email(message = "Incorrect email format")
    // 邮箱长度不能超过50个字符
    @Size(min = 0, max = 50, message = "Email length cannot exceed 50 characters")
    private String email;

    /** 部门状态:0正常,1停用 */
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    /** 父部门名称 */
    private transient String parentName;
    
    /** 子部门 */
    private transient List<SysDept> children = new ArrayList<>();

    public static final String STATUS_VALID = "0";
    public static final String STATUS_INVALID = "1";
    
    public static final String TYPE_DEPT = "D";
    public static final String TYPE_AREA = "A";

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("deptId", getDeptId())
            .append("parentId", getParentId())
            .append("ancestors", getAncestors())
            .append("deptName", getDeptName())
            .append("deptType", getDeptType())
            .append("orderNum", getOrderNum())
            .append("leader", getLeader())
            .append("phone", getPhone())
            .append("email", getEmail())
            .append("status", getStatus())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
