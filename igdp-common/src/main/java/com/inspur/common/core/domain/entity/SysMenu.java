package com.inspur.common.core.domain.entity;

import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.inspur.common.core.domain.BaseEntity;

/**
 * 菜单权限表 sys_menu
 * 
 * @author liyunlong
 */
@TableName("sys_menu")
@Setter
@Getter
@ToString
public class SysMenu extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 菜单ID */
    @TableId(type = IdType.ASSIGN_ID)
    private String menuId;

    /** 菜单名称 */
    @NotBlank(message = "Menu name cannot be empty")
    @Size(min = 0, max = 500, message = "Menu name length cannot exceed 500 characters")
    private String menuName;

    /**
     * 引入appId概念
     * */
    private String appId;

    /** 父菜单名称 */
    private transient String parentName;

    /** 父菜单ID */
    private String parentId;

    /** 显示顺序 */
    @NotNull(message = "Display order cannot be empty")
    private Integer orderNum;

    /** 路由地址 */
    @Size(min = 0, max = 2000, message = "Route address length cannot exceed 2000 characters")
    private String path;

    /**
     * 外链链接
     * 对于需要外链的菜单，配置为目录，path填写唯一路由，然后将外链链接配置到本参数中
     * */
    private String link;

    /** 组件路径 */
    @Size(min = 0, max = 2000, message = "Component path length cannot exceed 2000 characters")
    private String component;

    /** 路由参数 */
    private String query;

    /** 是否为外链（0是 1否） */
    private String isFrame;

    /** 是否缓存（0缓存 1不缓存） */
    private String isCache;

    /** 类型（M目录 C菜单 F按钮） */
    @NotBlank(message = "Menu type cannot be empty")
    private String menuType;

    /** 显示状态（0显示 1隐藏） */
    private String visible;
    
    /** 菜单状态（0正常 1停用） */
    private String status;

    /** 权限字符串 */
    @Size(min = 0, max = 100, message = "Permission string length cannot exceed 100 characters")
    private String perms;

    /** 菜单图标 */
    private String icon;

    /** 子菜单 */
    private transient List<SysMenu> children = new ArrayList<>();
}
