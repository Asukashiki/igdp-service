package com.inspur.system.mapper;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.yulichang.base.MPJBaseMapper;
import com.inspur.system.domain.SysOperLog;
import org.apache.ibatis.annotations.Update;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 操作日志 数据层
 *
 * @author liyunlong
 */
public interface SysOperLogMapper extends MPJBaseMapper<SysOperLog> {

    /**
     * 查询系统操作日志集合
     *
     * @param operLog 操作日志对象
     * @return 操作日志集合
     */
    default List<SysOperLog> selectOperLogList(SysOperLog operLog) {
        LambdaQueryWrapper<SysOperLog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StrUtil.isNotEmpty(operLog.getOperIp()), SysOperLog::getOperIp, operLog.getOperIp());
        queryWrapper.like(StrUtil.isNotEmpty(operLog.getTitle()), SysOperLog::getTitle, operLog.getTitle());
        queryWrapper.eq(null != operLog.getBusinessType(), SysOperLog::getBusinessType, operLog.getBusinessType());
        if (null != operLog.getBusinessTypes() && operLog.getBusinessTypes().length > 0) {
            queryWrapper.in(SysOperLog::getBusinessType, new ArrayList<>(Arrays.asList(operLog.getBusinessTypes())));
        }
        queryWrapper.eq(null != operLog.getStatus(), SysOperLog::getStatus, operLog.getStatus());
        queryWrapper.like(StrUtil.isNotEmpty(operLog.getOperName()), SysOperLog::getOperName, operLog.getOperName());
        queryWrapper.ge(null != operLog.getParams().get("beginTime"), SysOperLog::getOperTime, operLog.getParams().get("beginTime"));
        queryWrapper.le(null != operLog.getParams().get("endTime"), SysOperLog::getOperTime, operLog.getParams().get("endTime"));
        queryWrapper.orderByDesc(SysOperLog::getOperId);
        return selectList(queryWrapper);
    }

    /**
     * 清空操作日志
     */
    @Update(value = "truncate table sys_oper_log")
    void cleanOperLog();
}
