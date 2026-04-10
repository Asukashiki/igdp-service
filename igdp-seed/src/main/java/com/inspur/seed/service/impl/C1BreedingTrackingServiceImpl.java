package com.inspur.seed.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.common.utils.SecurityUtils;
import com.inspur.seed.domain.entity.C1BreedingTracking;
import com.inspur.seed.domain.entity.DetectionAuditRecord;
import com.inspur.seed.mapper.C1BreedingTrackingMapper;
import com.inspur.seed.service.IC1BreedingTrackingService;
import com.inspur.seed.service.IDetectionAuditRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class C1BreedingTrackingServiceImpl extends ServiceImpl<C1BreedingTrackingMapper, C1BreedingTracking> 
        implements IC1BreedingTrackingService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Autowired
    private IDetectionAuditRecordService detectionAuditRecordService;

    @Override
    public IPage<C1BreedingTracking> pageList(Map<String, Object> params) {
        int pageNum = params.get("pageNum") != null ? Integer.parseInt(params.get("pageNum").toString()) : 1;
        int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 10;
        
        Page<C1BreedingTracking> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<C1BreedingTracking> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(C1BreedingTracking::getDeleted, "0");
        
        if (params.get("batchId") != null && StringUtils.hasText(params.get("batchId").toString())) {
            wrapper.eq(C1BreedingTracking::getBatchId, params.get("batchId").toString());
        }
        if (params.get("stageName") != null && StringUtils.hasText(params.get("stageName").toString())) {
            wrapper.eq(C1BreedingTracking::getStageName, params.get("stageName").toString());
        }
        if (params.get("seedClass") != null && StringUtils.hasText(params.get("seedClass").toString())) {
            wrapper.eq(C1BreedingTracking::getSeedClass, params.get("seedClass").toString());
        }
        if (params.get("trackingResult") != null && StringUtils.hasText(params.get("trackingResult").toString())) {
            wrapper.eq(C1BreedingTracking::getTrackingResult, params.get("trackingResult").toString());
        }
        if (params.get("auditStatus") != null && StringUtils.hasText(params.get("auditStatus").toString())) {
            wrapper.eq(C1BreedingTracking::getAuditStatus, params.get("auditStatus").toString());
        }
        
        wrapper.orderByDesc(C1BreedingTracking::getCreatedTime);
        return this.page(page, wrapper);
    }

    @Override
    public boolean add(C1BreedingTracking entity) {
        // 根据种子级别生成不同的跟踪编号前缀
        String prefix;
        if ("Basic".equalsIgnoreCase(entity.getSeedClass())) {
            prefix = "BT"; // Breeder seed Tracking
        } else {
            prefix = "C1T"; // C1 Tracking
        }

        String trackingId = prefix + "-" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
                + "-" + String.format("%04d", (int)(Math.random() * 10000));
        entity.setTrackingId(trackingId);
        entity.setTestCount(0);
        entity.setDeleted("0");
        entity.setAuditStatus("draft");
        entity.setCreatedTime(LocalDateTime.now());
        entity.setCreatedBy(SecurityUtils.getUsername());
        return this.save(entity);
    }

    @Override
    public boolean update(C1BreedingTracking entity) {
        entity.setUpdatedTime(LocalDateTime.now());
        entity.setUpdatedBy(SecurityUtils.getUsername());
        return this.updateById(entity);
    }

    @Override
    public boolean deleteByIds(List<String> ids) {
        if (ids == null || ids.isEmpty()) return false;
        return this.update()
                .set("deleted", "1")
                .set("updated_time", LocalDateTime.now())
                .in("id", ids)
                .update();
    }

    @Override
    public C1BreedingTracking getDetailById(String id) {
        C1BreedingTracking entity = this.getById(id);
        if (entity != null && "1".equals(entity.getDeleted())) return null;
        return entity;
    }

    @Override
    public boolean submit(String id) {
        C1BreedingTracking entity = getDetailById(id);
        if (entity == null) {
            return false;
        }
        DetectionAuditRecord record = detectionAuditRecordService.createSubmittedRecord("field_detection", entity.getId(), entity.getBatchId());
        entity.setAuditStatus("submitted");
        entity.setSubmitTime(record.getSubmitTime());
        entity.setCurrentAuditId(record.getId());
        entity.setUpdatedBy(SecurityUtils.getUsername());
        entity.setUpdatedTime(LocalDateTime.now());
        return this.updateById(entity);
    }

    @Override
    public boolean approve(String id, String auditComment) {
        return audit(id, "approved", auditComment);
    }

    @Override
    public boolean reject(String id, String auditComment) {
        return audit(id, "rejected", auditComment);
    }

    @Override
    public Set<String> getApprovedBatchIdsBySeedClass(String seedClass) {
        LambdaQueryWrapper<C1BreedingTracking> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(C1BreedingTracking::getDeleted, "0")
                .eq(C1BreedingTracking::getAuditStatus, "approved")
                .isNotNull(C1BreedingTracking::getBatchId);
        if (StringUtils.hasText(seedClass)) {
            wrapper.eq(C1BreedingTracking::getSeedClass, seedClass);
        }
        List<C1BreedingTracking> records = this.list(wrapper);
        Set<String> batchIds = new LinkedHashSet<>();
        for (C1BreedingTracking record : records) {
            if (StringUtils.hasText(record.getBatchId())) {
                batchIds.add(record.getBatchId());
            }
        }
        return batchIds;
    }

    private boolean audit(String id, String auditStatus, String auditComment) {
        C1BreedingTracking entity = getDetailById(id);
        if (entity == null) {
            return false;
        }
        DetectionAuditRecord record = detectionAuditRecordService.createDecisionRecord("field_detection", entity.getId(), entity.getBatchId(), auditStatus, auditComment);
        entity.setAuditStatus(auditStatus);
        entity.setCurrentAuditId(record.getId());
        entity.setUpdatedBy(SecurityUtils.getUsername());
        entity.setUpdatedTime(LocalDateTime.now());
        return this.updateById(entity);
    }
}
