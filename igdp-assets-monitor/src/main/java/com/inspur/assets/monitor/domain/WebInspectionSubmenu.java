package com.inspur.assets.monitor.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.inspur.common.core.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 父菜单按钮表(暂时没用，被递归方法暂时取代)
 */
@TableName("web_inspection_submenu")
@Getter
@Setter
public class WebInspectionSubmenu extends BaseEntity {
    /**
     * 对应子按钮的序号 对应web_auto_test_input的主键
     */
    @TableField("child_menu_id")
    private int childMenuId;
    /**
     * 对应点击顺序
     */
    @TableField("click_order")
    private int clickOrder;
    /**
     * 对应父菜单按钮名称
     */
    @TableField("button_name")
    private String buttonName;
    /**
     * 对应父菜单按钮定位
     */
    @TableField("button_location_element")
    private String buttonLocationElement;
    /**
     * 对应父菜单按钮定位成功后标志
     */
    @TableField("display_success_element")
    private String displaySuccessElement;

}
