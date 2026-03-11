package com.inspur.agriculture.input.service;

import com.inspur.agriculture.input.domain.AgriDemandConfirmation;
import com.inspur.agriculture.input.vo.demand_confirmation.AgriDemandConfirmationVO;

import java.util.List;

/**
 * 需求确认日志服务接口
 *
 * @author igdp
 */
public interface IAgriDemandConfirmationService {

    /**
     * 查询需求确认日志列表
     *
     * @param confirmation 需求确认日志查询条件
     * @return 需求确认日志列表
     */
    List<AgriDemandConfirmation> selectConfirmationList(AgriDemandConfirmation confirmation);

    /**
     * 查询需求确认日志VO列表
     *
     * @param confirmation 需求确认日志查询条件
     * @return 需求确认日志VO列表
     */
    List<AgriDemandConfirmationVO> selectConfirmationVOList(AgriDemandConfirmation confirmation);

    /**
     * 根据ID查询需求确认日志
     *
     * @param confirmationId 确认ID
     * @return 需求确认日志信息
     */
    AgriDemandConfirmation selectConfirmationById(String confirmationId);

    /**
     * 根据ID查询需求确认日志VO
     *
     * @param confirmationId 确认ID
     * @return 需求确认日志VO信息
     */
    AgriDemandConfirmationVO selectConfirmationVOById(String confirmationId);

    /**
     * 根据发送方和接收方查询需求确认日志
     *
     * @param fromActor 发送方
     * @param toActor 接收方
     * @return 需求确认日志列表
     */
    List<AgriDemandConfirmation> selectConfirmationsByActors(String fromActor, String toActor);

    /**
     * 根据发送方和接收方查询需求确认日志VO
     *
     * @param fromActor 发送方
     * @param toActor 接收方
     * @return 需求确认日志VO列表
     */
    List<AgriDemandConfirmationVO> selectConfirmationVOsByActors(String fromActor, String toActor);

    /**
     * 新增需求确认日志
     *
     * @param confirmation 需求确认日志信息
     * @return 新增结果（影响行数）
     */
    int insertConfirmation(AgriDemandConfirmation confirmation);

    /**
     * 批量删除需求确认日志
     *
     * @param confirmationIds 需要删除的确认ID数组
     * @return 删除结果（影响行数）
     */
    int deleteConfirmationByIds(String[] confirmationIds);

    /**
     * 删除需求确认日志（逻辑删除）
     *
     * @param confirmationId 确认ID
     * @return 删除结果（影响行数）
     */
    int deleteConfirmationById(String confirmationId);
}