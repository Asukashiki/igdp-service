package com.inspur.workorder.service;

import cn.hutool.json.JSONObject;
import com.inspur.workorder.domain.vo.ProcessStatisticsVo;
import com.inspur.workorder.domain.vo.TodoItemPersonalStatisticsVo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 待办事项统计接口
 * @author liyunlong
 * @version 1.0
 * @ClassName ITodoItemStatisticsService
 * @date 2024/5/17 10:02
 */
public interface ITodoItemStatisticsService {


    /**
     * 根据年月统计工单数据
     * @param userId 用户id
     * @param startTime 开始日期
     * @param endTime 截止日期
     * @return 统计结果对象
     * */
    TodoItemPersonalStatisticsVo getPersonalStatistics(String userId, LocalDateTime startTime, LocalDateTime endTime);
    /**
     * 待办统计
     * @param userId 用户id
     * @param startTime 开始日期
     * @param endTime 截止日期
     * @param status 状态 0待办；1已办
     * @return 统计结果
     * */
    Long countDetail(String userId, LocalDateTime startTime, LocalDateTime endTime, String status);
    /**
     * 事项统计
     * 我的发起，总事项数量等
     * @param userId 用户id
     * @param startTime 开始日期
     * @param endTime 截止日期
     * @param state 状态：不填为所有；办结
     * @return 统计结果
     * */
    Long countItem(String userId, LocalDateTime startTime, LocalDateTime endTime,String state);
    /**
     * 统计不同状态的工单数据
     * @param userId 发起人userId
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param type 类型
     * @return 统计列表
     * */
    List<ProcessStatisticsVo> countByState(String userId, LocalDateTime startTime, LocalDateTime endTime, String type);
    /**
     * 统计不同状态的工单数据
     * 工单总量、处理中、已办结、未解决
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 统计结果
     * */
    List<ProcessStatisticsVo> countAllWithState(LocalDateTime startTime, LocalDateTime endTime);
    /**
     * 按照类型type统计各类型工单数量
     * @param userId 用户id
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param modular 所属模块：icd workOrder
     * @return 统计列表
     * */
    List<ProcessStatisticsVo> statisticsWithType(String userId, LocalDateTime startTime, LocalDateTime endTime, String modular);
    /**
     * 统计工单解决率相关数据
     * @param userId 用户id
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param modular 所属模块
     * @return 结果列表
     * */
    JSONObject statisticsResolution(String userId, LocalDateTime startTime, LocalDateTime endTime,String modular);
    /**
     * 统计人员处理工单的数量
     * @param deptId 部门id
     * @param userId 用户id
     * @param modular 所属模块
     * @param type 类型
     * @param startTime 开始时间
     * @param endTime 截至时间
     * @param itemState 主流程状态
     * @return 统计结果列表
     * */
    List<ProcessStatisticsVo> statisticsDetailWithUser(String deptId,String userId, LocalDateTime startTime, LocalDateTime endTime,String modular,String type,String itemState);
    /**
     * 按部门统计发起的工单数据
     * @param deptId 部门id
     * @param startTime 开始时间
     * @param endTime 截止时间
     * @param type 类型
     * @param status 状态
     * @return 统计结果列表
     * */
    List<ProcessStatisticsVo> statisticsWithDept(String deptId, LocalDateTime startTime, LocalDateTime endTime,String type,String status);
    /**
     * 统计各个部门不同类型的工单数量
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param status 状态
     * @return 统计结果
     * */
    JSONObject statisticsDeptType(LocalDateTime startTime, LocalDateTime endTime,String status);
    /**
     * 按月份统计工单变化趋势
     * 全部、各个类型工单数据
     * 响应内容：月份 [1月，2月，3月]
     *         类型 [全部、电脑维修、网络故障]
     *         数据 [20、15、5]
     * @param year 年
     * @param months 区间段：-1 按照年查询；大于0 则查询几个月，最大不能超过12
     * @param state 状态
     * @return 统计结果
     * */
    JSONObject statisticsTrendByMonthType(Integer year,Integer months,String state);

    /**
     * 统计年度工单，各个月份的数量
     * @param year 年
     * @param type 类型
     * @param state 状态
     * @return 集合
     * */
    List<ProcessStatisticsVo> statisticsWithYear(Integer year,String type,String state);

    /**
     * 统计部门事项表格数据
     * @param deptId 部门id
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param state 状态
     * @return 统计结果
     * */
    List<ProcessStatisticsVo> statisticsWithDeptType(String deptId,LocalDateTime startTime, LocalDateTime endTime,String state);

    /**
     * 统计部门人员发起工单数量
     * @param deptId 部门id
     * @param userId 用户id
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param state 状态
     * @param type 类型
     * @return 统计结果
     * */
    List<ProcessStatisticsVo> statisticsWithDeptUserType(String deptId,String userId,String type,LocalDateTime startTime, LocalDateTime endTime,String state);

    /**
     * 统计部门以及人员工单处理信息
     * @param startTime 开始时间
     * @param endTime 截止时间
     * @param state 状态
     * @param itemState 工单主状态
     * @return 统计列表
     * */
    List<ProcessStatisticsVo> statisticsDetailWithUserType(LocalDateTime startTime, LocalDateTime endTime,String state,String itemState);




}
