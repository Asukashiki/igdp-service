package com.inspur.seed.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.inspur.seed.breeding.breederSeed.domain.vo.BreedSeedProduceResultVO;
import com.inspur.seed.breeding.breederSeed.mapper.BreedSeedProduceResultMapper;
import com.inspur.seed.domain.entity.C1BreedingTest;
import com.inspur.seed.domain.entity.C1BreedingTracking;
import com.inspur.seed.domain.entity.DetectionCertificate;
import com.inspur.seed.mapper.C1BreedingTestMapper;
import com.inspur.seed.mapper.C1BreedingTrackingMapper;
import com.inspur.seed.mapper.DetectionCertificateMapper;
import com.inspur.seed.multiplication.c1Seed.domain.entity.C1BreedingBatch;
import com.inspur.seed.multiplication.c1Seed.mapper.C1BreedingBatchMapper;
import com.inspur.seed.service.IDetectionCertificateService;
import com.inspur.seed.service.basic.IBasicSeedProduceResultService;
import com.inspur.seed.service.prebasic.IPrebasicSeedProduceResultService;
import com.inspur.seed.vo.basic.BasicSeedProduceResultVO;
import com.inspur.seed.vo.prebasic.PrebasicSeedProduceResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class DetectionCertificateServiceImpl implements IDetectionCertificateService {

    @Autowired
    private DetectionCertificateMapper detectionCertificateMapper;

    @Autowired
    private C1BreedingTrackingMapper c1BreedingTrackingMapper;

    @Autowired
    private C1BreedingTestMapper c1BreedingTestMapper;

    @Autowired
    private C1BreedingBatchMapper c1BreedingBatchMapper;

    @Autowired
    private BreedSeedProduceResultMapper breedSeedProduceResultMapper;

    @Autowired
    private IPrebasicSeedProduceResultService prebasicSeedProduceResultService;

    @Autowired
    private IBasicSeedProduceResultService basicSeedProduceResultService;

    @Override
    public void syncApprovedCertificates() {
        List<C1BreedingTracking> trackingList = c1BreedingTrackingMapper.selectList(
                new LambdaQueryWrapper<C1BreedingTracking>()
                        .eq(C1BreedingTracking::getDeleted, "0")
                        .eq(C1BreedingTracking::getAuditStatus, "approved")
                        .isNotNull(C1BreedingTracking::getBatchId)
        );
        List<C1BreedingTest> testList = c1BreedingTestMapper.selectList(
                new LambdaQueryWrapper<C1BreedingTest>()
                        .eq(C1BreedingTest::getDeleted, "0")
                        .eq(C1BreedingTest::getAuditStatus, "approved")
                        .isNotNull(C1BreedingTest::getBatchId)
        );

        Set<String> pairSet = new LinkedHashSet<>();
        for (C1BreedingTracking item : trackingList) {
            if (!StringUtils.hasText(item.getBatchId())) {
                continue;
            }
            String seedClass = normalizeSegment(item.getSeedClass(), "UNKNOWN");
            pairSet.add(item.getBatchId() + "|" + seedClass);
        }
        for (C1BreedingTest item : testList) {
            if (!StringUtils.hasText(item.getBatchId())) {
                continue;
            }
            String seedClass = normalizeSegment(item.getSeedClass(), "UNKNOWN");
            pairSet.add(item.getBatchId() + "|" + seedClass);
        }

        for (String pair : pairSet) {
            String[] parts = pair.split("\\|", 2);
            if (parts.length < 2) {
                continue;
            }
            String batchId = parts[0];
            String seedClass = parts[1];
            String cropType = normalizeSegment(resolveCropTypeByBatchId(batchId), "UNKNOWN");
            String certificateId = buildCertificateRuleId(batchId, seedClass, cropType);

            LambdaQueryWrapper<DetectionCertificate> query = new LambdaQueryWrapper<DetectionCertificate>()
                    .eq(DetectionCertificate::getBatchId, batchId)
                    .eq(DetectionCertificate::getSeedClass, seedClass)
                    .orderByDesc(DetectionCertificate::getUpdatedTime)
                    .orderByDesc(DetectionCertificate::getId)
                    .last("limit 1");
            DetectionCertificate existing = detectionCertificateMapper.selectOne(query);

            if (existing == null) {
                DetectionCertificate entity = new DetectionCertificate();
                entity.setCertificateId(certificateId);
                entity.setBatchId(batchId);
                entity.setSeedClass(seedClass);
                entity.setCropType(cropType);
                entity.setAuditStatus("approved");
                entity.setSourceType("detection");
                entity.setCreatedBy("system");
                entity.setCreatedTime(LocalDateTime.now());
                entity.setUpdatedBy("system");
                entity.setUpdatedTime(LocalDateTime.now());
                detectionCertificateMapper.insert(entity);
            } else {
                existing.setCertificateId(certificateId);
                existing.setCropType(cropType);
                existing.setAuditStatus("approved");
                existing.setSourceType("detection");
                existing.setUpdatedBy("system");
                existing.setUpdatedTime(LocalDateTime.now());
                detectionCertificateMapper.updateById(existing);
            }
        }
    }

    @Override
    public Map<String, String> getCertificateIdMap(List<String> batchIds) {
        syncApprovedCertificates();

        List<DetectionCertificate> list;
        LambdaQueryWrapper<DetectionCertificate> wrapper = new LambdaQueryWrapper<DetectionCertificate>()
                .orderByDesc(DetectionCertificate::getUpdatedTime)
                .orderByDesc(DetectionCertificate::getId);
        if (batchIds != null && !batchIds.isEmpty()) {
            wrapper.in(DetectionCertificate::getBatchId, batchIds);
        }
        list = detectionCertificateMapper.selectList(wrapper);

        Map<String, String> map = new LinkedHashMap<>();
        for (DetectionCertificate item : list) {
            if (!StringUtils.hasText(item.getBatchId())) {
                continue;
            }
            // 兼容旧接口返回格式：每个 batchId 保留一条证书号
            if (!map.containsKey(item.getBatchId())) {
                map.put(item.getBatchId(), item.getCertificateId());
            }
        }
        if (batchIds != null && !batchIds.isEmpty()) {
            Map<String, String> ordered = new LinkedHashMap<>();
            for (String batchId : batchIds) {
                if (!StringUtils.hasText(batchId)) {
                    continue;
                }
                ordered.put(batchId, map.getOrDefault(batchId, ""));
            }
            return ordered;
        }
        return map;
    }

    @Override
    public String getCertificateId(String batchId, String seedClass, String cropType) {
        if (!StringUtils.hasText(batchId)) {
            return "";
        }
        syncApprovedCertificates();

        List<LambdaQueryWrapper<DetectionCertificate>> candidates = new ArrayList<>();

        if (StringUtils.hasText(seedClass) && StringUtils.hasText(cropType)) {
            candidates.add(new LambdaQueryWrapper<DetectionCertificate>()
                    .eq(DetectionCertificate::getBatchId, batchId)
                    .eq(DetectionCertificate::getSeedClass, normalizeSegment(seedClass, "UNKNOWN"))
                    .eq(DetectionCertificate::getCropType, normalizeSegment(cropType, "UNKNOWN")));
        }
        if (StringUtils.hasText(seedClass)) {
            candidates.add(new LambdaQueryWrapper<DetectionCertificate>()
                    .eq(DetectionCertificate::getBatchId, batchId)
                    .eq(DetectionCertificate::getSeedClass, normalizeSegment(seedClass, "UNKNOWN")));
        }
        candidates.add(new LambdaQueryWrapper<DetectionCertificate>()
                .eq(DetectionCertificate::getBatchId, batchId));

        for (LambdaQueryWrapper<DetectionCertificate> wrapper : candidates) {
            wrapper.orderByDesc(DetectionCertificate::getUpdatedTime)
                    .orderByDesc(DetectionCertificate::getId)
                    .last("limit 1");
            DetectionCertificate cert = detectionCertificateMapper.selectOne(wrapper);
            if (cert != null && StringUtils.hasText(cert.getCertificateId())) {
                return cert.getCertificateId();
            }
        }
        return "";
    }

    private String resolveCropTypeByBatchId(String batchId) {
        if (!StringUtils.hasText(batchId)) {
            return "";
        }
        C1BreedingTest approvedTest = c1BreedingTestMapper.selectOne(
                new LambdaQueryWrapper<C1BreedingTest>()
                        .eq(C1BreedingTest::getDeleted, "0")
                        .eq(C1BreedingTest::getAuditStatus, "approved")
                        .eq(C1BreedingTest::getBatchId, batchId)
                        .isNotNull(C1BreedingTest::getCropType)
                        .orderByDesc(C1BreedingTest::getUpdatedTime)
                        .last("limit 1")
        );
        if (approvedTest != null && StringUtils.hasText(approvedTest.getCropType())) {
            return approvedTest.getCropType();
        }

        C1BreedingBatch c1Batch = c1BreedingBatchMapper.selectOne(
                new LambdaQueryWrapper<C1BreedingBatch>()
                        .eq(C1BreedingBatch::getDeleted, "0")
                        .eq(C1BreedingBatch::getBatchId, batchId)
                        .orderByDesc(C1BreedingBatch::getCreatedTime)
                        .last("limit 1")
        );
        if (c1Batch != null && StringUtils.hasText(c1Batch.getCropType())) {
            return c1Batch.getCropType();
        }

        BreedSeedProduceResultVO breederResult = breedSeedProduceResultMapper.getResultByProduceBatchId(batchId);
        if (breederResult != null && StringUtils.hasText(breederResult.getCropType())) {
            return breederResult.getCropType();
        }

        PrebasicSeedProduceResultVO prebasicResult = prebasicSeedProduceResultService.getResultByProduceBatchId(batchId);
        if (prebasicResult != null && StringUtils.hasText(prebasicResult.getCropType())) {
            return prebasicResult.getCropType();
        }

        BasicSeedProduceResultVO basicResult = basicSeedProduceResultService.getResultByProduceBatchId(batchId);
        if (basicResult != null && StringUtils.hasText(basicResult.getCropType())) {
            return basicResult.getCropType();
        }
        return "";
    }

    private String normalizeSegment(String value, String fallback) {
        String raw = value == null ? "" : value.trim();
        if (!StringUtils.hasText(raw)) {
            return fallback;
        }
        String normalized = raw.replaceAll("\\s+", "_").replaceAll("[^A-Za-z0-9_-]", "_");
        return StringUtils.hasText(normalized) ? normalized : fallback;
    }

    private String buildCertificateRuleId(String batchId, String seedClass, String cropType) {
        String normalizedBatchId = normalizeSegment(batchId, "UNKNOWN_BATCH");
        return "CERT-" + seedClass + "-" + cropType + "-" + normalizedBatchId;
    }
}
