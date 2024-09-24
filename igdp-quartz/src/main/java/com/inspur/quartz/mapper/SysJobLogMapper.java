package com.inspur.quartz.mapper;

import java.time.LocalDateTime;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.common.utils.StringUtils;
import com.inspur.quartz.domain.SysJobLog;

/**
 * 调度任务日志信息 数据层
 *
 * @author liyunlong
 */
public interface SysJobLogMapper extends BaseMapper<SysJobLog> {
    /**
     * 获取quartz调度器日志的计划任务
     *
     * @param jobLog 调度日志信息
     * @return 调度任务日志集合
     */
    default List<SysJobLog> selectJobLogList(SysJobLog jobLog) {
        LambdaQueryWrapper<SysJobLog> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(jobLog.getJobName())) {
            wrapper.like(SysJobLog::getJobName, jobLog.getJobName());
        }
        if (StringUtils.isNotEmpty(jobLog.getJobGroup())) {
            wrapper.eq(SysJobLog::getJobGroup, jobLog.getJobGroup());
        }
        if (StringUtils.isNotEmpty(jobLog.getStatus())) {
            wrapper.eq(SysJobLog::getStatus, jobLog.getStatus());
        }
        if (StringUtils.isNotEmpty(jobLog.getInvokeTarget())) {
            wrapper.like(SysJobLog::getInvokeTarget, jobLog.getInvokeTarget());
        }
        LocalDateTime beginTime = jobLog.getBeginTime();
        LocalDateTime endTime = jobLog.getEndTime();
        if (null != beginTime) {
            wrapper.ge(SysJobLog::getCreateTime, beginTime);
        }
        if (null != endTime) {
            wrapper.le(SysJobLog::getCreateTime, endTime);
        }
        return selectList(wrapper);
    }
}
