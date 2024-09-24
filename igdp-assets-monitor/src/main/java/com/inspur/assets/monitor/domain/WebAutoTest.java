package com.inspur.assets.monitor.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.inspur.common.core.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 巡检项结果
 */
@TableName("web_auto_test")
@Getter
@Setter
public class WebAutoTest extends BaseEntity {
    /**
     * web_auto_test的主键
     */
    @TableId(value = "id",type = IdType.AUTO)
    @TableField("id")
    private Integer id;
    /**
     * 巡检项名字对应web_auto_test_input的inspection_element_name
     */
    @TableField("operation")
    private String operation;
    /**
     * 对应巡检对象是否正常
     */
    @TableField("state")
    private boolean state;
    /**
     * 对应该巡检项开始时间
     */
    @TableField("start_time")
    private LocalDateTime startTime;
    /**
     * 对应巡检结束时间
     */
    @TableField("end_time")
    private LocalDateTime endTime;
    /**
     * 对应巡检时间总共花费
     */
    @TableField("response_time")
    private String responseTime;
    /**
     * 对应任务序号 引用表web_inspection_info 的taskid
     */
    @TableField("task_id")
    private Integer taskId;
    /**
     * 对应任务总结的id 引用 web_auto_task_summary的id
     */
    @TableField("summary_id")
    private int summaryId;
}
