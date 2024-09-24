package com.inspur.assets.monitor.domain;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.inspur.common.core.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;



/**
 * @author wangxinyang
 * @version 1.0
 * @ClassName WebInspectionInfo
 * @date 2024/7/30 10:43
 * 设置任务，每个任务对应一个页面和账号密码，对任务进行定时任务
 */

@TableName("web_inspection_info")
@Getter
@Setter
public class WebInspectionInfo extends BaseEntity {
    /**
     * taskid 主键 任务编号 对应 web_inspection_info表每一项序号
     *
     */
    @TableField("task_id")
    @TableId(value = "task_id", type = IdType.AUTO)
    private Integer taskId;

    /**
     * systemName 对应系统名字
     */
    @TableField("system_name")
    private String systemName;

    /**
     * systemAddress 对应需要巡检的url
     */
    @TableField("system_address")
    private String systemAddress;

    /**
     * 对应巡检选择的驱动
     */
    @TableField("driver_select")
    private String driverSelect;

    /**
     * 对应巡检页面登录需要的账号
     */
    @TableField("username")
    private String username;
    /**
     * 对应巡检页面登录需要的密码
     */
    @TableField("password")
    private String password;
    /**
     * 对应页面账号输入框的元素定位
     */
    @TableField("username_input")
    private String usernameInput;

    /**
     * 对应页面密码输入框的元素定位
     */
    @TableField("password_input")
    private String passwordInput;

    /**
     * 对应页面验证码输入的元素定位(非必要)
     */
    @TableField("captcha_input")
    private String captchaInput;

    /**
     * 对应验证码图片元素的定位
     */
    @TableField("captcha_image_location")
    private String captchaImageLocation;

    /**
     * 对应任务需要巡检的定时任务频率 cron 会根据web_inspection_plan的test_frequency进行更新
     */
    @TableField("test_frequency")
    private String testFrequency;

    /**
     * 对应系统ID，暂时不需要
     */
    @TableField("system_id")
    private int systemID;

    /**
     * 对应是否暂时禁用 与web_inspection_plan的status有关
     */
    @TableField("status")
    private String status;
    @TableField(exist = false)
    private WebAutoTestInput[] elementList;
}
