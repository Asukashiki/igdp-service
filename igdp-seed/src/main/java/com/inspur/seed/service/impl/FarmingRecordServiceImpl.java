package com.inspur.seed.service.impl;

import cn.hutool.core.util.IdUtil;
import com.inspur.common.utils.SecurityUtils;
import com.inspur.seed.domain.FarmingRecord;
import com.inspur.seed.mapper.FarmingRecordMapper;
import com.inspur.seed.service.IFarmingRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
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
            FarmingRecord record = new FarmingRecord();
            record.setFarmingId(farmingId);
            record.setIsDeleted(1);
            count += farmingRecordMapper.updateById(record);
        }
        return count;
    }
}
