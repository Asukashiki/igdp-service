package com.inspur.seed.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.common.utils.SecurityUtils;
import com.inspur.seed.domain.entity.DetectionAuditRecord;
import com.inspur.seed.mapper.DetectionAuditRecordMapper;
import com.inspur.seed.service.IDetectionAuditRecordService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class DetectionAuditRecordServiceImpl extends ServiceImpl<DetectionAuditRecordMapper, DetectionAuditRecord>
        implements IDetectionAuditRecordService {

    @Override
    public DetectionAuditRecord createSubmittedRecord(String recordType, String businessId, String batchId) {
        DetectionAuditRecord record = buildBaseRecord(recordType, businessId, batchId);
        String username = SecurityUtils.getUsername();
        LocalDateTime now = LocalDateTime.now();
        record.setAuditStatus("submitted");
        record.setSubmitter(username);
        record.setSubmitTime(now);
        record.setCreatedBy(username);
        record.setCreatedTime(now);
        save(record);
        return record;
    }

    @Override
    public DetectionAuditRecord createDecisionRecord(String recordType, String businessId, String batchId, String auditStatus, String auditComment) {
        DetectionAuditRecord record = buildBaseRecord(recordType, businessId, batchId);
        String username = SecurityUtils.getUsername();
        LocalDateTime now = LocalDateTime.now();
        record.setAuditStatus(auditStatus);
        record.setAuditor(username);
        record.setAuditTime(now);
        record.setAuditComment(auditComment);
        record.setCreatedBy(username);
        record.setCreatedTime(now);
        save(record);
        return record;
    }

    private DetectionAuditRecord buildBaseRecord(String recordType, String businessId, String batchId) {
        DetectionAuditRecord record = new DetectionAuditRecord();
        record.setRecordType(recordType);
        record.setBusinessId(businessId);
        record.setBatchId(batchId);
        record.setDeleted("0");
        return record;
    }
}
