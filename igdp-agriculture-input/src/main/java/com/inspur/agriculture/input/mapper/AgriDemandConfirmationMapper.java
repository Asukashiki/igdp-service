package com.inspur.agriculture.input.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.agriculture.input.domain.AgriDemandConfirmation;
import com.inspur.agriculture.input.vo.demand_confirmation.AgriDemandConfirmationVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 需求确认日志Mapper接口
 *
 * @author igdp
 */
public interface AgriDemandConfirmationMapper extends BaseMapper<AgriDemandConfirmation> {

    /**
     * 查询需求确认日志列表
     *
     * @param confirmation 需求确认日志
     * @return 需求确认日志集合
     */
    List<AgriDemandConfirmation> selectConfirmationList(AgriDemandConfirmation confirmation);

    /**
     * 查询需求确认日志VO列表
     *
     * @param confirmation 需求确认日志
     * @return 需求确认日志VO集合
     */
    List<AgriDemandConfirmationVO> selectConfirmationVOList(AgriDemandConfirmation confirmation);

    /**
     * 根据ID查询需求确认日志
     *
     * @param confirmationId 确认ID
     * @return 需求确认日志
     */
    AgriDemandConfirmation selectConfirmationById(@Param("confirmationId") String confirmationId);

    /**
     * 根据ID查询需求确认日志VO
     *
     * @param confirmationId 确认ID
     * @return 需求确认日志VO
     */
    AgriDemandConfirmationVO selectConfirmationVOById(@Param("confirmationId") String confirmationId);

    /**
     * 根据发送方和接收方查询需求确认日志
     *
     * @param fromActor 发送方
     * @param toActor 接收方
     * @return 需求确认日志集合
     */
    List<AgriDemandConfirmation> selectConfirmationsByActors(@Param("fromActor") String fromActor, @Param("toActor") String toActor);

    /**
     * 根据发送方和接收方查询需求确认日志VO
     *
     * @param fromActor 发送方
     * @param toActor 接收方
     * @return 需求确认日志VO集合
     */
    List<AgriDemandConfirmationVO> selectConfirmationVOsByActors(@Param("fromActor") String fromActor, @Param("toActor") String toActor);
}