package com.inspur.assets.monitor.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.inspur.common.core.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * 巡检计划的总结
 */
@TableName("web_auto_task_summary")
@Getter
@Setter
public class WebAutoTaskSummary extends BaseEntity {
    /**
     * web_auto_task_summary的主键
     */
    @TableField("id")
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    /**
     * 对应web_inspection_info的taskid 对应任务的id
     */
    @TableField("task_id")
    private Integer taskId;
    /**
     * 对应计划开始时间
     */
    @TableField("task_start_time")
    private String taskStartTime;
    /**
     * 对应计划结束时间
     */
    @TableField("task_finish_time")
    private String taskFinishTime;
    /**
     * 对应巡检的总数
     */
    @TableField("task_test_total_number")
    private int taskTestTotalNumber;
    /**
     * 对应巡检成功的数量
     */
    @TableField("task_test_success_count")
    private int taskTestSuccessCount;
    /**
     * 对应巡检失败的数量
     */
    @TableField("task_test_failed_count")
    private int taskTestFailedCount;
}
