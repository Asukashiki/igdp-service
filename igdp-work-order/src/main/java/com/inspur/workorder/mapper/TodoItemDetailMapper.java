package com.inspur.workorder.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.workorder.domain.TodoItemDetail;
import com.inspur.workorder.domain.vo.ProcessStatisticsVo;
import com.inspur.workorder.domain.vo.TodoItemDetailVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author liyunlong02
 * @version 1.0
 * @ClassName TodoItemDetailMapper
 * @date 2024/4/15 17:47
 */
@Mapper
public interface TodoItemDetailMapper extends BaseMapper<TodoItemDetail> {
    /**
     * 获取个人代办列表
     * @param userId 用户id
     * @param status 状态 0待办；1已办
     * @return 集合
     * */
    List<TodoItemDetailVo> selectVoList(@Param("userId")String userId,@Param("status") String status);

    /**
     * 统计用户处理工单明细
     * 状态是已处理的
     * @param deptId 部门id
     * @param userId 用户id
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param type 类型
     * @param status 状态
     * @param itemState 主流程状态
     * @return 统计结果
     * */
    List<ProcessStatisticsVo> countByUser(@Param("deptId") String deptId,@Param("userId") String userId, @Param("startTime") LocalDateTime startTime,
                                          @Param("endTime") LocalDateTime endTime, @Param("type") String type,@Param("status")String status,@Param("itemState")String itemState);

    /**
     * 统计用户处理的各类工单信息
     * @param userId 用户id
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param itemState 工单状态
     * @param status 明细状态
     * @param type 类型
     * @return 统计结果
     * */
    List<ProcessStatisticsVo> countByUserType(@Param("userId") String userId, @Param("startTime") LocalDateTime startTime,
                                              @Param("endTime") LocalDateTime endTime, @Param("type") String type,@Param("status")String status,@Param("itemState")String itemState);

    /**
     * 统计用户处理工单明细
     * 状态是已处理的
     * @param userId 用户id
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param status 状态
     * @return 统计结果
     * */
    List<ProcessStatisticsVo> countPersonalByType(@Param("userId") String userId, @Param("startTime") LocalDateTime startTime,
                                          @Param("endTime") LocalDateTime endTime,@Param("status") String status);
    /**
     * 统计用户各个状态的工单明细
     * 状态是已处理的
     * @param userId 用户id
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param type 类型
     * @return 统计结果
     * */
    List<ProcessStatisticsVo> countPersonalByStatus(@Param("userId") String userId, @Param("startTime") LocalDateTime startTime,
                                                  @Param("endTime") LocalDateTime endTime,@Param("type") String type);

    /**
     * 统计个人参与的办结工单
     * 个人参与的并且已办结的工单
     * @param userId 用户id
     * @param itemState 主信息状态，办结的为2
     * @return value
     * */
    Long countSuccessItemByUserId(@Param("userId") String userId,@Param("itemState")String itemState);

    /**
     * 统计部门以及类型处理的工单数量
     * @param status 状态
     * @param itemState 主流程状态
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 统计列表
     * */
    List<ProcessStatisticsVo> countGroupByDeptType(@Param("status")String status,@Param("startTime") LocalDateTime startTime,
                                                   @Param("endTime") LocalDateTime endTime,@Param("itemState")String itemState);
}
