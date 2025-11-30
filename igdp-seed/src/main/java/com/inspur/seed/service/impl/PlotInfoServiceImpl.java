package com.inspur.seed.service.impl;

import cn.hutool.core.util.IdUtil;
import com.inspur.common.utils.SecurityUtils;
import com.inspur.seed.domain.PlotInfo;
import com.inspur.seed.domain.SowingInfo;
import com.inspur.seed.mapper.PlotInfoMapper;
import com.inspur.seed.mapper.SowingInfoMapper;
import com.inspur.seed.service.IPlotInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * 地块信息Service实现类
 *
 * @author inspur
 */
@Service
public class PlotInfoServiceImpl implements IPlotInfoService {

    @Autowired
    private PlotInfoMapper plotInfoMapper;

    @Autowired
    private SowingInfoMapper sowingInfoMapper;

    @Override
    public List<PlotInfo> selectPlotInfoList(PlotInfo plotInfo) {
        return plotInfoMapper.selectPlotInfoList(plotInfo);
    }

    @Override
    public PlotInfo selectPlotInfoById(String groundId) {
        return plotInfoMapper.selectPlotInfoById(groundId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String insertPlotInfo(PlotInfo plotInfo) {
        // 生成主键
        String groundId = IdUtil.simpleUUID();
        plotInfo.setGroundId(groundId);

        // 设置创建信息
        plotInfo.setCreateTime(LocalDateTime.now());
        plotInfo.setCreateBy(SecurityUtils.getUsername());

        // 保存地块信息
        plotInfoMapper.insert(plotInfo);

        // 保存播种信息
        saveSowingList(groundId, plotInfo.getSowingList());

        return groundId;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updatePlotInfo(PlotInfo plotInfo) {
        // 设置更新信息
        plotInfo.setUpdateTime(LocalDateTime.now());
        plotInfo.setUpdateBy(SecurityUtils.getUsername());

        // 更新地块信息
        int rows = plotInfoMapper.updateById(plotInfo);

        // 删除原有播种信息
        sowingInfoMapper.deleteByGroundId(plotInfo.getGroundId());

        // 保存新的播种信息
        saveSowingList(plotInfo.getGroundId(), plotInfo.getSowingList());

        return rows;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deletePlotInfoByIds(String[] groundIds) {
        int count = 0;
        for (String groundId : groundIds) {
            // 删除播种信息
            sowingInfoMapper.deleteByGroundId(groundId);

            // 删除地块信息
            PlotInfo plotInfo = new PlotInfo();
            plotInfo.setGroundId(groundId);
            plotInfo.setIsDeleted(1);
            count += plotInfoMapper.updateById(plotInfo);
        }
        return count;
    }

    @Override
    public List<PlotInfo> selectPlotsByBatchId(String batchId) {
        return plotInfoMapper.selectPlotsByBatchId(batchId);
    }

    @Override
    public List<PlotInfo> selectPlotOptions(String batchId, String trialId) {
        return plotInfoMapper.selectPlotOptions(batchId, trialId);
    }

    /**
     * 保存播种信息列表
     */
    private void saveSowingList(String groundId, List<SowingInfo> sowingList) {
        if (CollectionUtils.isEmpty(sowingList)) {
            return;
        }

        Date now = new Date();
        for (SowingInfo sowing : sowingList) {
            sowing.setSowingId(IdUtil.simpleUUID());
            sowing.setGroundId(groundId);
            sowing.setCreateTime(now);
        }

        sowingInfoMapper.batchInsert(sowingList);
    }
}
