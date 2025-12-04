package com.inspur.seed.service.impl;

import cn.hutool.core.util.IdUtil;
import com.inspur.common.exception.ServiceException;
import com.inspur.common.utils.SecurityUtils;
import com.inspur.seed.domain.FarmingRecord;
import com.inspur.seed.mapper.FarmingRecordMapper;
import com.inspur.seed.service.IFarmingRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 农事记录Service实现类
 *
 * @author inspur
 */
@Service
public class FarmingRecordServiceImpl implements IFarmingRecordService {

    @Autowired
    private FarmingRecordMapper farmingRecordMapper;

    @Override
    public List<FarmingRecord> selectFarmingRecordList(FarmingRecord farmingRecord) {
        return farmingRecordMapper.selectFarmingRecordList(farmingRecord);
    }

    @Override
    public FarmingRecord selectFarmingRecordById(String farmingId) {
        return farmingRecordMapper.selectFarmingRecordById(farmingId);
    }

    @Override
    public String insertFarmingRecord(FarmingRecord farmingRecord) {
        // 生成主键
        String farmingId = IdUtil.simpleUUID();
        farmingRecord.setFarmingId(farmingId);

        // 生成农事记录ID: {plot_id}-F{record_no}
        String farmingRecordId = generateFarmingRecordId(farmingRecord.getPlotId());
        farmingRecord.setFarmingRecordId(farmingRecordId);

        // 设置创建信息
        farmingRecord.setCreateTime(LocalDateTime.now());
        farmingRecord.setCreateBy(SecurityUtils.getUsername());

        farmingRecordMapper.insert(farmingRecord);
        return farmingId;
    }

    @Override
    public int updateFarmingRecord(FarmingRecord farmingRecord) {
        // 设置更新信息
        farmingRecord.setUpdateTime(LocalDateTime.now());
        farmingRecord.setUpdateBy(SecurityUtils.getUsername());

        return farmingRecordMapper.updateById(farmingRecord);
    }

    @Override
    public int deleteFarmingRecordByIds(String[] farmingIds) {
        int count = 0;
        for (String farmingId : farmingIds) {
            // 使用deleteById方法，让@TableLogic自动处理逻辑删除
            boolean success = farmingRecordMapper.deleteById(farmingId) > 0;
            if (success) {
                count++;
            }
        }
        return count;
    }

    /**
     * 生成农事记录ID
     * 格式: {plot_id}-F{record_no}
     */
    private String generateFarmingRecordId(String plotId) {
        if (plotId == null || plotId.isEmpty()) {
            throw new ServiceException("地块ID不能为空");
        }

        // 查询该地块下的最大记录编号
        int maxRecordNo = farmingRecordMapper.getMaxRecordNoByPlotId(plotId);
        int nextRecordNo = maxRecordNo + 1;

        return String.format("%s-F%d", plotId, nextRecordNo);
    }
}
