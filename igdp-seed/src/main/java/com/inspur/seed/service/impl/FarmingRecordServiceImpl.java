package com.inspur.seed.service.impl;

import cn.hutool.core.util.IdUtil;
import com.inspur.common.utils.MessageUtils;
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
        List<FarmingRecord> list = farmingRecordMapper.selectFarmingRecordList(farmingRecord);
        // 为每个对象设置翻译名称
        list.forEach(this::setTranslatedNames);
        return list;
    }

    @Override
    public FarmingRecord selectFarmingRecordById(String farmingId) {
        FarmingRecord record = farmingRecordMapper.selectFarmingRecordById(farmingId);
        if (record != null) {
            setTranslatedNames(record);
        }
        return record;
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
            // 使用deleteById方法，让@TableLogic自动处理逻辑删除
            boolean success = farmingRecordMapper.deleteById(farmingId) > 0;
            if (success) {
                count++;
            }
        }
        return count;
    }

    /**
     * 设置翻译名称
     */
    private void setTranslatedNames(FarmingRecord record) {
        if (record == null) {
            return;
        }
        // 设置操作类型名称（国际化）
        record.setOperationTypeName(getOperationTypeName(record.getOperationType()));
    }

    /**
     * 获取操作类型名称（支持国际化）
     */
    private String getOperationTypeName(String operationType) {
        if (operationType == null || operationType.isEmpty()) {
            return "";
        }
        String messageKey = "operation.type." + operationType.toLowerCase();
        try {
            return MessageUtils.message(messageKey);
        } catch (Exception e) {
            // 如果找不到对应的国际化key，返回原值
            return operationType;
        }
    }
}
