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
 * 离线数据同步Controller
 * 统一处理移动端离线数据的同步请求
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
     * 同步农民数据
     * 兼容两种格式：
     * 1. OfflineSyncRequest格式：{"businessType":"farmer","formCode":"farmer-add","formData":{...}}
     * 2. 直接formData格式：{"farmerName":"xxx","idCard":"xxx",...}
     */
    @PostMapping("/farmer")
    public AjaxResult syncFarmer(@RequestBody Map<String, Object> requestBody) {
        try {
            log.info("收到农民数据同步请求: {}", requestBody);
            
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
                request.setBusinessType("farmer");
                request.setFormCode("farmer-add");
                request.setFormData(requestBody);
            }
            
            return offlineSyncService.syncFarmer(request);
        } catch (Exception e) {
            log.error("同步农民数据失败", e);
            return AjaxResult.error("同步失败: " + e.getMessage());
        }
    }

    /**
     * 同步土地数据
     * 兼容两种格式：
     * 1. OfflineSyncRequest格式：{"businessType":"land","formCode":"land-add","formData":{...}}
     * 2. 直接formData格式：{"landName":"xxx","areaSize":10.5,...}
     */
    @PostMapping("/land")
    public AjaxResult syncLand(@RequestBody Map<String, Object> requestBody) {
        try {
            log.info("收到土地数据同步请求: {}", requestBody);
            
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
                request.setBusinessType("land");
                request.setFormCode("land-add");
                request.setFormData(requestBody);
            }
            
            return offlineSyncService.syncLand(request);
        } catch (Exception e) {
            log.error("同步土地数据失败", e);
            return AjaxResult.error("同步失败: " + e.getMessage());
        }
    }
}
