package com.inspur.seed.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.seed.domain.entity.DetectionAuditRecord;

public interface IDetectionAuditRecordService extends IService<DetectionAuditRecord> {

    DetectionAuditRecord createSubmittedRecord(String recordType, String businessId, String batchId);

    DetectionAuditRecord createDecisionRecord(String recordType, String businessId, String batchId, String auditStatus, String auditComment);
}
