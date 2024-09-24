package com.inspur.workorder.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.inspur.common.annotation.DataSource;
import com.inspur.common.enums.DataSourceType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;

/**
 * @author liyunlong
 * @version 1.0
 * @ClassName IcdWorkFlowMapper
 * @date 2024/6/19 9:30
 */
@Mapper
@DataSource(value = DataSourceType.IST_WORKFLOW)
public interface IcdWorkFlowProcessMapper {
    /**
     * 获取个人待办统计数据
     *
     * @param userId        用户id
     * @param applicationId 应用id
     * @return 统计结果
     */
    Long countPersonalTodo(@Param("userId") String userId, @Param("applicationId") String applicationId);

    /**
     * 获取个人的办结数据
     * @param userId        用户id
     * @param applicationId 应用id
     * @return 统计结果
     */
    Long countMyHandleFinished(@Param("userId") String userId, @Param("applicationId") String applicationId);
    /**
     * 获取个人的已办数据
     * @param userId        用户id
     * @param applicationId 应用id
     * @return 统计结果
     */
    Long countMyHandle(@Param("userId") String userId, @Param("applicationId") String applicationId);

    /**
     * 获取个人的发起数据
     * @param userId        用户id
     * @param applicationId 应用id
     * @return 统计结果
     */
    Long countMyApply(@Param("userId") String userId, @Param("applicationId") String applicationId);

    /**
     * 统计流程数量
     * @param applicationId 应用id
     * @param stages 阶段状态数组
     * @return 统计结果
     * */
    Long countByStage(@Param("applicationId") String applicationId, @Param("stages") Integer[] stages, @Param("startTime")LocalDateTime startTime, @Param("endTime")LocalDateTime endTime);

}
