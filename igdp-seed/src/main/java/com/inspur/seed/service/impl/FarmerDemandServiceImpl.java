package com.inspur.seed.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static cn.hutool.core.util.RandomUtil.randomString;
import static com.inspur.seed.utils.FertilizerDoseMap.CROP_FERTILIZER_DOSE_MAP;

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


    private static final String STATUS_DRAFT = "0";
    private static final String STATUS_REJECTED = "3";

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String addFarmerDemand(FarmerDemandAddDTO dto) {

        // 判断该农民当年需求是否已经存在
        QueryWrapper<DemandFarmerDetail> query = new QueryWrapper<>();
        query.eq("year",dto.getYear());
        query.eq("farmer_id",dto.getFarmerId());
        List<DemandFarmerDetail> currentDetail = super.baseMapper.selectList(query);
        if(currentDetail.size()>0){
            return "1";
        }


        // 根据当前年份自动获取或创建批次
        int currentYear = java.time.Year.now().getValue();
        String batchNo = "BATCH-" + currentYear +"-"+ dto.getKebeleName() +"-" + randomString(6).toUpperCase();
        DemandCollectionBatch batch = batchService.getOrCreateBatchByYear(batchNo,currentYear);

        // 设置批次ID
        dto.setBatchId(batch.getId());


        // Create farmer demand detail
        DemandFarmerDetail detail = BeanUtil.copyProperties(dto, DemandFarmerDetail.class);
        // 直接赋值状态字符串（替代原DemandStatusEnum.DRAFT.getCode()）
        detail.setStatus(STATUS_DRAFT);
        detail.setCreatedTime(new Date());

        // TODO: Get current user ID and name from security context
        detail.setDaUserId(dto.getDaUserId());
        detail.setDaUserName(dto.getDaUserName());
        detail.setCreatedBy(dto.getDaUserName());

        // Calculate max seed and fertilizer quantities (simplified version)
        detail.setMaxSeedQuantity(calculateMaxSeedQuantity(dto.getLandArea(), dto.getInputItems()));
        detail.setMaxFertilizerQuantity(calculateMaxFertilizerQuantity(dto.getLandArea(), dto.getInputItems()));
        detail.setCurrentAuditLevel(AuditLevelEnum.VILLAGE.getCode());
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


        List<DemandFarmerInputItem> cropItems = new ArrayList<>();
        List<DemandFarmerInputItem> fertItems = new ArrayList<>();
        List<String> seasonList = new ArrayList<>();
        for (DemandFarmerInputItem inputItem : inputItems) {
            String season = inputItem.getSeason();
            if(!seasonList.contains(season)){
                seasonList.add(season);
            }
        }

        for (DemandFarmerInputItem inputItem : inputItems) {
            //判断类型
            String type = inputItem.getInputType();
            if(type.equals("IN01")){
                cropItems.add(inputItem);
                inputItemMapper.insert(inputItem);
            }else if(type.equals("IN02")){
                fertItems.add(inputItem);
            }
        }
        for(DemandFarmerInputItem fertItem : fertItems){
            for(String season : seasonList){
                List<DemandFarmerInputItem> cropSeasonItems = new ArrayList<>();
                for(DemandFarmerInputItem crop:cropItems){
                    String tmpSeason = crop.getSeason();
                    if (tmpSeason.equals(season)){
                        cropSeasonItems.add(crop);
                    }
                }
                if(fertItem.getSeason().equals(season)){
                    fertItem = setFertilizerAmount(fertItem,cropSeasonItems);
                    inputItemMapper.insert(fertItem);
                }
            }
        }

//        for (DemandFarmerInputItem inputItem : inputItems) {
//            inputItemMapper.insert(inputItem);
//        }

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
//        if (!STATUS_DRAFT.equals(demand.getStatus())
//                && !STATUS_REJECTED.equals(demand.getStatus())) {
//            throw new ServiceException("Can only update demand in draft or rejected status");
//        }
//
//        // 3. Validate current user is the DA who created the demand
//        // TODO: Get current user ID from security context
        String currentUserId = "current_user_id";
//        if (!currentUserId.equals(demand.getDaUserId())) {
//            throw new ServiceException("Only the creator can update this demand");
//        }


        // 5. Update farmer demand detail
        DemandFarmerDetail updatedDetail = BeanUtil.copyProperties(dto, DemandFarmerDetail.class);
        updatedDetail.setUpdatedTime(new Date());
        updatedDetail.setUpdatedBy(currentUserId);
        // 直接赋值状态字符串（替代原DemandStatusEnum.DRAFT.getCode()）
//        if(dto.getStatus()!=null){
//            updatedDetail.setStatus(dto.getStatus());
//        }
//        updatedDetail.setStatus(dto.getStatus()); // Reset to draft
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
    @Transactional(rollbackFor = Exception.class)
    public boolean updateAuditFarmerDemand(FarmerDemandUpdateDTO dto) {
        // 1. Validate demand exists
        DemandFarmerDetail demand = this.getById(dto.getId());
        if (demand == null || demand.getIsDeleted() == 1) {
            throw new ServiceException("Demand not found");
        }

        // 2. Validate status is draft or rejected
        // 替换原枚举判断，直接用字符串比较
//        if (!STATUS_DRAFT.equals(demand.getStatus())
//                && !STATUS_REJECTED.equals(demand.getStatus())) {
//            throw new ServiceException("Can only update demand in draft or rejected status");
//        }
//
//        // 3. Validate current user is the DA who created the demand
//        // TODO: Get current user ID from security context
        String currentUserId = "current_user_id";
//        if (!currentUserId.equals(demand.getDaUserId())) {
//            throw new ServiceException("Only the creator can update this demand");
//        }


        // 5. Update farmer demand detail
        DemandFarmerDetail updatedDetail = BeanUtil.copyProperties(dto, DemandFarmerDetail.class);
        updatedDetail.setUpdatedTime(new Date());
        updatedDetail.setUpdatedBy(currentUserId);
        // 直接赋值状态字符串（替代原DemandStatusEnum.DRAFT.getCode()）
//        if(dto.getStatus()!=null){
//            updatedDetail.setStatus(dto.getStatus());
//        }
//        updatedDetail.setStatus(dto.getStatus()); // Reset to draft
        updatedDetail.setCurrentAuditLevel(null); // Clear audit level

        // Recalculate max quantities
        updatedDetail.setMaxSeedQuantity(calculateMaxSeedQuantity(dto.getLandArea(), dto.getInputItems()));
        updatedDetail.setMaxFertilizerQuantity(calculateMaxFertilizerQuantity(dto.getLandArea(), dto.getInputItems()));

        // Use optimistic lock for update
        if (!this.updateById(updatedDetail)) {
            throw new ServiceException("Update failed, please retry");
        }

        // 6. Delete old input items and insert new ones
//        LambdaUpdateWrapper<DemandFarmerInputItem> deleteWrapper = new LambdaUpdateWrapper<>();
//        deleteWrapper.eq(DemandFarmerInputItem::getDemandId, dto.getId());
//        deleteWrapper.set(DemandFarmerInputItem::getIsDeleted, 1);
//        inputItemMapper.update(null, deleteWrapper);




        // Insert new input items
        List<DemandFarmerInputItem> inputItems = dto.getInputItems().stream().map(item -> {
            DemandFarmerInputItem inputItem = BeanUtil.copyProperties(item, DemandFarmerInputItem.class);
            String inputId = item.getId();
            BigDecimal quantity = item.getQuantity();
            LambdaUpdateWrapper<DemandFarmerInputItem> queryWrapper = new LambdaUpdateWrapper<>();
            queryWrapper.eq(DemandFarmerInputItem::getId,inputId);
            queryWrapper.set(DemandFarmerInputItem::getQuantity,quantity);
//            inputItem.setDemandId(dto.getId());
//            inputItem.setCreatedTime(new Date());
//            inputItem.setCreatedBy(currentUserId);
//            // 新增：给variety字段赋值（空字符串，避免无默认值报错）
//            inputItem.setVariety(StrUtil.blankToDefault(item.getVariety(), ""));
            inputItemMapper.update(queryWrapper);
            return inputItem;
        }).collect(Collectors.toList());


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
            itemVO.setId(item.getId());
            itemVO.setSeason(item.getSeason());
            itemVO.setCropLand(item.getCropLand());
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
            itemVO.setCropLand(item.getCropLand());
            itemVO.setSeason(item.getSeason());
            itemVO.setFertilizerAmount(item.getFertilizerAmount());
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

        // 动态排序：根据前端传入的 orderByColumn 和 isAsc 参数
        String orderColumn = StrUtil.blankToDefault(dto.getOrderByColumn(), "createdTime");
        boolean isDescOrder = !"asc".equalsIgnoreCase(dto.getIsAsc());

        // 根据排序字段选择对应的数据库列
        if ("createdTime".equals(orderColumn)) {
            if (isDescOrder) {
                wrapper.orderByDesc(DemandFarmerDetail::getCreatedTime);
            } else {
                wrapper.orderByAsc(DemandFarmerDetail::getCreatedTime);
            }
        } else if ("year".equals(orderColumn)) {
            if (isDescOrder) {
                wrapper.orderByDesc(DemandFarmerDetail::getYear);
            } else {
                wrapper.orderByAsc(DemandFarmerDetail::getYear);
            }
        } else {
            // 默认按创建时间倒序
            wrapper.orderByDesc(DemandFarmerDetail::getCreatedTime);
        }

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

    @Override
    public List<FarmerInputAggregationVO> getDemandByFarmerId(String farmerId, String year) {
        if (StrUtil.isBlank(farmerId)) {
            return new ArrayList<>();
        }

        // 1. 查询该农民的需求记录
        LambdaQueryWrapper<DemandFarmerDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DemandFarmerDetail::getFarmerId, farmerId);
        wrapper.eq(DemandFarmerDetail::getIsDeleted, 0);
        // 只查询已通过审核的需求
        wrapper.in(DemandFarmerDetail::getStatus,
            DemandStatusEnum.APPROVED.getCode(),
            DemandStatusEnum.SUBMITTED.getCode());

        // 按年度过滤
        if (StrUtil.isNotBlank(year)) {
            wrapper.eq(DemandFarmerDetail::getYear, year);
        }

        List<DemandFarmerDetail> demands = this.list(wrapper);
        if (demands.isEmpty()) {
            return new ArrayList<>();
        }

        // 2. 获取所有需求ID
        List<String> demandIds = demands.stream()
            .map(DemandFarmerDetail::getId)
            .collect(Collectors.toList());

        // 3. 查询所有投入品明细
        LambdaQueryWrapper<DemandFarmerInputItem> itemWrapper = new LambdaQueryWrapper<>();
        itemWrapper.in(DemandFarmerInputItem::getDemandId, demandIds);
        itemWrapper.eq(DemandFarmerInputItem::getIsDeleted, 0);
        List<DemandFarmerInputItem> inputItems = inputItemMapper.selectList(itemWrapper);

        // 4. 按 inputType 和 inputCategory 汇总
        return inputItems.stream()
            .collect(Collectors.groupingBy(
                item -> item.getInputType() + "|" + item.getInputCategory(),
                Collectors.reducing(
                    BigDecimal.ZERO,
                    DemandFarmerInputItem::getQuantity,
                    BigDecimal::add
                )
            ))
            .entrySet().stream()
            .map(entry -> {
                String[] keys = entry.getKey().split("\\|");
                FarmerInputAggregationVO vo = new FarmerInputAggregationVO();
                vo.setInputType(keys[0]);
                vo.setInputCategory(keys.length > 1 ? keys[1] : "");
                vo.setTotalQuantity(entry.getValue());
                vo.setTotalCount(1); // 简化处理
                return vo;
            })
            .collect(Collectors.toList());
    }



    /**
     * 计算指定肥料的最大允许量
     * @param fertilizerType 肥料类型（如：尿素、NPS）
     * @param cropProducts 已存在的作物类投入品列表（玉米、小麦、苔麸）
     * @return 肥料的最大允许量（公斤）
     */
    public static Double calculateMaxFertilizerAmount(String fertilizerType, List<DemandFarmerInputItem> cropProducts) {
        // 空值校验
        if (fertilizerType == null) {
            throw new IllegalArgumentException("肥料类型不能为空");
        }
        if (cropProducts == null || cropProducts.isEmpty()) {
            return 0.0;
        }

        BigDecimal total = BigDecimal.ZERO;

        // 遍历所有作物投入品，累加计算
        for (DemandFarmerInputItem cropProduct : cropProducts) {
            String cropType = cropProduct.getInputCategory();
            BigDecimal cropLand = cropProduct.getCropLand();

            // 跳过无效的作物数据（类型不存在/面积为空/面积<=0）
            if (cropType == null
                    || cropLand == null) {
                continue;
            }

            // 获取该作物对应肥料的特定施肥量
            Integer dose = Optional.ofNullable(CROP_FERTILIZER_DOSE_MAP.get(cropType))
                    .map(fertilizerMap -> fertilizerMap.get(fertilizerType))
                    .orElse(0);

            // 累加：面积 × 特定施肥量
            BigDecimal contribution = cropLand.multiply(BigDecimal.valueOf(dose));
            total = total.add(contribution);
        }

        return  total != null ? total.doubleValue() : 0.0;

    }

    /**
     * 为肥料类投入品设置最大允许量（直接修改传入的对象）
     * @param fertilizerProduct 肥料类投入品
     * @param cropProducts 已存在的作物类投入品列表
     */
    public static DemandFarmerInputItem setFertilizerAmount(DemandFarmerInputItem fertilizerProduct, List<DemandFarmerInputItem> cropProducts) {
        String fertilizerType = fertilizerProduct.getInputCategory();
        Double maxAmount = calculateMaxFertilizerAmount(fertilizerType, cropProducts);
        fertilizerProduct.setFertilizerAmount(maxAmount);
        return fertilizerProduct;
    }
}
