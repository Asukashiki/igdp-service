package com.inspur.workorder.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.workorder.domain.TodoItem;
import com.inspur.workorder.domain.TodoItemDetail;
import com.inspur.workorder.domain.payload.TodoItemPayload;
import com.inspur.workorder.domain.vo.ProcessStatisticsVo;
import com.inspur.workorder.domain.vo.TodoItemDetailVo;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 待办事项明细service
 * @author liyunlong02
 * @version 1.0
 * @ClassName ITodoItemDetailService
 * @date 2024/4/16 9:27
 */
public interface ITodoItemDetailService extends IService<TodoItemDetail> {

    /**
     * 创建明细
     * @param todoItem 待办事项
     * @param itemPayload 主信息载体
     *
     * */
    void addTodoItemDetail(TodoItem todoItem,TodoItemPayload itemPayload);
    /**
     * 更新明细信息
     * @param todoItem 待办事项
     * @param itemPayload 明细载体
     * */
    void updateItemDetail(TodoItem todoItem, TodoItemPayload itemPayload);

    /**
     * 获取代办列表
     * @param userId 用户id
     * @param status 状态 0代办；1已办
     * @return 列表集合
     * */
    List<TodoItemDetailVo> selectVoList(String userId,String status);

    /**
     * 统计用户处理工单明细
     * 状态是已处理的
     * @param userId 用户id
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param type 类型
     * @param status 状态
     * @param itemState 主流程状态
     * @return 统计结果
     * */
    List<ProcessStatisticsVo> countGroupByUser(String userId, LocalDateTime startTime, LocalDateTime endTime,String type,String status,String itemState);
    /**
     * 统计用户处理工单明细
     * 状态是已处理的
     * @param userId 用户id
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param type 类型
     * @param status 状态
     * @param itemState 主流程状态
     * @return 统计结果
     * */
    List<ProcessStatisticsVo> countGroupByUserType(String userId, LocalDateTime startTime, LocalDateTime endTime,String type,String status,String itemState);

    /**
     * 统计个人处理的各类型数据
     * @param userId 用户id
     * @param startTime 开始时间
     * @param endTime 截止时间
     * @param status 状态
     * @return 统计结果
     * */
    List<ProcessStatisticsVo> countGroupByType(String userId, LocalDateTime startTime, LocalDateTime endTime,String status);
    /**
     * 统计个人各个状态的数据
     * @param userId 用户id
     * @param startTime 开始时间
     * @param endTime 截止时间
     * @param type 类型
     * @return 统计结果
     * */
    List<ProcessStatisticsVo> countGroupByStatus(String userId, LocalDateTime startTime, LocalDateTime endTime,String type);
}
