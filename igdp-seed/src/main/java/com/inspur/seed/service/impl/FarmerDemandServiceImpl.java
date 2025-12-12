package com.inspur.seed.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.agriculture.input.domain.oauth.PubRegion;
import com.inspur.agriculture.input.dto.demand.DemandInputSummaryDTO;
import com.inspur.agriculture.input.dto.demand.DemandInputSummaryItemDTO;
import com.inspur.agriculture.input.mapper.oauth.PubRegionMapper;
import com.inspur.agriculture.input.service.demand.IDemandInputSummaryItemService;
import com.inspur.common.exception.ServiceException;
import com.inspur.seed.constant.AuditLevelEnum;
import com.inspur.seed.constant.DemandStatusEnum;
import com.inspur.seed.constant.CategoryEnum;
import com.inspur.seed.domain.dto.DemandOrganDTO;
import com.inspur.seed.domain.dto.FarmerDemandAddDTO;
import com.inspur.seed.domain.dto.FarmerDemandPageDTO;
import com.inspur.seed.domain.dto.FarmerDemandUpdateDTO;
import com.inspur.seed.domain.entity.DemandAuditRecord;
import com.inspur.seed.domain.entity.DemandCollectionBatch;
import com.inspur.seed.domain.entity.DemandFarmerDetail;
import com.inspur.seed.domain.entity.DemandFarmerInputItem;
import com.inspur.seed.domain.vo.FarmerDemandDetailVO;
import com.inspur.seed.domain.vo.FarmerDemandPageVO;
import com.inspur.seed.domain.vo.FarmerInputAggregationVO;
import com.inspur.seed.mapper.DemandAuditRecordMapper;
import com.inspur.seed.mapper.DemandCollectionBatchMapper;
import com.inspur.seed.mapper.DemandFarmerDetailMapper;
import com.inspur.seed.mapper.DemandFarmerInputItemMapper;
import com.inspur.seed.service.IDemandCollectionBatchService;
import com.inspur.seed.service.IFarmerDemandService;
import com.inspur.seed.service.IDemandSummaryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Farmer Demand Service Implementation
 *
 * @author igdp
 * @date 2025-12-04
 */
@Slf4j
@Service
public class FarmerDemandServiceImpl extends ServiceImpl<DemandFarmerDetailMapper, DemandFarmerDetail> implements IFarmerDemandService {

    @Autowired
    private DemandCollectionBatchMapper batchMapper;

    @Autowired
    private DemandFarmerInputItemMapper inputItemMapper;

    @Autowired
    private DemandAuditRecordMapper auditRecordMapper;

    @Autowired
    private IDemandSummaryService summaryService;

    @Autowired
    private IDemandCollectionBatchService batchService;


    @Autowired
    private IDemandInputSummaryItemService demandInputSummaryItemService;

    @Autowired
    private PubRegionMapper regionMapper;


