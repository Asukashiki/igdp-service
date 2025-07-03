package com.inspur.ucif.domain;

import lombok.Data;

/**
 * BSP菜单扩展属性类
 * 用于存储菜单的补充字段属性
 * 
 * @author liuruchen
 * @date 2026/06/30
 */
@Data
public class BspMenuExtend {
    
    /**
     * 菜单图标
     */
    private String icon;
    
    /**
     * 权限标识
     */
    private String perms;
    
    /**
     * 是否为外链（0是 1否）
     */
    private String isFrame;
    
    /**
     * 外链链接
     */
    private String link;
    
    /**
     * 显示状态（0显示 1隐藏）
     */
    private String visible;

    /**
     * 是否缓存（0缓存 1不缓存）
     */
    private String isCache;

    /**
     * 组件路径
     */
    private String component;
} 