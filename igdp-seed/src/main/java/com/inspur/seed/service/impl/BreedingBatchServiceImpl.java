package com.inspur.seed.service.impl;

import cn.hutool.core.util.IdUtil;
import com.inspur.common.exception.ServiceException;
import com.inspur.common.utils.MessageUtils;
import com.inspur.common.utils.SecurityUtils;
import com.inspur.seed.domain.BreedingBatch;
import com.inspur.seed.mapper.BreedingBatchMapper;
import com.inspur.seed.service.IBreedingBatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * 育种批次信息Service实现类
 *
 * @author inspur
 */
@Service
public class BreedingBatchServiceImpl implements IBreedingBatchService {

    @Autowired
    private BreedingBatchMapper breedingBatchMapper;

    @Override
    public List<BreedingBatch> selectBreedingBatchList(BreedingBatch breedingBatch) {
        List<BreedingBatch> list = breedingBatchMapper.selectBreedingBatchList(breedingBatch);
        // 为每个对象设置中文名称
        list.forEach(this::setChineseNames);
        return list;
    }

    @Override
    public BreedingBatch selectBreedingBatchById(String dataId) {
        BreedingBatch batch = breedingBatchMapper.selectById(dataId);
        if (batch != null) {
            setChineseNames(batch);
        }
        return batch;
    }

    @Override
    public String insertBreedingBatch(BreedingBatch breedingBatch) {
        // 生成主键
        String dataId = IdUtil.simpleUUID();
        breedingBatch.setDataId(dataId);

        // 生成育种批次ID
        int year = Calendar.getInstance().get(Calendar.YEAR);
        String batchId = generateBatchId(year);
        breedingBatch.setBatchId(batchId);

        // 设置创建信息
        breedingBatch.setCreateTime(LocalDateTime.now());
        breedingBatch.setCreateBy(SecurityUtils.getUsername());

        // 数据校验
        validateBreedingBatch(breedingBatch);

        breedingBatchMapper.insert(breedingBatch);
        return dataId;
    }

    @Override
    public int updateBreedingBatch(BreedingBatch breedingBatch) {
        // 数据校验
        validateBreedingBatch(breedingBatch);

        // 设置更新信息
        breedingBatch.setUpdateTime(LocalDateTime.now());
        breedingBatch.setUpdateBy(SecurityUtils.getUsername());

        return breedingBatchMapper.updateById(breedingBatch);
    }

    @Override
    public int deleteBreedingBatchByIds(String[] dataIds) {
//        int count = 0;
//        for (String dataId : dataIds) {
//            BreedingBatch batch = new BreedingBatch();
//            batch.setDataId(dataId);
//            batch.setIsDeleted(1);
//            count += breedingBatchMapper.updateById(batch);
//        }
//        return count;
        return breedingBatchMapper.deleteBatchIds(Arrays.asList(dataIds));
    }

    @Override
    public List<BreedingBatch> selectBatchOptions() {
        List<BreedingBatch> list = breedingBatchMapper.selectBatchOptions();
        // 为每个对象设置中文名称
        list.forEach(this::setChineseNames);
        return list;
    }

    /**
     * 生成育种批次ID
     */
    private String generateBatchId(int year) {
        String batchId = breedingBatchMapper.generateBatchId(year);
        if (batchId == null) {
            batchId = "BREED" + year + "000001";
        }
        return batchId;
    }

    /**
     * 设置中文名称
     */
    private void setChineseNames(BreedingBatch batch) {
        if (batch == null) {
            return;
        }
        // 设置作物类型中文名称
        batch.setCropTypeName(getCropTypeName(batch.getCropType()));
        // 设置繁育方法中文名称
        batch.setBreedingMethodName(getBreedingMethodName(batch.getBreedingMethod()));
    }

    /**
     * 获取作物类型名称（支持国际化）
     */
    private String getCropTypeName(String cropType) {
        if (cropType == null || cropType.isEmpty()) {
            return "";
        }
        // 转换为小写进行匹配
        String lowerCropType = cropType.toLowerCase();
        String messageKey = "crop.type." + lowerCropType;
        try {
            return MessageUtils.message(messageKey);
        } catch (Exception e) {
            // 如果找不到对应的国际化key，返回原值
            return cropType;
        }
    }

    /**
     * 获取繁育方法名称（支持国际化）
     */
    private String getBreedingMethodName(String breedingMethod) {
        if (breedingMethod == null || breedingMethod.isEmpty()) {
            return "";
        }
        // 转换为小写进行匹配
        String lowerMethod = breedingMethod.toLowerCase();
        String messageKey = "breeding.method." + lowerMethod;
        try {
            return MessageUtils.message(messageKey);
        } catch (Exception e) {
            // 如果找不到对应的国际化key，返回原值
            return breedingMethod;
        }
    }

    /**
     * 数据校验
     */
    private void validateBreedingBatch(BreedingBatch breedingBatch) {
        // 批次时间校验
        if (breedingBatch.getBatchTime() != null && breedingBatch.getStartDate() != null) {
            if (breedingBatch.getBatchTime().after(breedingBatch.getStartDate())) {
                throw new ServiceException("批次时间不能晚于计划起始时间");
            }
        }

        // 起止时间校验
        if (breedingBatch.getStartDate() != null && breedingBatch.getEndDate() != null) {
            if (breedingBatch.getStartDate().after(breedingBatch.getEndDate())) {
                throw new ServiceException("计划起始时间不能晚于计划结束时间");
            }
        }

        // 繁育年份校验
        if (breedingBatch.getYearOfDevelopment() != null) {
            if (breedingBatch.getYearOfDevelopment() < 1900 || breedingBatch.getYearOfDevelopment() > 2100) {
                throw new ServiceException("繁育年份需在1900-2100范围内");
            }
        }
    }
}
