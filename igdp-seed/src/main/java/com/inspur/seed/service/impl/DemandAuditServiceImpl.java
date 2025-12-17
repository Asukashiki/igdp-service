package com.inspur.seed.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.inspur.common.exception.ServiceException;
import com.inspur.seed.constant.*;
import com.inspur.seed.domain.dto.*;
import com.inspur.seed.domain.entity.DemandAuditRecord;
import com.inspur.seed.domain.entity.DemandCollectionBatch;
import com.inspur.seed.domain.entity.DemandFarmerDetail;
import com.inspur.seed.domain.vo.DemandAuditResultVO;
import com.inspur.seed.domain.vo.DemandPendingPageVO;
import com.inspur.seed.mapper.DemandAuditRecordMapper;
import com.inspur.seed.mapper.DemandCollectionBatchMapper;
import com.inspur.seed.mapper.DemandFarmerDetailMapper;
import com.inspur.seed.service.IDemandAuditService;
// import com.inspur.seed.service.IDemandCategorySummaryService;
import com.inspur.seed.service.IDemandSummaryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Demand Audit Service Implementation
 *
 * @author igdp
 * @date 2025-12-04
 */
@Slf4j
@Service
public class DemandAuditServiceImpl implements IDemandAuditService {

    @Autowired
    private DemandFarmerDetailMapper demandDetailMapper;

    @Autowired
    private DemandCollectionBatchMapper batchMapper;

    @Autowired
    private DemandAuditRecordMapper auditRecordMapper;

    @Autowired
    private IDemandSummaryService summaryService;

    // @Autowired
    // private IDemandCategorySummaryService categorySummaryService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DemandAuditResultVO submitForAudit(DemandAuditSubmitDTO dto) {
        int successCount = 0;
        int failCount = 0;

        // TODO: Get current user info from security context
        String currentUserId = "current_user_id";
        String currentUserName = "current_user_name";

        for (String demandId : dto.getIds()) {
            try {
                // 1. Validate demand exists and status is draft
                DemandFarmerDetail demand = demandDetailMapper.selectById(demandId);
                if (demand == null || demand.getIsDeleted() == 1) {
                    log.warn("Demand not found: {}", demandId);
                    failCount++;
                    continue;
                }

                if (!DemandStatusEnum.DRAFT.getCode().equals(demand.getStatus())
                        && !DemandStatusEnum.REJECTED.getCode().equals(demand.getStatus())) {
                    log.warn("Demand is not in draft or rejected status: {}", demandId);
                    failCount++;
                    continue;
                }


                // 2. Validate current user is the DA who created the demand
//                if (!currentUserId.equals(demand.getDaUserId())) {
//                    log.warn("User is not the creator of demand: {}", demandId);
//                    failCount++;
//                    continue;
//                }

                // 3. Update demand status to submitted and set audit level to village
                LambdaUpdateWrapper<DemandFarmerDetail> updateWrapper = new LambdaUpdateWrapper<>();
                updateWrapper.eq(DemandFarmerDetail::getId, demandId);
                updateWrapper.set(DemandFarmerDetail::getStatus, DemandStatusEnum.SUBMITTED.getCode());
                updateWrapper.set(DemandFarmerDetail::getCurrentAuditLevel, AuditLevelEnum.VILLAGE.getCode());
                updateWrapper.set(DemandFarmerDetail::getSubmitTime, new Date());
                updateWrapper.set(DemandFarmerDetail::getUpdatedBy, currentUserId);
                updateWrapper.set(DemandFarmerDetail::getUpdatedTime, new Date());

                demandDetailMapper.update(null, updateWrapper);

                // 4. Create audit record
                DemandAuditRecord auditRecord = new DemandAuditRecord();
                auditRecord.setBatchId(demand.getBatchId());

                auditRecord.setDemandId(demandId);
                auditRecord.setAuditType("single");
                auditRecord.setAuditLevel(AuditLevelEnum.VILLAGE.getCode());
                auditRecord.setAdminCode(demand.getKebele()); // Use kebele as admin code
                auditRecord.setAdminName(demand.getKebele());
                auditRecord.setAuditUserId(currentUserId);
                auditRecord.setAuditUserName(currentUserName);
                auditRecord.setAuditTime(new Date());
                auditRecord.setAuditAction(AuditActionEnum.SUBMIT.getCode());
                auditRecord.setAuditResult(AuditResultEnum.PASSED.getCode());
                auditRecord.setCreatedBy(currentUserId);
                auditRecord.setCreatedTime(new Date());

                auditRecordMapper.insert(auditRecord);

                // 5. Generate summary data for this demand
                try {
                    summaryService.generateSummaryForDemand(demandId);
                } catch (Exception e) {
                    log.error("Failed to generate summary for demand: {}", demandId, e);
                    // Don't fail the whole transaction, just log the error
                }

                // 6. Update batch status to reviewing if this is the first submission
                updateBatchStatusToReviewing(demand.getBatchId());

                successCount++;
            } catch (Exception e) {
                log.error("Failed to submit demand: {}", demandId, e);
                failCount++;
            }
        }

