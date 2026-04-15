package com.inspur.seed.controller;

import com.inspur.common.core.domain.AjaxResult;
import com.inspur.seed.multiplication.basic.domain.dto.BreedingBatchQueryDTO;
import com.inspur.seed.multiplication.basic.domain.vo.BreedingBatchVO;
import com.inspur.seed.multiplication.basic.service.IBreedingBatchInfoService;
import com.inspur.seed.multiplication.c1Seed.domain.dto.C1BreedingBatchQueryDTO;
import com.inspur.seed.multiplication.c1Seed.domain.vo.C1BreedingBatchVO;
import com.inspur.seed.multiplication.c1Seed.service.IC1BreedingBatchService;
import com.inspur.seed.service.IDetectionCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 繁育批次检测Controller - 统一返回Basic和C1批次用于检测功能
 *
 * @author system
 * @since 2025-12-29
 */
@RestController
@RequestMapping("/seed/batch")
public class BreedingBatchDetectionController {

    @Autowired
    private IBreedingBatchInfoService breedingBatchInfoService;

    @Autowired
    private IC1BreedingBatchService c1BreedingBatchService;

    @Autowired
    private IDetectionCertificateService detectionCertificateService;

    /**
     * 获取用于检测的批次列表（Basic + C1）
     * 用于田间检测和实验室检测页面的批次选择器
     *
     * @return 统一格式的批次列表，包含seedClass字段标识批次类型
     */
    @GetMapping("/list-for-detection")
    public AjaxResult getBatchesForDetection() {
        List<Map<String, Object>> resultList = new ArrayList<>();

        try {
            // 1. 获取Basic种子生产批次（Breeder Seed）
            BreedingBatchQueryDTO basicQuery = new BreedingBatchQueryDTO();
            basicQuery.setPageNum(1);
            basicQuery.setPageSize(1000);
            List<BreedingBatchVO> basicBatches = breedingBatchInfoService.queryList(basicQuery);

            // 转换Basic批次为统一格式
            if (basicBatches != null && !basicBatches.isEmpty()) {
                for (BreedingBatchVO batch : basicBatches) {
                    Map<String, Object> batchMap = new HashMap<>();
                    batchMap.put("batchId", batch.getBatchId());  // 使用batchId
                    batchMap.put("batchName", batch.getBatchId()); // 批次名称使用batchId
                    batchMap.put("varietyName", batch.getVarietyName());
                    batchMap.put("cropType", batch.getCropType());
                    batchMap.put("seedClass", "Basic");  // 标识为Basic种子
                    batchMap.put("startDate", batch.getStartDate());  // 开始日期
                    batchMap.put("endDate", batch.getEndDate());  // 结束日期
                    resultList.add(batchMap);
                }
            }

            // 2. 获取C1种子批次
            C1BreedingBatchQueryDTO c1Query = new C1BreedingBatchQueryDTO();
            c1Query.setPageNum(1);
            c1Query.setPageSize(1000);
            c1Query.setAuditStatus("approved");
            List<C1BreedingBatchVO> c1Batches = c1BreedingBatchService.pageList(c1Query).getRecords();

            // 转换C1批次为统一格式
            if (c1Batches != null && !c1Batches.isEmpty()) {
                for (C1BreedingBatchVO batch : c1Batches) {
                    Map<String, Object> batchMap = new HashMap<>();
                    batchMap.put("batchId", batch.getBatchId());  // C1使用batchId
                    batchMap.put("batchName", batch.getBatchId()); // C1批次使用batchId作为名称
                    batchMap.put("varietyName", batch.getVarietyName());
                    batchMap.put("cropType", batch.getCropType());
                    batchMap.put("seedClass", "C1");  // 标识为C1种子
                    batchMap.put("startDate", batch.getStartDate());
                    batchMap.put("endDate", batch.getEndDate());
                    resultList.add(batchMap);
                }
            }

            return AjaxResult.success(resultList);

        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error("获取批次列表失败：" + e.getMessage());
        }
    }

    /**
     * 批量获取 Certificate ID 映射（推荐前端调用）
     * 规则：CERT-{SeedClass}-{CropType}-{BatchId}
     * 说明：不使用 uuid；同一批次始终生成同一个证书ID。
     */
    @PostMapping("/certificate-id-map")
    public AjaxResult getCertificateIdMap(@RequestBody(required = false) List<String> batchIds) {
        try {
            return AjaxResult.success(detectionCertificateService.getCertificateIdMap(batchIds));
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error("获取证书ID映射失败：" + e.getMessage());
        }
    }

    /**
     * 根据生产批次ID获取 Certificate ID
     */
    @GetMapping("/certificate-id/{produceBatchId}")
    public AjaxResult getCertificateIdByProduceBatchId(@PathVariable("produceBatchId") String produceBatchId,
                                                       @RequestParam(value = "seedClass", required = false) String seedClass,
                                                       @RequestParam(value = "cropType", required = false) String cropType) {
        try {
            Map<String, String> result = new HashMap<>();
            result.put("produceBatchId", produceBatchId);
            result.put("certificateId", detectionCertificateService.getCertificateId(produceBatchId, seedClass, cropType));
            return AjaxResult.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error("获取证书ID失败：" + e.getMessage());
        }
    }

}
