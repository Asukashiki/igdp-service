package com.inspur.workorder.service;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.workorder.domain.TodoItem;
import com.inspur.workorder.domain.payload.TodoItemPayload;
import com.inspur.workorder.domain.vo.ProcessStatisticsVo;
import com.inspur.workorder.domain.vo.TodoItemVo;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 待办事项service
 * @author liyunlong
 * @date 2024/4/8
 */
public interface ITodoItemService extends IService<TodoItem> {

    /**
     * 查询列表
     * @param queryParam 查询条件
     * @return 列表集合
     * */
    List<TodoItem> selectList(TodoItem queryParam);

    /**
     * 获取展示信息列表
     * @param queryParam 查询条件
     * @return TodoItemVo 集合
     * */
    List<TodoItemVo> getVoList(TodoItem queryParam);

    /**
     * 工单类型查询转拼音
     * @return 集合
     */
    List<Map<String,String>> getTypeList();
    /**
     * 分发起人工单统计
     * @param queryParam 查询条件
     * @return 分发起人工单统计
     */
    List<JSONObject> getDlList(TodoItem queryParam);

    /**
     * 分发起部门工单统计
     * @param queryParam 查询条件
     * @return 分发起人工单统计
     */
    List<JSONObject> getDeptList(TodoItem queryParam);

    /**
     * 分发起时间工单统计
     * @param queryParam 查询条件
     * @return 分发起人工单统计
     */
    List<Map<String,Object>> getTimeList(TodoItem queryParam);
    /**
     * 获取类别列表
     * @return 列表集合
     * */
    List<String> selectTypeList();

    /**
     * 工单统计导出
     * @param list
     * @param response
     */
    void export(List<JSONObject> list, HttpServletResponse response);

    /**
     * 根据businessCode获取待办事项
     * @param modular 模块
     * @param type 类型
     * @param businessCode 事项code
     * @param source 数据来源，低代码icd...
     * @return 单条数据
     * */
    TodoItem selectByBusinessCode(String modular, String type, String businessCode, String source);
    /**
     * 根据businessId获取待办事项
     * @param modular 模块
     * @param type 类型
     * @param businessId 事项id
     * @param source 数据来源，低代码icd...
     * @return 单条数据
     * */
    TodoItem selectByBusinessId(String modular, String type, String businessId, String source);

    /**
     * 待办事项推送
     * @param todoItemPayload 载体
     * @return 结果
     * */
    AjaxResult pushTodoItem(TodoItemPayload todoItemPayload);

    /**
     * 获取回调现实链接
     * @param appId appid
     * @param formId formId
     * @param dataId dataId
     * @param source 来源
     * @return 链接地址
     * */
    String getLinkUri(String appId,String formId,String dataId,String source);

    /**
     * 撤销工单，将待办数据删除
     * @param todoItemPayload 事项
     * @return 结果
     * */
    AjaxResult cancelTodoItem(TodoItemPayload todoItemPayload);

    /**
     * 根据type分组统计工单数量
     * @param userId 用户id
     * @param startTime 开始时间
     * @param endTime 截至时间
     * @param modular 所属模块
     * @param state 状态
     * @return 结果
     */
    List<ProcessStatisticsVo> countGroupByType(String userId, LocalDateTime startTime,
                                              LocalDateTime endTime, String modular,String state);
    /**
     * 根据类别不同状态的工单数据
     * @param userId 用户id
     * @param startTime 开始时间
     * @param endTime 截至时间
     * @param modular 所属模块
     * @return 结果
     */
    List<ProcessStatisticsVo> countGroupByTypeState(String userId, LocalDateTime startTime, LocalDateTime endTime, String modular);

    /**
     * 统计各个状态的工单数量
     * @param userId 用户id
     * @param startTime 开始时间
     * @param endTime 截至时间
     * @param type 类型
     * @return 结果
     */
    List<ProcessStatisticsVo> countGroupByState(String userId, LocalDateTime startTime, LocalDateTime endTime, String type);
    /**
     * 统计不同部门发起的工单数量
     * @param deptId 部门id
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param type 工单类型
     * @param state 状态
     * @return 统计集合
     * */
    List<ProcessStatisticsVo> countGroupByDept(String deptId,LocalDateTime startTime, LocalDateTime endTime, String type,String state);

    /**
     * 统计各个部门不同的类型的工单数量
     * @param deptId 部门id
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param state 状态
     * @return 统计集合
     * */
    List<ProcessStatisticsVo> countGroupByDeptType(String deptId,LocalDateTime startTime, LocalDateTime endTime,String state);

    /**
     * 统计各个部门不同的类型的工单数量
     * @param userId 用户id
     * @param deptId 部门id
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param type 类型
     * @param state 状态
     * @return 统计集合
     * */
    List<ProcessStatisticsVo> countGroupByMonth(String userId,String deptId,LocalDateTime startTime, LocalDateTime endTime, String type,String state);

    /**
     * 统计各个部门不同的类型的工单数量
     * @param userId 用户id
     * @param deptId 部门id
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param state 状态
     * @return 统计集合
     * */
    List<ProcessStatisticsVo> countGroupByMonthType(String userId,String deptId,LocalDateTime startTime, LocalDateTime endTime,String state);

    /**
     * 获取工单状态
     * @return
     */
    List<TodoItemVo>getState();
}
