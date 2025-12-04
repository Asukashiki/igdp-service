package com.inspur.seed.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.inspur.common.exception.ServiceException;
import com.inspur.seed.constant.AuditLevelEnum;
import com.inspur.seed.domain.entity.DemandFarmerDetail;
import com.inspur.seed.domain.entity.DemandFarmerInputItem;
import com.inspur.seed.domain.entity.DemandSummary;
import com.inspur.seed.mapper.DemandFarmerDetailMapper;
import com.inspur.seed.mapper.DemandFarmerInputItemMapper;
import com.inspur.seed.mapper.DemandSummaryMapper;
import com.inspur.seed.service.IDemandSummaryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Demand Summary Service Implementation
 * 需求汇总服务实现
 *
 * @author igdp
 * @date 2025-12-04
 */
@Slf4j
@Service
public class DemandSummaryServiceImpl implements IDemandSummaryService {

    @Autowired
    private DemandSummaryMapper summaryMapper;

    @Autowired
    private DemandFarmerDetailMapper demandDetailMapper;

    @Autowired
    private DemandFarmerInputItemMapper inputItemMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void generateSummaryForDemand(String demandId) {
        // 1. 获取农民需求详情
        DemandFarmerDetail demand = demandDetailMapper.selectById(demandId);
        if (demand == null || demand.getIsDeleted() == 1) {
            throw new ServiceException("Demand not found");
        }

        // 2. 获取投入品明细
        LambdaQueryWrapper<DemandFarmerInputItem> itemWrapper = new LambdaQueryWrapper<>();
        itemWrapper.eq(DemandFarmerInputItem::getDemandId, demandId);
        itemWrapper.eq(DemandFarmerInputItem::getIsDeleted, 0);
        List<DemandFarmerInputItem> inputItems = inputItemMapper.selectList(itemWrapper);

        if (inputItems.isEmpty()) {
            log.warn("No input items found for demand: {}", demandId);
            return;
        }

        // 3. 为每个行政层级生成汇总数据
        String currentUserId = demand.getCreatedBy();
        Date now = new Date();

        // Kebele级别
        updateSummaryForLevel(demand.getBatchId(), AuditLevelEnum.VILLAGE.getCode(),
                demand.getKebele(), demand.getKebele(), demand.getWoreda(),
                inputItems, currentUserId, now);

        // Woreda级别
        updateSummaryForLevel(demand.getBatchId(), AuditLevelEnum.TOWN.getCode(),
                demand.getWoreda(), demand.getWoreda(), demand.getZone(),
                inputItems, currentUserId, now);

        // Zone级别
        if (demand.getZone() != null) {
            updateSummaryForLevel(demand.getBatchId(), AuditLevelEnum.DISTRICT.getCode(),
                    demand.getZone(), demand.getZone(), demand.getRegion(),
                    inputItems, currentUserId, now);
        }

        // Region级别
        if (demand.getRegion() != null) {
            updateSummaryForLevel(demand.getBatchId(), AuditLevelEnum.STATE.getCode(),
                    demand.getRegion(), demand.getRegion(), null,
                    inputItems, currentUserId, now);
        }

        log.info("Successfully generated summary for demand: {}", demandId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void regenerateSummaryForBatch(String batchId) {
        // 1. 删除该批次的所有汇总数据(逻辑删除)
        LambdaUpdateWrapper<DemandSummary> deleteWrapper = new LambdaUpdateWrapper<>();
        deleteWrapper.eq(DemandSummary::getBatchId, batchId);
        deleteWrapper.set(DemandSummary::getIsDeleted, 1);
        summaryMapper.update(null, deleteWrapper);

        // 2. 获取该批次所有已提交的农民需求
        LambdaQueryWrapper<DemandFarmerDetail> demandWrapper = new LambdaQueryWrapper<>();
        demandWrapper.eq(DemandFarmerDetail::getBatchId, batchId);
        demandWrapper.eq(DemandFarmerDetail::getIsDeleted, 0);
        demandWrapper.in(DemandFarmerDetail::getStatus, Arrays.asList("submitted", "approved", "locked"));
        List<DemandFarmerDetail> demands = demandDetailMapper.selectList(demandWrapper);

        // 3. 为每个需求重新生成汇总
        for (DemandFarmerDetail demand : demands) {
            try {
                generateSummaryForDemand(demand.getId());
            } catch (Exception e) {
                log.error("Failed to generate summary for demand: {}", demand.getId(), e);
            }
        }

        log.info("Successfully regenerated summary for batch: {}, processed {} demands", batchId, demands.size());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSummaryOnDemandDelete(String demandId) {
        // 1. 获取农民需求详情
        DemandFarmerDetail demand = demandDetailMapper.selectById(demandId);
        if (demand == null) {
            return;
        }

        // 2. 获取投入品明细
        LambdaQueryWrapper<DemandFarmerInputItem> itemWrapper = new LambdaQueryWrapper<>();
        itemWrapper.eq(DemandFarmerInputItem::getDemandId, demandId);
        itemWrapper.eq(DemandFarmerInputItem::getIsDeleted, 0);
        List<DemandFarmerInputItem> inputItems = inputItemMapper.selectList(itemWrapper);

        if (inputItems.isEmpty()) {
            return;
        }

        // 3. 减少每个层级的汇总数据
        String currentUserId = demand.getUpdatedBy();
        Date now = new Date();

        // Kebele级别
        decrementSummaryForLevel(demand.getBatchId(), AuditLevelEnum.VILLAGE.getCode(),
                demand.getKebele(), inputItems, currentUserId, now);

        // Woreda级别
        decrementSummaryForLevel(demand.getBatchId(), AuditLevelEnum.TOWN.getCode(),
                demand.getWoreda(), inputItems, currentUserId, now);

        // Zone级别
        if (demand.getZone() != null) {
            decrementSummaryForLevel(demand.getBatchId(), AuditLevelEnum.DISTRICT.getCode(),
                    demand.getZone(), inputItems, currentUserId, now);
        }

        // Region级别
        if (demand.getRegion() != null) {
            decrementSummaryForLevel(demand.getBatchId(), AuditLevelEnum.STATE.getCode(),
                    demand.getRegion(), inputItems, currentUserId, now);
        }

        log.info("Successfully updated summary on demand delete: {}", demandId);
    }

    /**
     * 更新指定层级的汇总数据
     */
    private void updateSummaryForLevel(String batchId, String adminLevel, String adminCode,
                                       String adminName, String parentAdminCode,
                                       List<DemandFarmerInputItem> inputItems,
                                       String userId, Date operateTime) {
        // 按投入品类型、品种分组
        Map<String, List<DemandFarmerInputItem>> groupedItems = inputItems.stream()
                .collect(Collectors.groupingBy(item ->
                        item.getInputCategory() + "|" + item.getInputType() + "|" +
                                (item.getVariety() != null ? item.getVariety() : "")));

        for (Map.Entry<String, List<DemandFarmerInputItem>> entry : groupedItems.entrySet()) {
            String[] keys = entry.getKey().split("\\|");
            String inputCategory = keys[0];
            String inputType = keys[1];
            String variety = keys.length > 2 && !keys[2].isEmpty() ? keys[2] : null;

            List<DemandFarmerInputItem> items = entry.getValue();
            BigDecimal totalQuantity = items.stream()
                    .map(DemandFarmerInputItem::getQuantity)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            // 查找是否已存在汇总记录
            LambdaQueryWrapper<DemandSummary> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(DemandSummary::getBatchId, batchId);
            queryWrapper.eq(DemandSummary::getAdminLevel, adminLevel);
            queryWrapper.eq(DemandSummary::getAdminCode, adminCode);
            queryWrapper.eq(DemandSummary::getInputCategory, inputCategory);
            queryWrapper.eq(DemandSummary::getInputType, inputType);
            if (variety != null) {
                queryWrapper.eq(DemandSummary::getVariety, variety);
            } else {
                queryWrapper.isNull(DemandSummary::getVariety);
            }
            queryWrapper.eq(DemandSummary::getIsDeleted, 0);

            DemandSummary existingSummary = summaryMapper.selectOne(queryWrapper);

            if (existingSummary != null) {
                // 更新已有记录
                existingSummary.setTotalQuantity(existingSummary.getTotalQuantity().add(totalQuantity));
                existingSummary.setFarmerCount(existingSummary.getFarmerCount() + 1);
                existingSummary.setUpdatedBy(userId);
                existingSummary.setUpdatedTime(operateTime);
                summaryMapper.updateById(existingSummary);
            } else {
                // 创建新记录
                DemandSummary newSummary = new DemandSummary();
                newSummary.setBatchId(batchId);
                newSummary.setAdminLevel(adminLevel);
                newSummary.setAdminCode(adminCode);
                newSummary.setAdminName(adminName);
                newSummary.setParentAdminCode(parentAdminCode);
                newSummary.setInputCategory(inputCategory);
                newSummary.setInputType(inputType);
                newSummary.setVariety(variety);
                newSummary.setTotalQuantity(totalQuantity);
                newSummary.setFarmerCount(1);
                newSummary.setStatus("pending");
                newSummary.setCreatedBy(userId);
                newSummary.setCreatedTime(operateTime);
                summaryMapper.insert(newSummary);
            }
        }
    }

    /**
     * 减少指定层级的汇总数据(删除需求时)
     */
    private void decrementSummaryForLevel(String batchId, String adminLevel, String adminCode,
                                          List<DemandFarmerInputItem> inputItems,
                                          String userId, Date operateTime) {
        // 按投入品类型、品种分组
        Map<String, List<DemandFarmerInputItem>> groupedItems = inputItems.stream()
                .collect(Collectors.groupingBy(item ->
                        item.getInputCategory() + "|" + item.getInputType() + "|" +
                                (item.getVariety() != null ? item.getVariety() : "")));

        for (Map.Entry<String, List<DemandFarmerInputItem>> entry : groupedItems.entrySet()) {
            String[] keys = entry.getKey().split("\\|");
            String inputCategory = keys[0];
            String inputType = keys[1];
            String variety = keys.length > 2 && !keys[2].isEmpty() ? keys[2] : null;

            List<DemandFarmerInputItem> items = entry.getValue();
            BigDecimal totalQuantity = items.stream()
                    .map(DemandFarmerInputItem::getQuantity)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            // 查找汇总记录
            LambdaQueryWrapper<DemandSummary> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(DemandSummary::getBatchId, batchId);
            queryWrapper.eq(DemandSummary::getAdminLevel, adminLevel);
            queryWrapper.eq(DemandSummary::getAdminCode, adminCode);
            queryWrapper.eq(DemandSummary::getInputCategory, inputCategory);
            queryWrapper.eq(DemandSummary::getInputType, inputType);
            if (variety != null) {
                queryWrapper.eq(DemandSummary::getVariety, variety);
            } else {
                queryWrapper.isNull(DemandSummary::getVariety);
            }
            queryWrapper.eq(DemandSummary::getIsDeleted, 0);

            DemandSummary existingSummary = summaryMapper.selectOne(queryWrapper);

            if (existingSummary != null) {
                // 减少数量
                BigDecimal newQuantity = existingSummary.getTotalQuantity().subtract(totalQuantity);
                int newFarmerCount = existingSummary.getFarmerCount() - 1;

                if (newQuantity.compareTo(BigDecimal.ZERO) <= 0 || newFarmerCount <= 0) {
                    // 如果数量为0或农民数为0,逻辑删除该记录
                    existingSummary.setIsDeleted(1);
                    existingSummary.setUpdatedBy(userId);
                    existingSummary.setUpdatedTime(operateTime);
                    summaryMapper.updateById(existingSummary);
                } else {
                    // 更新数量
                    existingSummary.setTotalQuantity(newQuantity);
                    existingSummary.setFarmerCount(newFarmerCount);
                    existingSummary.setUpdatedBy(userId);
                    existingSummary.setUpdatedTime(operateTime);
                    summaryMapper.updateById(existingSummary);
                }
            }
        }
    }
}
