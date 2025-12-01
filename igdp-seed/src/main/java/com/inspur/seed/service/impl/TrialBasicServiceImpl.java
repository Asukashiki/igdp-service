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
        // 为每个对象设置中文名称
        list.forEach(this::setTranslatedNames);
        return list;
    }

    @Override
    public TrialBasic selectTrialBasicById(String trialId) {
        TrialBasic trialBasic = trialBasicMapper.selectTrialBasicById(trialId);
        if (trialBasic != null) {
            // 查询关联地块ID列表
            List<String> plotIds = trialPlotRelationMapper.selectPlotIdsByTrialId(trialId);
            trialBasic.setPlotIds(plotIds);
            // 设置翻译名称
            setTranslatedNames(trialBasic);
        }
        return trialBasic;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String insertTrialBasic(TrialBasic trialBasic) {
        // 检查试验名称唯一性
        if (trialBasicMapper.checkTrialNameUnique(trialBasic.getTrialName(), null) > 0) {
            throw new ServiceException("试验名称已存在");
        }

        // 生成主键
        String trialId = IdUtil.simpleUUID();
        trialBasic.setTrialId(trialId);

        // 设置创建信息
        trialBasic.setCreateTime(LocalDateTime.now());
        trialBasic.setCreateBy(SecurityUtils.getUsername());

        // 保存试验信息
        trialBasicMapper.insert(trialBasic);

        // 保存地块关联关系
        savePlotRelations(trialId, trialBasic.getPlotIds());

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

        // 保存新的关联关系
        savePlotRelations(trialBasic.getTrialId(), trialBasic.getPlotIds());

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
     * 设置翻译名称
     */
    private void setTranslatedNames(TrialBasic trialBasic) {
        if (trialBasic == null) {
            return;
        }
        // 设置季节名称（国际化）
        trialBasic.setSeasonName(getSeasonName(trialBasic.getSeason()));
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