        DemandAuditResultVO result = new DemandAuditResultVO();
        result.setSuccessCount(successCount);
        result.setFailCount(failCount);
        return result;
    }

    @Override
    public Page<DemandPendingPageVO> getPendingAuditPage(DemandAuditPendingPageDTO dto) {
        // TODO: Get current user's audit level and administrative division from security context
        // Temporarily disable user-level filtering for testing and development
        // String currentAuditLevel = "village"; // Should be determined by user role
        // String currentAdminCode = "kebele_code"; // Should be from user's admin division

        // 1. Build query wrapper
        LambdaQueryWrapper<DemandFarmerDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DemandFarmerDetail::getIsDeleted, 0);
        wrapper.eq(DemandFarmerDetail::getStatus, DemandStatusEnum.SUBMITTED.getCode());
        // Temporarily disable audit level filtering - show all submitted demands regardless of audit level
        // wrapper.eq(DemandFarmerDetail::getCurrentAuditLevel, currentAuditLevel);

        // Filter by user's administrative division
        // This should be more sophisticated based on audit level
        // For example, village level checks kebele, town level checks woreda, etc.
        // Temporarily disabled to show all submitted demands
        // if (AuditLevelEnum.VILLAGE.getCode().equals(currentAuditLevel)) {
        //     wrapper.eq(DemandFarmerDetail::getKebele, currentAdminCode);
        // } else if (AuditLevelEnum.TOWN.getCode().equals(currentAuditLevel)) {
        //     wrapper.eq(DemandFarmerDetail::getWoreda, currentAdminCode);
        // } else if (AuditLevelEnum.DISTRICT.getCode().equals(currentAuditLevel)) {
        //     wrapper.eq(DemandFarmerDetail::getZone, currentAdminCode);
        // } else if (AuditLevelEnum.STATE.getCode().equals(currentAuditLevel)) {
        //     wrapper.eq(DemandFarmerDetail::getRegion, currentAdminCode);
        // }

        // Filter by batch ID
        if (StrUtil.isNotBlank(dto.getBatchId())) {
            wrapper.eq(DemandFarmerDetail::getBatchId, dto.getBatchId());
        }

        // Filter by farmer name (fuzzy)
        if (StrUtil.isNotBlank(dto.getFarmerName())) {
            wrapper.like(DemandFarmerDetail::getFarmerName, dto.getFarmerName());
        }

        // Filter by administrative divisions
        if (StrUtil.isNotBlank(dto.getKebele())) {
            wrapper.eq(DemandFarmerDetail::getKebele, dto.getKebele());
        }
        if (StrUtil.isNotBlank(dto.getWoreda())) {
            wrapper.eq(DemandFarmerDetail::getWoreda, dto.getWoreda());
        }
        if (StrUtil.isNotBlank(dto.getVillage())) {
            wrapper.eq(DemandFarmerDetail::getVillage, dto.getVillage());
        }

        // Filter by year
        if (StrUtil.isNotBlank(dto.getYear())) {
            wrapper.eq(DemandFarmerDetail::getYear, dto.getYear());
        }

        // Order by submit time desc
        wrapper.orderByDesc(DemandFarmerDetail::getSubmitTime);

        // 2. Query page
        Page<DemandFarmerDetail> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        Page<DemandFarmerDetail> resultPage = demandDetailMapper.selectPage(page, wrapper);
        // 3. Convert to VO
        Page<DemandPendingPageVO> voPage = new Page<>(resultPage.getCurrent(), resultPage.getSize(), resultPage.getTotal());
        List<DemandPendingPageVO> voList = resultPage.getRecords().stream().map(demand -> {
            DemandPendingPageVO vo = BeanUtil.copyProperties(demand, DemandPendingPageVO.class);

            // Get batch number
            DemandCollectionBatch batch = batchMapper.selectById(demand.getBatchId());
            if (batch != null) {
                vo.setBatchNo(batch.getBatchNo());
            }

            return vo;
        }).collect(Collectors.toList());
        voPage.setRecords(voList);

        return voPage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DemandAuditResultVO approveDemands(DemandAuditApproveDTO dto) {
        int successCount = 0;
        int failCount = 0;

        // TODO: Get current user info from security context
        String currentUserId = "current_user_id";
        String currentUserName = "current_user_name";
        // Temporarily disable user audit level check - use demand's current audit level instead
        // String currentAuditLevel = "village"; // Should be from user role

        for (String demandId : dto.getIds()) {
            try {
                // 1. Validate demand exists and status is submitted
                DemandFarmerDetail demand = demandDetailMapper.selectById(demandId);
                if (demand == null || demand.getIsDeleted() == 1) {
                    log.warn("Demand not found: {}", demandId);
                    failCount++;
                    continue;
                }

                if (!DemandStatusEnum.SUBMITTED.getCode().equals(demand.getStatus())) {
                    log.warn("Demand is not in submitted status: {}", demandId);
                    failCount++;
                    continue;
                }

                // 2. Get demand's current audit level (for audit record only)
                String demandAuditLevel = demand.getCurrentAuditLevel();
                if (demandAuditLevel == null) {
                    log.warn("Demand has no current audit level: {}", demandId);
                    failCount++;
                    continue;
                }

                // 3. Directly approve demand (no multi-level audit flow)
                // 4. Update demand
                LambdaUpdateWrapper<DemandFarmerDetail> updateWrapper = new LambdaUpdateWrapper<>();
                updateWrapper.eq(DemandFarmerDetail::getId, demandId);
                updateWrapper.set(DemandFarmerDetail::getStatus, DemandStatusEnum.APPROVED.getCode());
                updateWrapper.set(DemandFarmerDetail::getCurrentAuditLevel, null);

                updateWrapper.set(DemandFarmerDetail::getUpdatedBy, currentUserId);
                updateWrapper.set(DemandFarmerDetail::getUpdatedTime, new Date());

                demandDetailMapper.update(null, updateWrapper);

                // 5. Update category summary for approved demand
                // categorySummaryService.updateOnDemandApproved(demandId);

                // 6. Create audit record
                DemandAuditRecord auditRecord = new DemandAuditRecord();
                auditRecord.setBatchId(demand.getBatchId());

                auditRecord.setDemandId(demandId);
                auditRecord.setAuditType("single");
                auditRecord.setAuditLevel(demandAuditLevel);
                auditRecord.setAdminCode(getAdminCodeByLevel(demand, demandAuditLevel));
                auditRecord.setAdminName(getAdminNameByLevel(demand, demandAuditLevel));
                auditRecord.setAuditUserId(currentUserId);
                auditRecord.setAuditUserName(currentUserName);
                auditRecord.setAuditTime(new Date());
                auditRecord.setAuditAction(AuditActionEnum.APPROVE.getCode());
                auditRecord.setAuditResult(AuditResultEnum.PASSED.getCode());
                auditRecord.setRemark(dto.getRemark());
                auditRecord.setCreatedBy(currentUserId);
                auditRecord.setCreatedTime(new Date());

                auditRecordMapper.insert(auditRecord);

                successCount++;
            } catch (Exception e) {
                log.error("Failed to approve demand: {}", demandId, e);
                failCount++;
            }
        }

        DemandAuditResultVO result = new DemandAuditResultVO();
        result.setSuccessCount(successCount);
        result.setFailCount(failCount);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DemandAuditResultVO rejectDemands(DemandAuditRejectDTO dto) {
        int successCount = 0;
        int failCount = 0;

        // TODO: Get current user info from security context
        String currentUserId = "current_user_id";
        String currentUserName = "current_user_name";
        // Temporarily disable user audit level check - use demand's current audit level instead
        // String currentAuditLevel = "village"; // Should be from user role

        for (String demandId : dto.getIds()) {
            try {
                // 1. Validate demand exists and status is submitted
                DemandFarmerDetail demand = demandDetailMapper.selectById(demandId);
                if (demand == null || demand.getIsDeleted() == 1) {
                    log.warn("Demand not found: {}", demandId);
                    failCount++;
                    continue;
                }

//                if (!DemandStatusEnum.SUBMITTED.getCode().equals(demand.getStatus())) {
//                    log.warn("Demand is not in submitted status: {}", demandId);
//                    failCount++;
//                    continue;
//                }

                // 2. Get demand's current audit level for audit record
                String demandAuditLevel = demand.getCurrentAuditLevel();
                if (demandAuditLevel == null) {
                    log.warn("Demand has no current audit level: {}", demandId);
                    // Still allow rejection even if no audit level
                    demandAuditLevel = "unknown";
                }

                // 3. Update demand status to rejected and clear audit level
                LambdaUpdateWrapper<DemandFarmerDetail> updateWrapper = new LambdaUpdateWrapper<>();
                updateWrapper.eq(DemandFarmerDetail::getId, demandId);
                updateWrapper.set(DemandFarmerDetail::getStatus, DemandStatusEnum.REJECTED.getCode());
                updateWrapper.set(DemandFarmerDetail::getCurrentAuditLevel, null);
                updateWrapper.set(DemandFarmerDetail::getUpdatedBy, currentUserId);
                updateWrapper.set(DemandFarmerDetail::getUpdatedTime, new Date());

                demandDetailMapper.update(null, updateWrapper);

                // 4. Create audit record
                DemandAuditRecord auditRecord = new DemandAuditRecord();
                auditRecord.setBatchId(demand.getBatchId());

                auditRecord.setDemandId(demandId);
                auditRecord.setAuditType("single");
                auditRecord.setAuditLevel(demandAuditLevel);
                auditRecord.setAdminCode(getAdminCodeByLevel(demand, demandAuditLevel));
                auditRecord.setAdminName(getAdminNameByLevel(demand, demandAuditLevel));
                auditRecord.setAuditUserId(currentUserId);
                auditRecord.setAuditUserName(currentUserName);
                auditRecord.setAuditTime(new Date());
                auditRecord.setAuditAction(AuditActionEnum.REJECT.getCode());
                auditRecord.setAuditResult(AuditResultEnum.REJECTED.getCode());
                auditRecord.setAuditOpinion(dto.getAuditOpinion());
                auditRecord.setRemark(dto.getRemark());
                auditRecord.setCreatedBy(currentUserId);
                auditRecord.setCreatedTime(new Date());

                auditRecordMapper.insert(auditRecord);

                successCount++;
            } catch (Exception e) {
                log.error("Failed to reject demand: {}", demandId, e);
                failCount++;
            }
        }

        DemandAuditResultVO result = new DemandAuditResultVO();
        result.setSuccessCount(successCount);
        result.setFailCount(failCount);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean lockBatchDemands(DemandLockDTO dto) {
        // TODO: Validate current user is Ministry level
        String currentUserId = "current_user_id";

        // 1. Validate batch exists and status is reviewing
        DemandCollectionBatch batch = batchMapper.selectById(dto.getBatchId());
        if (batch == null || batch.getIsDeleted() == 1) {
            throw new ServiceException("Batch not found");
        }

        if (!BatchStatusEnum.REVIEWING.getCode().equals(batch.getStatus())) {
            throw new ServiceException("Batch is not in reviewing status");
        }

        // 2. Validate all demands in batch are approved
        LambdaQueryWrapper<DemandFarmerDetail> demandWrapper = new LambdaQueryWrapper<>();
        demandWrapper.eq(DemandFarmerDetail::getBatchId, dto.getBatchId());
        demandWrapper.eq(DemandFarmerDetail::getIsDeleted, 0);
        demandWrapper.ne(DemandFarmerDetail::getStatus, DemandStatusEnum.APPROVED.getCode());

        long unapprovedCount = demandDetailMapper.selectCount(demandWrapper);
        if (unapprovedCount > 0) {
            throw new ServiceException("Not all demands in batch are approved");
        }

        // 3. Update batch status to locked (use optimistic lock)
        batch.setStatus(BatchStatusEnum.LOCKED.getCode());
        batch.setUpdatedBy(currentUserId);
        batch.setUpdatedTime(new Date());

        if (batchMapper.updateById(batch) == 0) {
            throw new ServiceException("Failed to lock batch, please retry");
        }

        // 4. Update all approved demands to locked status
        LambdaUpdateWrapper<DemandFarmerDetail> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(DemandFarmerDetail::getBatchId, dto.getBatchId());
        updateWrapper.eq(DemandFarmerDetail::getStatus, DemandStatusEnum.APPROVED.getCode());
        updateWrapper.set(DemandFarmerDetail::getStatus, DemandStatusEnum.LOCKED.getCode());
        updateWrapper.set(DemandFarmerDetail::getUpdatedBy, currentUserId);
        updateWrapper.set(DemandFarmerDetail::getUpdatedTime, new Date());

        demandDetailMapper.update(null, updateWrapper);

        return true;
    }

    /**
     * Get admin code by audit level
     */
    private String getAdminCodeByLevel(DemandFarmerDetail demand, String auditLevel) {
        if (AuditLevelEnum.VILLAGE.getCode().equals(auditLevel)) {
            return demand.getKebele();
        } else if (AuditLevelEnum.TOWN.getCode().equals(auditLevel)) {
            return demand.getWoreda();
        } else if (AuditLevelEnum.DISTRICT.getCode().equals(auditLevel)) {
            return demand.getZone();
        } else if (AuditLevelEnum.STATE.getCode().equals(auditLevel)) {
            return demand.getRegion();
        } else {
            return "MINISTRY";
        }
    }

    /**
     * Get admin name by audit level
     */
    private String getAdminNameByLevel(DemandFarmerDetail demand, String auditLevel) {
        return getAdminCodeByLevel(demand, auditLevel);
    }

    /**
     * Update batch status to reviewing if still in collecting status
     */
    private void updateBatchStatusToReviewing(String batchId) {
        DemandCollectionBatch batch = batchMapper.selectById(batchId);
        if (batch != null && BatchStatusEnum.COLLECTING.getCode().equals(batch.getStatus())) {
            batch.setStatus(BatchStatusEnum.REVIEWING.getCode());
            batch.setUpdatedTime(new Date());
            batchMapper.updateById(batch);
            log.info("Batch status updated to reviewing: {}", batchId);
        }
    }

    @Override
    public Page<DemandPendingPageVO> getApprovedAuditPage(DemandAuditPendingPageDTO dto) {
        // 1. Build query wrapper for approved demands
        LambdaQueryWrapper<DemandFarmerDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DemandFarmerDetail::getIsDeleted, 0);
        wrapper.eq(DemandFarmerDetail::getStatus, DemandStatusEnum.APPROVED.getCode());

        // Filter by batch ID
        if (StrUtil.isNotBlank(dto.getBatchId())) {
            wrapper.eq(DemandFarmerDetail::getBatchId, dto.getBatchId());
        }

        // Filter by farmer name (fuzzy)
        if (StrUtil.isNotBlank(dto.getFarmerName())) {
            wrapper.like(DemandFarmerDetail::getFarmerName, dto.getFarmerName());
        }

        // Filter by administrative divisions
        if (StrUtil.isNotBlank(dto.getKebele())) {
            wrapper.eq(DemandFarmerDetail::getKebele, dto.getKebele());
        }
        if (StrUtil.isNotBlank(dto.getWoreda())) {
            wrapper.eq(DemandFarmerDetail::getWoreda, dto.getWoreda());
        }
        if (StrUtil.isNotBlank(dto.getVillage())) {
            wrapper.eq(DemandFarmerDetail::getVillage, dto.getVillage());
        }

        // Filter by year
        if (StrUtil.isNotBlank(dto.getYear())) {
            wrapper.eq(DemandFarmerDetail::getYear, dto.getYear());
        }

        // Order by updated time desc (when approved)
        wrapper.orderByDesc(DemandFarmerDetail::getUpdatedTime);

        // 2. Query page
        Page<DemandFarmerDetail> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        Page<DemandFarmerDetail> resultPage = demandDetailMapper.selectPage(page, wrapper);

        // 3. Convert to VO
        Page<DemandPendingPageVO> voPage = new Page<>(resultPage.getCurrent(), resultPage.getSize(), resultPage.getTotal());
        List<DemandPendingPageVO> voList = resultPage.getRecords().stream().map(demand -> {
            DemandPendingPageVO vo = BeanUtil.copyProperties(demand, DemandPendingPageVO.class);

            // Get batch number
            DemandCollectionBatch batch = batchMapper.selectById(demand.getBatchId());
            if (batch != null) {
                vo.setBatchNo(batch.getBatchNo());
            }

            return vo;
        }).collect(Collectors.toList());
        voPage.setRecords(voList);

        return voPage;
    }


    public Page<DemandPendingPageVO> getAuditPage(DemandAuditPendingPageDTO dto) {
        // TODO: Get current user's audit level and administrative division from security context
        // Temporarily disable user-level filtering for testing and development
        // String currentAuditLevel = "village"; // Should be determined by user role
        // String currentAdminCode = "kebele_code"; // Should be from user's admin division

        // 1. Build query wrapper
        LambdaQueryWrapper<DemandFarmerDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DemandFarmerDetail::getIsDeleted, 0);
        wrapper.ne(DemandFarmerDetail::getStatus, DemandStatusEnum.DRAFT.getCode());
        // Temporarily disable audit level filtering - show all submitted demands regardless of audit level
        // wrapper.eq(DemandFarmerDetail::getCurrentAuditLevel, currentAuditLevel);

        // Filter by user's administrative division
        // This should be more sophisticated based on audit level
        // For example, village level checks kebele, town level checks woreda, etc.
        // Temporarily disabled to show all submitted demands
        // if (AuditLevelEnum.VILLAGE.getCode().equals(currentAuditLevel)) {
        //     wrapper.eq(DemandFarmerDetail::getKebele, currentAdminCode);
        // } else if (AuditLevelEnum.TOWN.getCode().equals(currentAuditLevel)) {
        //     wrapper.eq(DemandFarmerDetail::getWoreda, currentAdminCode);
        // } else if (AuditLevelEnum.DISTRICT.getCode().equals(currentAuditLevel)) {
        //     wrapper.eq(DemandFarmerDetail::getZone, currentAdminCode);
        // } else if (AuditLevelEnum.STATE.getCode().equals(currentAuditLevel)) {
        //     wrapper.eq(DemandFarmerDetail::getRegion, currentAdminCode);
        // }

        // Filter by batch ID
        if (StrUtil.isNotBlank(dto.getBatchId())) {
            wrapper.eq(DemandFarmerDetail::getBatchId, dto.getBatchId());
        }

        // Filter by farmer name (fuzzy)
        if (StrUtil.isNotBlank(dto.getFarmerName())) {
            wrapper.like(DemandFarmerDetail::getFarmerName, dto.getFarmerName());
        }

        // Filter by administrative divisions
        if (StrUtil.isNotBlank(dto.getKebele())) {
            wrapper.eq(DemandFarmerDetail::getKebele, dto.getKebele());
        }
        if (StrUtil.isNotBlank(dto.getWoreda())) {
            wrapper.eq(DemandFarmerDetail::getWoreda, dto.getWoreda());
        }
        if (StrUtil.isNotBlank(dto.getVillage())) {
            wrapper.eq(DemandFarmerDetail::getVillage, dto.getVillage());
        }

        // Filter by year
        if (StrUtil.isNotBlank(dto.getYear())) {
            wrapper.eq(DemandFarmerDetail::getYear, dto.getYear());
        }

        // Order by submit time desc
        wrapper.orderByDesc(DemandFarmerDetail::getSubmitTime);

        // 2. Query page
        Page<DemandFarmerDetail> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        Page<DemandFarmerDetail> resultPage = demandDetailMapper.selectPage(page, wrapper);
        // 3. Convert to VO
        Page<DemandPendingPageVO> voPage = new Page<>(resultPage.getCurrent(), resultPage.getSize(), resultPage.getTotal());
        List<DemandPendingPageVO> voList = resultPage.getRecords().stream().map(demand -> {
            DemandPendingPageVO vo = BeanUtil.copyProperties(demand, DemandPendingPageVO.class);

            // Get batch number
            DemandCollectionBatch batch = batchMapper.selectById(demand.getBatchId());
            if (batch != null) {
                vo.setBatchNo(batch.getBatchNo());
            }

            return vo;
        }).collect(Collectors.toList());
        voPage.setRecords(voList);

        return voPage;
    }
}
