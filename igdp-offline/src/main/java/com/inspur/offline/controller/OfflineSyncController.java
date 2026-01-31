package com.inspur.offline.controller;

import com.inspur.common.core.controller.BaseController;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.offline.domain.OfflineSyncRequest;
import com.inspur.offline.service.IOfflineSyncService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Offline Data Sync Controller
 * Unified handling of mobile offline data sync requests
 *
 * @author inspur
 */
@RestController
@RequestMapping("/offline/sync")
public class OfflineSyncController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(OfflineSyncController.class);

    @Autowired
    private IOfflineSyncService offlineSyncService;

    /**
     * Sync farmer data
     * Compatible with two formats:
     * 1. OfflineSyncRequest format: {"businessType":"farmer","formCode":"farmer-add","formData":{...}}
     * 2. Direct formData format: {"farmerName":"xxx","idCard":"xxx",...}
     */
    @PostMapping("/farmer")
    public AjaxResult syncFarmer(@RequestBody Map<String, Object> requestBody) {
        try {
            log.info("Received farmer data sync request: {}", requestBody);
            
            // Determine if it's OfflineSyncRequest format or direct formData format
            OfflineSyncRequest request;
            if (requestBody.containsKey("formData")) {
                // OfflineSyncRequest format
                request = new OfflineSyncRequest();
                request.setBusinessType((String) requestBody.get("businessType"));
                request.setFormCode((String) requestBody.get("formCode"));
                request.setFormData((Map<String, Object>) requestBody.get("formData"));
                request.setClientRecordId((String) requestBody.get("clientRecordId"));
            } else {
                // Direct formData format, wrap into OfflineSyncRequest
                request = new OfflineSyncRequest();
                request.setBusinessType("farmer");
                request.setFormCode("farmer-add");
                request.setFormData(requestBody);
            }
            
            return offlineSyncService.syncFarmer(request);
        } catch (Exception e) {
            log.error("Failed to sync farmer data", e);
            return AjaxResult.error("Sync failed: " + e.getMessage());
        }
    }

    /**
     * Sync land data
     * Compatible with two formats:
     * 1. OfflineSyncRequest format: {"businessType":"land","formCode":"land-add","formData":{...}}
     * 2. Direct formData format: {"landName":"xxx","areaSize":10.5,...}
     */
    @PostMapping("/land")
    public AjaxResult syncLand(@RequestBody Map<String, Object> requestBody) {
        try {
            log.info("Received land data sync request: {}", requestBody);
            
            // Determine if it's OfflineSyncRequest format or direct formData format
            OfflineSyncRequest request;
            if (requestBody.containsKey("formData")) {
                // OfflineSyncRequest format
                request = new OfflineSyncRequest();
                request.setBusinessType((String) requestBody.get("businessType"));
                request.setFormCode((String) requestBody.get("formCode"));
                request.setFormData((Map<String, Object>) requestBody.get("formData"));
                request.setClientRecordId((String) requestBody.get("clientRecordId"));
            } else {
                // Direct formData format, wrap into OfflineSyncRequest
                request = new OfflineSyncRequest();
                request.setBusinessType("land");
                request.setFormCode("land-add");
                request.setFormData(requestBody);
            }
            
            return offlineSyncService.syncLand(request);
        } catch (Exception e) {
            log.error("Failed to sync land data", e);
            return AjaxResult.error("Sync failed: " + e.getMessage());
        }
    }

    /**
     * Sync agronomic trait data
     * Compatible with two formats:
     * 1. OfflineSyncRequest format: {"businessType":"trait","formCode":"trait-data","formData":{...}}
     * 2. Direct formData format: {"plotId":"xxx","observationDate":"2024-01-01 12:00:00",...}
     */
    @PostMapping("/trait")
    public AjaxResult syncTrait(@RequestBody Map<String, Object> requestBody) {
        try {
            log.info("Received agronomic trait data sync request: {}", requestBody);
            
            // Determine if it's OfflineSyncRequest format or direct formData format
            OfflineSyncRequest request;
            if (requestBody.containsKey("formData")) {
                // OfflineSyncRequest format
                request = new OfflineSyncRequest();
                request.setBusinessType((String) requestBody.get("businessType"));
                request.setFormCode((String) requestBody.get("formCode"));
                request.setFormData((Map<String, Object>) requestBody.get("formData"));
                request.setClientRecordId((String) requestBody.get("clientRecordId"));
            } else {
                // Direct formData format, wrap into OfflineSyncRequest
                request = new OfflineSyncRequest();
                request.setBusinessType("trait");
                request.setFormCode("trait-data");
                request.setFormData(requestBody);
            }
            
            return offlineSyncService.syncTrait(request);
        } catch (Exception e) {
            log.error("Failed to sync agronomic trait data", e);
            return AjaxResult.error("Sync failed: " + e.getMessage());
        }
    }

    /**
     * Sync environment monitoring data
     * Compatible with two formats:
     * 1. OfflineSyncRequest format: {"businessType":"environment","formCode":"environment-data","formData":{...}}
     * 2. Direct formData format: {"stationId":"xxx","timestamp":"2024-01-01",...}
     */
    @PostMapping("/environment")
    public AjaxResult syncEnvironment(@RequestBody Map<String, Object> requestBody) {
        try {
            log.info("Received environment monitoring data sync request: {}", requestBody);
            
            // Determine if it's OfflineSyncRequest format or direct formData format
            OfflineSyncRequest request;
            if (requestBody.containsKey("formData")) {
                // OfflineSyncRequest format
                request = new OfflineSyncRequest();
                request.setBusinessType((String) requestBody.get("businessType"));
                request.setFormCode((String) requestBody.get("formCode"));
                request.setFormData((Map<String, Object>) requestBody.get("formData"));
                request.setClientRecordId((String) requestBody.get("clientRecordId"));
            } else {
                // Direct formData format, wrap into OfflineSyncRequest
                request = new OfflineSyncRequest();
                request.setBusinessType("environment");
                request.setFormCode("environment-data");
                request.setFormData(requestBody);
            }
            
            return offlineSyncService.syncEnvironment(request);
        } catch (Exception e) {
            log.error("Failed to sync environment monitoring data", e);
            return AjaxResult.error("Sync failed: " + e.getMessage());
        }
    }

    /**
     * Sync field inspection data
     * Compatible with two formats:
     * 1. OfflineSyncRequest format: {"businessType":"yield","formCode":"yield-inspection","formData":{...}}
     * 2. Direct formData format: {"plotId":"xxx","inspectionDate":"2024-01-01",...}
     */
    @PostMapping("/yield")
    public AjaxResult syncYield(@RequestBody Map<String, Object> requestBody) {
        try {
            log.info("Received field inspection data sync request: {}", requestBody);
            
            // Determine if it's OfflineSyncRequest format or direct formData format
            OfflineSyncRequest request;
            if (requestBody.containsKey("formData")) {
                // OfflineSyncRequest format
                request = new OfflineSyncRequest();
                request.setBusinessType((String) requestBody.get("businessType"));
                request.setFormCode((String) requestBody.get("formCode"));
                request.setFormData((Map<String, Object>) requestBody.get("formData"));
                request.setClientRecordId((String) requestBody.get("clientRecordId"));
            } else {
                // Direct formData format, wrap into OfflineSyncRequest
                request = new OfflineSyncRequest();
                request.setBusinessType("yield");
                request.setFormCode("yield-inspection");
                request.setFormData(requestBody);
            }
            
            return offlineSyncService.syncYield(request);
        } catch (Exception e) {
            log.error("Failed to sync field inspection data", e);
            return AjaxResult.error("Sync failed: " + e.getMessage());
        }
    }

    /**
     * 同步繁育田间检测数据（仅新增）
     * 兼容两种格式：
     * 1. OfflineSyncRequest格式：{"businessType":"c1BreedingTracking","formCode":"c1-breeding-tracking-add","formData":{...}}
     * 2. 直接formData格式：{"batchId":"xxx","seedClass":"C1",...}
     *
     * 说明：离线数据的ID是手动输入的，系统会判断是否已存在：
     * - 如果传入的id在数据库中已存在，则返回已同步的信息，避免重复提交
     * - 如果传入的id在数据库中不存在或未传id，则执行新增操作
     */
    @PostMapping("/c1-breeding-tracking")
    public AjaxResult syncC1BreedingTracking(@RequestBody Map<String, Object> requestBody) {
        try {
            log.info("收到繁育田间检测数据同步请求: {}", requestBody);

            // 判断是OfflineSyncRequest格式还是直接formData格式
            OfflineSyncRequest request;
            if (requestBody.containsKey("formData")) {
                // OfflineSyncRequest格式
                request = new OfflineSyncRequest();
                request.setBusinessType((String) requestBody.get("businessType"));
                request.setFormCode((String) requestBody.get("formCode"));
                request.setFormData((Map<String, Object>) requestBody.get("formData"));
                request.setClientRecordId((String) requestBody.get("clientRecordId"));
            } else {
                // 直接formData格式，包装成OfflineSyncRequest
                request = new OfflineSyncRequest();
                request.setBusinessType("c1BreedingTracking");
                request.setFormCode("c1-breeding-tracking-add");
                request.setFormData(requestBody);
            }

            return offlineSyncService.syncC1BreedingTracking(request);
        } catch (Exception e) {
            log.error("同步繁育田间检测数据失败", e);
            return AjaxResult.error("同步失败: " + e.getMessage());
        }
    }

    /**
     * 同步农事记录数据
     * 兼容两种格式：
     * 1. OfflineSyncRequest格式：{"businessType":"farming-record","formCode":"farming-record-add","formData":{...}}
     * 2. 直接formData格式：{"plotId":"xxx","activityType":"xxx",...}
     */
    @PostMapping("/farming-record")
    public AjaxResult syncFarmingRecord(@RequestBody Map<String, Object> requestBody) {
        try {
            log.info("收到农事记录数据同步请求: {}", requestBody);

            // 判断是OfflineSyncRequest格式还是直接formData格式
            OfflineSyncRequest request;
            if (requestBody.containsKey("formData")) {
                // OfflineSyncRequest格式
                request = new OfflineSyncRequest();
                request.setBusinessType((String) requestBody.get("businessType"));
                request.setFormCode((String) requestBody.get("formCode"));
                request.setFormData((Map<String, Object>) requestBody.get("formData"));
                request.setClientRecordId((String) requestBody.get("clientRecordId"));
            } else {
                // 直接formData格式，包装成OfflineSyncRequest
                request = new OfflineSyncRequest();
                request.setBusinessType("farming-record");
                request.setFormCode("farming-record-add");
                request.setFormData(requestBody);
            }

            return offlineSyncService.syncFarmingRecord(request);
        } catch (Exception e) {
            log.error("同步农事记录数据失败", e);
            return AjaxResult.error("同步失败: " + e.getMessage());
        }
    }

    /**
     * 同步农户需求数据（仅新增）
     * 兼容两种格式：
     * 1. OfflineSyncRequest格式：{"businessType":"farmerDemand","formCode":"farmer-demand-add","formData":{...}}
     * 2. 直接formData格式：{"farmerId":"xxx","farmerName":"xxx","inputItems":[...],...}
     *
     * 说明：离线数据的ID是手动输入的，系统会判断是否已存在：
     * - 如果传入的id在数据库中已存在，则返回已同步的信息，避免重复提交
     * - 如果传入的id在数据库中不存在或未传id，则执行新增操作
     */
    @PostMapping("/farmer-demand")
    public AjaxResult syncFarmerDemand(@RequestBody Map<String, Object> requestBody) {
        try {
            log.info("收到农户需求数据同步请求: {}", requestBody);
            // 判断是OfflineSyncRequest格式还是直接formData格式
            OfflineSyncRequest request;
            if (requestBody.containsKey("formData")) {
                // OfflineSyncRequest格式
                request = new OfflineSyncRequest();
                request.setBusinessType((String) requestBody.get("businessType"));
                request.setFormCode((String) requestBody.get("formCode"));
                request.setFormData((Map<String, Object>) requestBody.get("formData"));
                request.setClientRecordId((String) requestBody.get("clientRecordId"));
            } else {
                // 直接formData格式，包装成OfflineSyncRequest
                request = new OfflineSyncRequest();
                request.setBusinessType("farmerDemand");
                request.setFormCode("farmer-demand-add");
                request.setFormData(requestBody);
            }

            return offlineSyncService.syncFarmerDemand(request);
        } catch (Exception e) {
            log.error("同步农户需求数据失败", e);
            return AjaxResult.error("同步失败: " + e.getMessage());
        }
    }
}
