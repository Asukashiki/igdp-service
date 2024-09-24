package com.inspur.workorder.mapper;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.common.core.domain.entity.UnifyStatisticsItemValue;
import com.inspur.workorder.domain.TodoItem;
import com.inspur.workorder.domain.vo.ProcessStatisticsVo;
import com.inspur.workorder.domain.vo.TodoItemVo;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @author liyunlong
 * @date 2024/4/8
 */
@Mapper
public interface TodoItemMapper extends BaseMapper<TodoItem> {

    /**
     * 根据type分组统计工单数量
     *
     * @param userId    用户id
     * @param startTime 开始时间
     * @param endTime   截至时间
     * @param modular   所属模块
     * @param state     状态
     * @return 结果
     */
    List<ProcessStatisticsVo> countGroupByType(@Param("userId") String userId, @Param("startTime") LocalDateTime startTime,
                                               @Param("endTime") LocalDateTime endTime, @Param("modular") String modular, @Param("state") String state);

    /**
     * 根据状态，type分组统计工单数量
     *
     * @param userId    用户id
     * @param startTime 开始时间
     * @param endTime   截至时间
     * @param modular   所属模块
     * @return 结果
     */
    @MapKey("type")
    List<ProcessStatisticsVo> countGroupByStateType(@Param("userId") String userId, @Param("startTime") LocalDateTime startTime,
                                                    @Param("endTime") LocalDateTime endTime, @Param("modular") String modular);

    /**
     * 根据状态，type分组统计工单数量
     *
     * @param userId    用户id
     * @param startTime 开始时间
     * @param endTime   截至时间
     * @param type      所属类型
     * @return 结果
     */
    @MapKey("type")
    List<ProcessStatisticsVo> countGroupByState(@Param("userId") String userId, @Param("startTime") LocalDateTime startTime,
                                                @Param("endTime") LocalDateTime endTime, @Param("type") String type);

    /**
     * 按部门统计工单数量
     *
     * @param deptId    部门id
     * @param startTime 开始时间
     * @param endTime   截止时间
     * @param type      类型
     * @param state     状态
     * @return 统计结果
     */
    List<ProcessStatisticsVo> countGroupByDept(@Param("deptId") String deptId, @Param("startTime") LocalDateTime startTime,
                                               @Param("endTime") LocalDateTime endTime, @Param("type") String type, @Param("state") String state);

    /**
     * 按部门类型统计工单数量
     *
     * @param deptId    部门id
     * @param startTime 开始时间
     * @param endTime   截止时间
     * @param state     状态
     * @return 统计结果
     */
    List<ProcessStatisticsVo> countGroupByDeptType(@Param("deptId") String deptId, @Param("startTime") LocalDateTime startTime,
                                                   @Param("endTime") LocalDateTime endTime, @Param("state") String state);

    /**
     * 按照部门、人员、类型统计数量
     * @param deptId 部门id
     * @param userId 用户id
     * @param type 类型
     * @param state 状态
     * @param endTime 结束时间
     * @param startTime 开始时间
     * @return 统计列表
     */
    List<ProcessStatisticsVo> countGroupByDeptUserType(@Param("deptId") String deptId, @Param("userId") String userId, @Param("type") String type,
                                                       @Param("state") String state, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    /**
     * 根据年月分组统计数据
     *
     * @param userId    用户id
     * @param deptId    部门id
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param type      类型
     * @param state     状态
     * @return 统计结果
     */
    List<ProcessStatisticsVo> countGroupByYearMonth(@Param("userId") String userId, @Param("deptId") String deptId,
                                                    @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime,
                                                    @Param("type") String type, @Param("state") String state);

    /**
     * 根据年月分组统计数据
     *
     * @param userId    用户id
     * @param deptId    部门id
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param state     状态
     * @return 统计结果
     */
    List<ProcessStatisticsVo> countGroupByYearMonthType(@Param("userId") String userId, @Param("deptId") String deptId,
                                                        @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime,
                                                        @Param("state") String state);

    /**
     * 获取类别列表
     *
     * @return 类别集合
     */
    List<String> selectTypeList();

    /**
     * 查询展示数据列表
     * @param params 查询条件
     * @return TodoItemVo 集合
     * */
    List<TodoItemVo> selectVoList( TodoItem params);
    /**
     * 分发起人工单统计
     * @param params 查询条件
     * @return TodoItemVo 集合
     * */
    List<JSONObject> selectDlList(TodoItem params);
    /**
     * 分发起部门工单统计
     * @param params 查询条件
     * @return TodoItemVo 集合
     * */
    List<JSONObject> selectDeptList( TodoItem params);

    /**
     * 分发起时间工单统计
     * @param params 查询条件
     * @return TodoItemVo 集合
     * */
    List<JSONObject> selectTimeList( TodoItem params);

    /**
     * 获取工单状态
     * @return 工单状态列表
     */
    List<TodoItemVo> getState();

    /**
     * 根据sql语句查询统计信息
     * @param sqlStr sql语句
     * @return 查询统计列表
     * */
    @Select("${sqlStr}")
    List<UnifyStatisticsItemValue> selectStatisticsItemValueListBySql(@Param("sqlStr")String sqlStr);

   }
