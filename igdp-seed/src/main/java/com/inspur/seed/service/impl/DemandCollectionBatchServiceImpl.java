package com.inspur.seed.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.common.exception.ServiceException;
import com.inspur.seed.constant.BatchStatusEnum;
import com.inspur.seed.domain.entity.DemandCollectionBatch;
import com.inspur.seed.mapper.DemandCollectionBatchMapper;
import com.inspur.seed.service.IDemandCollectionBatchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static cn.hutool.core.util.RandomUtil.randomString;

/**
 * Demand Collection Batch Service Implementation
 * 需求采集批次管理服务实现
 *
 * @author igdp
 * @date 2025-12-05
 */
@Slf4j
@Service
public class DemandCollectionBatchServiceImpl extends ServiceImpl<DemandCollectionBatchMapper, DemandCollectionBatch> implements IDemandCollectionBatchService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DemandCollectionBatch getOrCreateBatchByYear(String batchNo,Integer year) {
        if (year == null) {
            throw new ServiceException("Year cannot be empty");
        }

//        // 查询当前年份的批次
//        LambdaQueryWrapper<DemandCollectionBatch> wrapper = new LambdaQueryWrapper<>();
//        wrapper.eq(DemandCollectionBatch::getYear, year);
//        wrapper.eq(DemandCollectionBatch::getIsDeleted, 0);
//        wrapper.last("LIMIT 1");
//
//        DemandCollectionBatch batch = this.getOne(wrapper);

        // 如果不存在，则自动创建
//        if (batch == null) {
            DemandCollectionBatch batch = new DemandCollectionBatch();
            batch.setBatchName(year + "年度农民需求采集");
            batch.setBatchNo(batchNo);
            batch.setYear(year);
            batch.setStatus(BatchStatusEnum.COLLECTING.getCode());

            // 设置批次时间范围：1月1日到12月31日
            java.util.Calendar calendar = java.util.Calendar.getInstance();
            calendar.set(year, java.util.Calendar.JANUARY, 1, 0, 0, 0);
            calendar.set(java.util.Calendar.MILLISECOND, 0);
            batch.setStartDate(calendar.getTime());

            calendar.set(year, java.util.Calendar.DECEMBER, 31, 23, 59, 59);
            batch.setEndDate(calendar.getTime());

            batch.setCreatedTime(new Date());
            // TODO: Get current user ID from security context
            batch.setCreatedBy("system");
            if (!this.save(batch)) {
                throw new ServiceException("Failed to create batch for year: " + year);
            }
            log.info("Auto-created demand collection batch for year {}: {}", year, batch.getId());

        return batch;
    }
}
