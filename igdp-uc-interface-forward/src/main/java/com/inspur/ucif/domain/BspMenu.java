package com.inspur.ucif.domain;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

import java.util.List;

/**
 * BSP菜单实体类
 * 
 * @author liuruchen
 * @date 2026/06/30
 */
@Data
public class BspMenu {

    /**
     * 菜单ID
     */
    private String menuId;
    
    /**
     * 菜单路径
     */
    private String path;
    
    /**
     * 组件数据
     */
    private String data;
    
    /**
     * 是否隐藏
     */
    private Integer hidden;
    
    /**
     * 打开方式
     */
    private Integer openWay;
    
    /**
     * 菜单图标
     */
    private String icon;

    
    /**
     * 备注
     */
    private String remark;
    
    /**
     * 菜单标题
     */
    private String title;
    
    /**
     * 菜单类型
     */
    private String type;
    
    /**
     * 子菜单列表
     */
    private List<BspMenu> children;
    
    /**
     * 菜单扩展属性
     */
    private BspMenuExtend extend;
}
