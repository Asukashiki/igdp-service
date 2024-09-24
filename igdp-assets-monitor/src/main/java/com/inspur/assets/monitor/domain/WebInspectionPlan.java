package com.inspur.assets.monitor.domain;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.inspur.common.core.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * 设置巡检计划
 */
@TableName("web_inspection_plan")
@Getter
@Setter
public class WebInspectionPlan extends BaseEntity {
    /**
     * web_inspection_plan的主键
     */
    @TableId(value = "jobid",type = IdType.INPUT)
    @TableField("jobid")
    private int jobid;
    /**
     *对应任务计划的id，引用了web_inspection_info的taskid
     */
    @TableField("taskid")
    private Integer taskid;
    /**
     * 设置的定时任务cron表达式，会更新web_inspection_info的test_frquency
     */
    @TableField("test_frequency")
    private String testFrequency;
    /**
     * 是否禁用
     */
    @TableField("status")
    private String status;
    /**
     * 是否发生并发
     */
    @TableField("concurrent")
    private int concurrent;

    @TableField("misfire_policy")
    private int misfirePolicy;
    /**
     * 对应计划名 设置的巡检计划名
     */
    @TableField("system_name")
    private String systemName;
    /**
     * 引用的任务名，对应web_inspection_info的systemName
     */
    @TableField("group_name")
    private String groupName;
}
