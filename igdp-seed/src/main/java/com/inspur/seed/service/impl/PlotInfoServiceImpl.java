package com.inspur.seed.service.impl;

import com.inspur.common.exception.ServiceException;
import com.inspur.common.utils.SecurityUtils;
import com.inspur.seed.domain.PlotInfo;
import com.inspur.seed.mapper.PlotInfoMapper;
import com.inspur.seed.service.IPlotInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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

    @Override
    public List<PlotInfo> selectPlotInfoList(PlotInfo plotInfo) {
        return plotInfoMapper.selectPlotInfoList(plotInfo);
    }

    @Override
    public PlotInfo selectPlotInfoById(String plotId) {
        return plotInfoMapper.selectPlotInfoById(plotId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String insertPlotInfo(PlotInfo plotInfo) {
        // 生成地块ID: {trial_id}-P{replication_no}{row_no}{column_no}
        String plotId = generatePlotId(plotInfo.getTrialId(), plotInfo.getReplicationNo(),
                                        plotInfo.getRowNo(), plotInfo.getColumnNo());
        plotInfo.setPlotId(plotId);

        // 设置创建信息
        plotInfo.setCreateTime(LocalDateTime.now());
        plotInfo.setCreateBy(SecurityUtils.getUsername());

        // 保存地块信息
        plotInfoMapper.insert(plotInfo);

        return plotId;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updatePlotInfo(PlotInfo plotInfo) {
        // 设置更新信息
        plotInfo.setUpdateTime(LocalDateTime.now());
        plotInfo.setUpdateBy(SecurityUtils.getUsername());

        // 更新地块信息
        return plotInfoMapper.updateById(plotInfo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deletePlotInfoByIds(String[] plotIds) {
        int count = 0;
        for (String plotId : plotIds) {
            // 删除地块信息 - 使用deleteById方法，让@TableLogic自动处理逻辑删除
            boolean success = plotInfoMapper.deleteById(plotId) > 0;
            if (success) {
                count++;
            }
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
     * 生成地块ID
     * 格式: {trial_id}-P{replication_no}{row_no}{column_no}
     */
    private String generatePlotId(String trialId, Integer replicationNo, Integer rowNo, Integer columnNo) {
        if (trialId == null || trialId.isEmpty()) {
            throw new ServiceException("试验ID不能为空");
        }
        if (replicationNo == null) {
            throw new ServiceException("重复组编号不能为空");
        }
        if (rowNo == null) {
            throw new ServiceException("行号不能为空");
        }
        if (columnNo == null) {
            throw new ServiceException("列号不能为空");
        }

        return String.format("%s-P%d%d%d", trialId, replicationNo, rowNo, columnNo);
    }
}
