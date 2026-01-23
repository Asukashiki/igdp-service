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
}