    private static final String STATUS_DRAFT = "DRAFT";
    private static final String STATUS_REJECTED = "REJECTED";

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String addFarmerDemand(FarmerDemandAddDTO dto) {
        // 根据当前年份自动获取或创建批次
        int currentYear = java.time.Year.now().getValue();
        DemandCollectionBatch batch = batchService.getOrCreateBatchByYear(currentYear);

        // 设置批次ID
        dto.setBatchId(batch.getId());


        // Create farmer demand detail
        DemandFarmerDetail detail = BeanUtil.copyProperties(dto, DemandFarmerDetail.class);
        // 直接赋值状态字符串（替代原DemandStatusEnum.DRAFT.getCode()）
        detail.setStatus(STATUS_DRAFT);
        detail.setCreatedTime(new Date());

        // TODO: Get current user ID and name from security context
        detail.setDaUserId("current_user_id");
        detail.setDaUserName("current_user_name");
        detail.setCreatedBy("current_user_id");

        // Calculate max seed and fertilizer quantities (simplified version)
        detail.setMaxSeedQuantity(calculateMaxSeedQuantity(dto.getLandArea(), dto.getInputItems()));
        detail.setMaxFertilizerQuantity(calculateMaxFertilizerQuantity(dto.getLandArea(), dto.getInputItems()));

        // Save farmer demand detail
        if (!this.save(detail)) {
            throw new ServiceException("Failed to save farmer demand");
        }

        // 4. Save input items
        List<DemandFarmerInputItem> inputItems = dto.getInputItems().stream().map(item -> {
            DemandFarmerInputItem inputItem = BeanUtil.copyProperties(item, DemandFarmerInputItem.class);
            inputItem.setDemandId(detail.getId());
            inputItem.setCreatedTime(new Date());
            inputItem.setCreatedBy("current_user_id");
            // 新增：给variety字段赋值（空字符串，避免无默认值报错）
            inputItem.setVariety(StrUtil.blankToDefault(item.getVariety(), ""));
            return inputItem;
        }).collect(Collectors.toList());

        for (DemandFarmerInputItem inputItem : inputItems) {
            inputItemMapper.insert(inputItem);
        }

        return detail.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateFarmerDemand(FarmerDemandUpdateDTO dto) {
        // 1. Validate demand exists
        DemandFarmerDetail demand = this.getById(dto.getId());
        if (demand == null || demand.getIsDeleted() == 1) {
            throw new ServiceException("Demand not found");
        }

        // 2. Validate status is draft or rejected
        // 替换原枚举判断，直接用字符串比较
        if (!STATUS_DRAFT.equals(demand.getStatus())
                && !STATUS_REJECTED.equals(demand.getStatus())) {
            throw new ServiceException("Can only update demand in draft or rejected status");
        }

        // 3. Validate current user is the DA who created the demand
        // TODO: Get current user ID from security context
        String currentUserId = "current_user_id";
        if (!currentUserId.equals(demand.getDaUserId())) {
            throw new ServiceException("Only the creator can update this demand");
        }
        

        // 5. Update farmer demand detail
        DemandFarmerDetail updatedDetail = BeanUtil.copyProperties(dto, DemandFarmerDetail.class);
        updatedDetail.setUpdatedTime(new Date());
        updatedDetail.setUpdatedBy(currentUserId);
        // 直接赋值状态字符串（替代原DemandStatusEnum.DRAFT.getCode()）
        updatedDetail.setStatus(STATUS_DRAFT); // Reset to draft
        updatedDetail.setCurrentAuditLevel(null); // Clear audit level

        // Recalculate max quantities
        updatedDetail.setMaxSeedQuantity(calculateMaxSeedQuantity(dto.getLandArea(), dto.getInputItems()));
        updatedDetail.setMaxFertilizerQuantity(calculateMaxFertilizerQuantity(dto.getLandArea(), dto.getInputItems()));

        // Use optimistic lock for update
        if (!this.updateById(updatedDetail)) {
            throw new ServiceException("Update failed, please retry");
        }

        // 6. Delete old input items and insert new ones
        LambdaUpdateWrapper<DemandFarmerInputItem> deleteWrapper = new LambdaUpdateWrapper<>();
        deleteWrapper.eq(DemandFarmerInputItem::getDemandId, dto.getId());
        deleteWrapper.set(DemandFarmerInputItem::getIsDeleted, 1);
        inputItemMapper.update(null, deleteWrapper);

        // Insert new input items
        List<DemandFarmerInputItem> inputItems = dto.getInputItems().stream().map(item -> {
            DemandFarmerInputItem inputItem = BeanUtil.copyProperties(item, DemandFarmerInputItem.class);
            inputItem.setDemandId(dto.getId());
            inputItem.setCreatedTime(new Date());
            inputItem.setCreatedBy(currentUserId);
            // 新增：给variety字段赋值（空字符串，避免无默认值报错）
            inputItem.setVariety(StrUtil.blankToDefault(item.getVariety(), ""));
            return inputItem;
        }).collect(Collectors.toList());

        for (DemandFarmerInputItem inputItem : inputItems) {
            inputItemMapper.insert(inputItem);
        }

        return true;
    }

    @Override
    public FarmerDemandDetailVO getFarmerDemandDetail(String id) {
        // 1. 获取主表数据（原有逻辑不变）
        DemandFarmerDetail demand = this.getById(id);
        if (demand == null || demand.getIsDeleted() == 1) {
            throw new ServiceException("Demand not found");
        }

        // 2. 转换主表VO（原有逻辑不变）
        FarmerDemandDetailVO vo = BeanUtil.copyProperties(demand, FarmerDemandDetailVO.class);
        DemandCollectionBatch batch = batchMapper.selectById(demand.getBatchId());
        if (batch != null) {
            vo.setBatchNo(batch.getBatchNo());
        }
        DemandStatusEnum statusEnum = DemandStatusEnum.fromCode(demand.getStatus());
        if (statusEnum != null) {
            vo.setStatusName(statusEnum.getMessage());
        }
        if (StrUtil.isNotBlank(demand.getCurrentAuditLevel())) {
            AuditLevelEnum levelEnum = AuditLevelEnum.fromCode(demand.getCurrentAuditLevel());
            if (levelEnum != null) {
                vo.setCurrentAuditLevelName(levelEnum.getMessage());
            }
        }

        // 3. 获取投入品明细（重点修改这部分）
        LambdaQueryWrapper<DemandFarmerInputItem> itemWrapper = new LambdaQueryWrapper<>();
        itemWrapper.eq(DemandFarmerInputItem::getDemandId, id);
        itemWrapper.eq(DemandFarmerInputItem::getIsDeleted, 0);
        List<DemandFarmerInputItem> inputItems = inputItemMapper.selectList(itemWrapper);

        List<FarmerDemandDetailVO.InputItemVO> inputItemVOs = inputItems.stream().map(item -> {
            FarmerDemandDetailVO.InputItemVO itemVO = new FarmerDemandDetailVO.InputItemVO();

            // ========== 关键修正：字段映射 ==========
            // 表的 input_type（大类）→ VO 的 inputCategory（前端显示的大类）
            itemVO.setInputCategory(item.getInputType());
            // 表的 input_category（小类）→ VO 的 inputType（前端可新增显示小类）
            itemVO.setInputType(item.getInputCategory());

            // 原有字段赋值（不变）
            itemVO.setVariety(item.getVariety());
            itemVO.setUnit(item.getUnit());
            itemVO.setQuantity(item.getQuantity());
            itemVO.setSpecification(item.getSpecification()); // 如有该字段

            // 修正：用表的 input_type（大类）匹配 CategoryEnum，获取大类名称
            CategoryEnum categoryEnum = CategoryEnum.getByCode(item.getInputType());
            if (categoryEnum != null) {
                itemVO.setInputCategoryName(categoryEnum.getDesc());
            }

            return itemVO;
        }).collect(Collectors.toList());
        vo.setInputItems(inputItemVOs);

        // 4. 审计记录（原有逻辑不变）
        LambdaQueryWrapper<DemandAuditRecord> auditWrapper = new LambdaQueryWrapper<>();
        auditWrapper.eq(DemandAuditRecord::getDemandId, id);
        auditWrapper.eq(DemandAuditRecord::getIsDeleted, 0);
        auditWrapper.orderByDesc(DemandAuditRecord::getAuditTime);
        List<DemandAuditRecord> auditRecords = auditRecordMapper.selectList(auditWrapper);
        List<FarmerDemandDetailVO.AuditRecordVO> auditRecordVOs = auditRecords.stream().map(record -> {
            FarmerDemandDetailVO.AuditRecordVO recordVO = BeanUtil.copyProperties(record, FarmerDemandDetailVO.AuditRecordVO.class);
            AuditLevelEnum levelEnum = AuditLevelEnum.fromCode(record.getAuditLevel());
            if (levelEnum != null) {
                recordVO.setAuditLevelName(levelEnum.getMessage());
            }
            return recordVO;
        }).collect(Collectors.toList());
        vo.setAuditRecords(auditRecordVOs);

        return vo;
    }

    @Override
    public Page<FarmerDemandPageVO> getFarmerDemandPage(FarmerDemandPageDTO dto) {
        // 1. Build query wrapper
        LambdaQueryWrapper<DemandFarmerDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DemandFarmerDetail::getIsDeleted, 0);

        // Filter by batch ID
        if (StrUtil.isNotBlank(dto.getBatchId())) {
            wrapper.eq(DemandFarmerDetail::getBatchId, dto.getBatchId());
        }

        // Filter by farmer name (fuzzy)
        if (StrUtil.isNotBlank(dto.getFarmerName())) {
            wrapper.like(DemandFarmerDetail::getFarmerName, dto.getFarmerName());
        }

        // Filter by farmer ID number
        if (StrUtil.isNotBlank(dto.getFarmerIdNumber())) {
            wrapper.eq(DemandFarmerDetail::getFarmerIdNumber, dto.getFarmerIdNumber());
        }

        // Filter by administrative divisions
        if (StrUtil.isNotBlank(dto.getKebele())) {
            wrapper.eq(DemandFarmerDetail::getKebele, dto.getKebele());
        }
        if (StrUtil.isNotBlank(dto.getWoreda())) {
            wrapper.eq(DemandFarmerDetail::getWoreda, dto.getWoreda());
        }
        if (StrUtil.isNotBlank(dto.getZone())) {
            wrapper.eq(DemandFarmerDetail::getZone, dto.getZone());
        }
        if (StrUtil.isNotBlank(dto.getVillage())) {
            wrapper.eq(DemandFarmerDetail::getVillage, dto.getVillage());
        }

        // Filter by status
        if (StrUtil.isNotBlank(dto.getStatus())) {
            wrapper.eq(DemandFarmerDetail::getStatus, dto.getStatus());
        }

        // Filter by current audit level
        if (StrUtil.isNotBlank(dto.getCurrentAuditLevel())) {
            wrapper.eq(DemandFarmerDetail::getCurrentAuditLevel, dto.getCurrentAuditLevel());
        }

        // Filter by created time range
        if (StrUtil.isNotBlank(dto.getCreatedTimeStart())) {
            wrapper.ge(DemandFarmerDetail::getCreatedTime, dto.getCreatedTimeStart());
        }
        if (StrUtil.isNotBlank(dto.getCreatedTimeEnd())) {
            wrapper.le(DemandFarmerDetail::getCreatedTime, dto.getCreatedTimeEnd() + " 23:59:59");
        }

        // Order by created time desc
        wrapper.orderByDesc(DemandFarmerDetail::getCreatedTime);

        // 2. Query page
        Page<DemandFarmerDetail> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        Page<DemandFarmerDetail> resultPage = this.page(page, wrapper);

        // 3. Convert to VO
        Page<FarmerDemandPageVO> voPage = new Page<>(resultPage.getCurrent(), resultPage.getSize(), resultPage.getTotal());
        List<FarmerDemandPageVO> voList = resultPage.getRecords().stream().map(demand -> {
            FarmerDemandPageVO vo = BeanUtil.copyProperties(demand, FarmerDemandPageVO.class);

            // Get batch number
            DemandCollectionBatch batch = batchMapper.selectById(demand.getBatchId());
            if (batch != null) {
                vo.setBatchNo(batch.getBatchNo());
            }

            // Set status name
            DemandStatusEnum statusEnum = DemandStatusEnum.fromCode(demand.getStatus());
            if (statusEnum != null) {
                vo.setStatusName(statusEnum.getMessage());
            }

            return vo;
        }).collect(Collectors.toList());
        voPage.setRecords(voList);

        return voPage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteFarmerDemand(String id) {
        // 1. Validate demand exists
        DemandFarmerDetail demand = this.getById(id);
        if (demand == null || demand.getIsDeleted() == 1) {
            throw new ServiceException("Demand not found");
        }

        // 2. Validate status is draft
        if (!DemandStatusEnum.DRAFT.getCode().equals(demand.getStatus())) {
            throw new ServiceException("Can only delete demand in draft status");
        }

        // 3. Validate current user is the DA who created the demand
        // TODO: Get current user ID from security context
        String currentUserId = "current_user_id";
        if (!currentUserId.equals(demand.getDaUserId())) {
            throw new ServiceException("Only the creator can delete this demand");
        }

        // 4. Update summary if demand was submitted (before deleting)
        if (DemandStatusEnum.SUBMITTED.getCode().equals(demand.getStatus())
                || DemandStatusEnum.APPROVED.getCode().equals(demand.getStatus())) {
            try {
                summaryService.updateSummaryOnDemandDelete(id);
            } catch (Exception e) {
                log.error("Failed to update summary on demand delete: {}", id, e);
                // Don't fail the whole transaction, just log the error
            }
        }

        // 5. Delete demand (logic delete)
        LambdaUpdateWrapper<DemandFarmerDetail> demandDeleteWrapper = new LambdaUpdateWrapper<>();
        demandDeleteWrapper.eq(DemandFarmerDetail::getId, id);
        demandDeleteWrapper.eq(DemandFarmerDetail::getVersion, demand.getVersion()); // Optimistic lock
        demandDeleteWrapper.set(DemandFarmerDetail::getIsDeleted, 1);
        demandDeleteWrapper.set(DemandFarmerDetail::getUpdatedBy, currentUserId);
        demandDeleteWrapper.set(DemandFarmerDetail::getUpdatedTime, new Date());
        if (!this.update(demandDeleteWrapper)) {
            throw new ServiceException("Delete failed");
        }

        // 6. Delete related input items (logic delete)
        LambdaUpdateWrapper<DemandFarmerInputItem> deleteWrapper = new LambdaUpdateWrapper<>();
        deleteWrapper.eq(DemandFarmerInputItem::getDemandId, id);
        deleteWrapper.set(DemandFarmerInputItem::getIsDeleted, 1);
        deleteWrapper.set(DemandFarmerInputItem::getUpdatedBy, currentUserId);
        deleteWrapper.set(DemandFarmerInputItem::getUpdatedTime, new Date());
        inputItemMapper.update(null, deleteWrapper);

        return true;
    }

    @Override
    public List<FarmerInputAggregationVO> getInputAggregation(DemandOrganDTO demandOrganDTO) {
        return inputItemMapper.getInputAggregation(demandOrganDTO.getSourceCode(),demandOrganDTO.getYear());
    }

    @Override
    public int submitInputAggregation(DemandOrganDTO demandOrganDTO) {
        String sourceCode = demandOrganDTO.getSourceCode();

        //根据sourceCode获取上级区划code、name
        PubRegion region = regionMapper.selectByRegionCode(sourceCode);
        String sourceName = region.getName();
        String targetCode = region.getParentCode();
        PubRegion fatherRegion = regionMapper.selectByRegionCode(targetCode);
        String targetName = fatherRegion.getName();
        demandOrganDTO.setSourceName(sourceName);
        demandOrganDTO.setTargetName(targetName);
        demandOrganDTO.setTargetCode(targetCode);


        List<FarmerInputAggregationVO> demands = inputItemMapper.getInputAggregation(sourceCode, demandOrganDTO.getYear());
        int count = 0;
        for(FarmerInputAggregationVO d:demands){
            DemandInputSummaryItemDTO dto = new DemandInputSummaryItemDTO();
            dto.setSourceCode(sourceCode);
            dto.setSourceName(sourceName);
            dto.setTargetCode(targetCode);
            dto.setTargetName(targetName);
            dto.setInputCategory(d.getInputCategory());
            dto.setInputType(d.getInputType());
            dto.setTotalQuantity(d.getTotalQuantity());
            dto.setTotalCount(d.getTotalCount());
            dto.setSummaryId(demandOrganDTO.getDemandSummaryId());
            int tempCount = demandInputSummaryItemService.addDemandInputSummaryItem(dto);
            count = count + tempCount;
        }
        return count;
    }

    /**
     * Calculate max seed quantity based on land area
     * Simplified calculation: land area * 100 kg/hectare
     */
    private BigDecimal calculateMaxSeedQuantity(BigDecimal landArea, List<?> inputItems) {
        if (landArea == null) {
            return null;
        }
        return landArea.multiply(new BigDecimal("100"));
    }

    /**
     * Calculate max fertilizer quantity based on land area
     * Simplified calculation: land area * 200 kg/hectare
     */
    private BigDecimal calculateMaxFertilizerQuantity(BigDecimal landArea, List<?> inputItems) {
        if (landArea == null) {
            return null;
        }
        return landArea.multiply(new BigDecimal("200"));
    }
}