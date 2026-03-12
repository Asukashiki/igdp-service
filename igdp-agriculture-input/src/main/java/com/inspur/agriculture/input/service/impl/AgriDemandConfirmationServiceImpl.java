package com.inspur.agriculture.input.service.impl;

import com.inspur.agriculture.input.domain.AgriDemandConfirmation;
import com.inspur.agriculture.input.mapper.AgriDemandConfirmationMapper;
import com.inspur.agriculture.input.service.IAgriDemandConfirmationService;
import com.inspur.agriculture.input.vo.demand_confirmation.AgriDemandConfirmationVO;
import com.inspur.common.utils.DateUtils;
import com.inspur.common.utils.SecurityUtils;
import com.inspur.common.utils.uuid.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 需求确认日志服务实现类
 *
 * @author igdp
 */
@Service
public class AgriDemandConfirmationServiceImpl implements IAgriDemandConfirmationService {

    @Autowired
    private AgriDemandConfirmationMapper agriDemandConfirmationMapper;

    /**
     * 查询需求确认日志列表
     */
    @Override
    public List<AgriDemandConfirmation> selectConfirmationList(AgriDemandConfirmation confirmation) {
        return agriDemandConfirmationMapper.selectConfirmationList(confirmation);
    }

    /**
     * 查询需求确认日志VO列表
     */
    @Override
    public List<AgriDemandConfirmationVO> selectConfirmationVOList(AgriDemandConfirmation confirmation) {
        return agriDemandConfirmationMapper.selectConfirmationVOList(confirmation);
    }

    /**
     * 根据ID查询需求确认日志
     */
    @Override
    public AgriDemandConfirmation selectConfirmationById(String confirmationId) {
        return agriDemandConfirmationMapper.selectConfirmationById(confirmationId);
    }

    /**
     * 根据ID查询需求确认日志VO
     */
    @Override
    public AgriDemandConfirmationVO selectConfirmationVOById(String confirmationId) {
        return agriDemandConfirmationMapper.selectConfirmationVOById(confirmationId);
    }

    /**
     * 根据发送方和接收方查询需求确认日志
     */
    @Override
    public List<AgriDemandConfirmation> selectConfirmationsByActors(String fromActor, String toActor) {
        return agriDemandConfirmationMapper.selectConfirmationsByActors(fromActor, toActor);
    }

    /**
     * 根据发送方和接收方查询需求确认日志VO
     */
    @Override
    public List<AgriDemandConfirmationVO> selectConfirmationVOsByActors(String fromActor, String toActor) {
        return agriDemandConfirmationMapper.selectConfirmationVOsByActors(fromActor, toActor);
    }

    /**
     * 新增需求确认日志
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int insertConfirmation(AgriDemandConfirmation confirmation) {
        // 生成确认ID
        confirmation.setConfirmationId(IdUtils.fastUUID());
        // 自动填充通用信息
        confirmation.setCreateTime(DateUtils.getNowDate());
        confirmation.setUpdateTime(DateUtils.getNowDate());
        try {
            String username = SecurityUtils.getUsername();
            confirmation.setCreatePeople(username);
            confirmation.setUpdatePeople(username);
        } catch (Exception e) {
            confirmation.setCreatePeople("system");
            confirmation.setUpdatePeople("system");
        }
        // 默认值设置
        confirmation.setDelFlag("0");
        // 设置确认时间
        if (confirmation.getConfirmedTime() == null) {
            confirmation.setConfirmedTime(DateUtils.getNowDate());
        }
        // 插入数据库
        return agriDemandConfirmationMapper.insert(confirmation);
    }

    /**
     * 批量删除需求确认日志
     */
    @Override
    public int deleteConfirmationByIds(String[] confirmationIds) {
        int rows = 0;
        for (String confirmationId : confirmationIds) {
            rows += deleteConfirmationById(confirmationId);
        }
        return rows;
    }

    /**
     * 逻辑删除需求确认日志
     */
    @Override
    public int deleteConfirmationById(String confirmationId) {
        AgriDemandConfirmation confirmation = new AgriDemandConfirmation();
        confirmation.setConfirmationId(confirmationId);
        confirmation.setDelFlag("2");
        confirmation.setUpdateTime(DateUtils.getNowDate());
        try {
            String username = SecurityUtils.getUsername();
            confirmation.setUpdatePeople(username);
        } catch (Exception e) {
            confirmation.setUpdatePeople("system");
        }
        return agriDemandConfirmationMapper.updateById(confirmation);
    }
}