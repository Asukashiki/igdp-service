package com.inspur.workorder.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.workorder.domain.ProcessScheduledTask;

import java.util.List;

/**
 * @author liyunlong
 * @version 1.0
 * @ClassName IProcessScheduledTaskService
 * @date 2024/6/4 16:44
 */
public interface IProcessScheduledTaskService extends IService<ProcessScheduledTask> {
    /**
     * 根据条件查询列表
     * @param queryParam 查询条件
     * @return 列表
     * */
    List<ProcessScheduledTask> selectList(ProcessScheduledTask queryParam);

    /**
     * 根据id获取信息
     * @param taskId id
     * @return 信息
     * */
    ProcessScheduledTask getTaskById(String taskId);
    /**
     * 添加新数据保存
     * @param processScheduledTask 数据内容
     * @return 结果
     * */
    boolean addTask(ProcessScheduledTask processScheduledTask);
    /**
     * 更新数据
     * @param processScheduledTask 更新数据
     * @return 结果
     * */
    boolean updateTask(ProcessScheduledTask processScheduledTask);

    /**
     * 执行任务
     * @param taskId 任务id
     * */
    void startTask(String taskId);
}
