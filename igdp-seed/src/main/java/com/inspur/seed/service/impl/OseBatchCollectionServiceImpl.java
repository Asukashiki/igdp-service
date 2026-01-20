package com.inspur.seed.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.common.utils.StringUtils;
import com.inspur.seed.domain.OseBatchCollection;
import com.inspur.seed.mapper.OseBatchCollectionMapper;
import com.inspur.seed.service.IOseBatchCollectionService;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * OSE繁殖批次信息数据采集 - Service实现
 * OSE Batch Information Data Collection Service Implementation
 *
 * @author system
 * @date 2026-01-04
 */
@Service
public class OseBatchCollectionServiceImpl extends ServiceImpl<OseBatchCollectionMapper, OseBatchCollection>
        implements IOseBatchCollectionService {

    /**
     * 查询OSE繁殖批次采集列表
     *
     * @param oseBatchCollection 查询条件
     * @return 采集列表
     */
    @Override
    public List<OseBatchCollection> selectOseBatchCollectionList(OseBatchCollection oseBatchCollection) {
        LambdaQueryWrapper<OseBatchCollection> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotEmpty(oseBatchCollection.getBreedingBatchId()),
                        OseBatchCollection::getBreedingBatchId, oseBatchCollection.getBreedingBatchId())
                .like(StringUtils.isNotEmpty(oseBatchCollection.getVarietyName()),
                        OseBatchCollection::getVarietyName, oseBatchCollection.getVarietyName())
                .like(StringUtils.isNotEmpty(oseBatchCollection.getBatchId()),
                        OseBatchCollection::getBatchId, oseBatchCollection.getBatchId())
                .eq(OseBatchCollection::getDelFlag, "0")
                .orderByDesc(OseBatchCollection::getCreateTime);
        return list(queryWrapper);
    }

    /**
     * 新增OSE繁殖批次采集
     *
     * @param oseBatchCollection 采集信息
     * @return 结果
     */
    @Override
    public int insertOseBatchCollection(OseBatchCollection oseBatchCollection) {
        // 生成批次ID: OSE_YYYYMMDD_HHmmss_XXX
        String batchId = generateBatchId();
        oseBatchCollection.setBatchId(batchId);
        oseBatchCollection.setDelFlag("0");
        oseBatchCollection.setCreateTime(LocalDateTime.now());
        return baseMapper.insert(oseBatchCollection) > 0 ? 1 : 0;
    }

    /**
     * 修改OSE繁殖批次采集
     *
     * @param oseBatchCollection 采集信息
     * @return 结果
     */
    @Override
    public int updateOseBatchCollection(OseBatchCollection oseBatchCollection) {
        oseBatchCollection.setUpdateTime(LocalDateTime.now());
        return baseMapper.updateById(oseBatchCollection);
    }

    /**
     * 批量删除OSE繁殖批次采集（逻辑删除）
     *
     * @param ids 需要删除的ID数组
     * @return 结果
     */
    @Override
    public int deleteOseBatchCollectionByIds(Long[] ids) {
        int count = 0;
        for (Long id : ids) {
            OseBatchCollection oseBatchCollection = baseMapper.selectById(id);
            if (oseBatchCollection != null) {
                oseBatchCollection.setDelFlag("2");
                oseBatchCollection.setUpdateTime(LocalDateTime.now());
                count += baseMapper.updateById(oseBatchCollection);
            }
        }
        return count;
    }

    /**
     * 生成批次ID
     * 格式: OSE_YYYYMMDD_HHmmss_XXX
     *
     * @return 批次ID
     */
    private String generateBatchId() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String dateTime = sdf.format(new Date());

        // 查询当天已有的批次数量
        SimpleDateFormat dayFormat = new SimpleDateFormat("yyyyMMdd");
        String today = dayFormat.format(new Date());

        LambdaQueryWrapper<OseBatchCollection> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.likeRight(OseBatchCollection::getBatchId, "OSE_" + today)
                .eq(OseBatchCollection::getDelFlag, "0");
        long count = count(queryWrapper);

        // 序号从1开始，3位数字，不足补0
        String sequence = String.format("%03d", count + 1);

        return "OSE_" + dateTime + "_" + sequence;
    }
}
