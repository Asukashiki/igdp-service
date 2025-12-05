package com.inspur.seed.service.impl;

import cn.hutool.core.util.IdUtil;
import com.inspur.common.exception.ServiceException;
import com.inspur.common.utils.MessageUtils;
import com.inspur.common.utils.SecurityUtils;
import com.inspur.seed.domain.TrialBasic;
import com.inspur.seed.domain.TrialPlotRelation;
import com.inspur.seed.mapper.TrialBasicMapper;
import com.inspur.seed.mapper.TrialPlotRelationMapper;
import com.inspur.seed.service.ITrialBasicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 试验基础信息Service实现类
 *
 * @author inspur
 */
@Service
public class TrialBasicServiceImpl implements ITrialBasicService {

    @Autowired
    private TrialBasicMapper trialBasicMapper;

    @Autowired
    private TrialPlotRelationMapper trialPlotRelationMapper;

    @Override
    public List<TrialBasic> selectTrialBasicList(TrialBasic trialBasic) {
        List<TrialBasic> list = trialBasicMapper.selectTrialBasicList(trialBasic);
        return list;
    }

    @Override
    public TrialBasic selectTrialBasicById(String trialId) {
        TrialBasic trialBasic = trialBasicMapper.selectTrialBasicById(trialId);
        return trialBasic;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String insertTrialBasic(TrialBasic trialBasic) {
        // 检查试验名称唯一性
        if (trialBasicMapper.checkTrialNameUnique(trialBasic.getTrialName(), null) > 0) {
            throw new ServiceException("试验名称已存在");
        }

        // 生成试验ID: TR-{variety_code}-{location_id}-{year}-序号
        String trialId = generateTrialId(trialBasic.getBatchId(), trialBasic.getLocationId(), trialBasic.getYear());
        trialBasic.setTrialId(trialId);

        // 设置创建信息
        trialBasic.setCreateTime(LocalDateTime.now());
        trialBasic.setCreateBy(SecurityUtils.getUsername());

        // 保存试验信息
        trialBasicMapper.insert(trialBasic);

        return trialId;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateTrialBasic(TrialBasic trialBasic) {
        // 检查试验名称唯一性
        if (trialBasicMapper.checkTrialNameUnique(trialBasic.getTrialName(), trialBasic.getTrialId()) > 0) {
            throw new ServiceException("试验名称已存在");
        }

        // 设置更新信息
        trialBasic.setUpdateTime(LocalDateTime.now());
        trialBasic.setUpdateBy(SecurityUtils.getUsername());

        // 更新试验信息
        int rows = trialBasicMapper.updateById(trialBasic);

        // 删除原有关联关系
        trialPlotRelationMapper.deleteByTrialId(trialBasic.getTrialId());


        return rows;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteTrialBasicByIds(String[] trialIds) {
        int count = 0;
        for (String trialId : trialIds) {
            // 删除关联关系
            trialPlotRelationMapper.deleteByTrialId(trialId);

            // 删除试验信息 - 使用deleteById方法，让@TableLogic自动处理逻辑删除
            boolean success = trialBasicMapper.deleteById(trialId) > 0;
            if (success) {
                count++;
            }
        }
        return count;
    }

    @Override
    public List<TrialBasic> selectTrialOptions(String batchId) {
        return trialBasicMapper.selectTrialOptions(batchId);
    }

    /**
     * 生成试验ID
     * 格式: TR-{variety_code}-{location_id}-{year}-序号
     */
    private String generateTrialId(String batchId, String locationId, Integer year) {
        if (batchId == null || batchId.isEmpty()) {
            throw new ServiceException("育种批次ID不能为空");
        }
        if (locationId == null || locationId.isEmpty()) {
            throw new ServiceException("研究中心ID不能为空");
        }
        if (year == null) {
            throw new ServiceException("年份不能为空");
        }

        String trialId = trialBasicMapper.generateTrialIdByBatchAndLocationAndYear(batchId, locationId, year);
        if (trialId == null) {
            // 如果没有找到记录，需要从batch中获取variety_code来生成默认ID
            // 这里假设返回null时使用一个默认格式，实际应该从batch表查询variety_code
            throw new ServiceException("无法生成试验ID，请检查育种批次信息");
        }
        return trialId;
    }

    /**
     * 保存地块关联关系
     */
    private void savePlotRelations(String trialId, List<String> plotIds) {
        if (CollectionUtils.isEmpty(plotIds)) {
            return;
        }

        Date now = new Date();
        List<TrialPlotRelation> relationList = new ArrayList<>();
        for (String plotId : plotIds) {
            TrialPlotRelation relation = new TrialPlotRelation();
            relation.setRelationId(IdUtil.simpleUUID());
            relation.setTrialId(trialId);
            relation.setGroundId(plotId);
            relation.setCreateTime(now);
            relationList.add(relation);
        }

        trialPlotRelationMapper.batchInsert(relationList);
    }

    /**
     * 获取季节名称（支持国际化）
     */
    private String getSeasonName(String season) {
        if (season == null || season.isEmpty()) {
            return "";
        }
        String messageKey = "season." + season.toLowerCase();
        try {
            return MessageUtils.message(messageKey);
        } catch (Exception e) {
            // 如果找不到对应的国际化key，返回原值
            return season;
        }
    }
}
