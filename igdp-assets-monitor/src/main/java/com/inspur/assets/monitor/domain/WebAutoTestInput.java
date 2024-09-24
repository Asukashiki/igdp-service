package com.inspur.assets.monitor.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.inspur.common.core.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * 每个任务下需要巡检的巡检项
 */
@TableName("web_auto_test_input")
@Getter
@Setter
public class WebAutoTestInput extends BaseEntity {
    /**
     * 对应web_auto_test_input的主键，每个检测项的序号
     */
    @TableId(value = "id",type = IdType.AUTO)
    @TableField("id")
    private Integer id;

    /**
     * 对应巡检对象按钮的名字
     */
    @TableField("inspection_element_name")
    private String inspectionElementName;
    /**
     * 对应巡检对象按钮的定位元素
     */
    @TableField("element_location")
    private String elementLocation;

    /**
     * 对应巡检对象检测成功标志的元素定位
     */
    @TableField("inspection_success_element")
    private String inspectionSuccessElement;

    /**
     * 对应任务序号 引用了web_inspection_info的主键taskid
     */
    @TableField("task_number")
    private Integer taskNumber;
    /**
     * 对应存在父级菜单，父级菜单按钮的序号 暂时被parent_button_id替换
     */
    @TableField("exist_parent_menu_number")
    private int existParentMenuNumber;

    /**
     * 对应是否拍照
     */
    @TableField("shot_screen")
    private int shotScreen;

    /**
     * 对应父按钮序号 对应web_auto_test_input的id
     */
    @TableField("parent_button_id")
    private int parentButtonId;

    @TableField(exist = false)
    private String parentButtonName;
}
