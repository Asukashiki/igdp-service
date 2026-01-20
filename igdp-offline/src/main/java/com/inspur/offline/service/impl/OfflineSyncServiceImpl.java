package com.inspur.offline.service.impl;

import cn.hutool.core.util.StrUtil;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.farmland.domain.FarmerInfo;
import com.inspur.farmland.domain.LandInfo;
import com.inspur.farmland.service.IFarmerInfoService;
import com.inspur.farmland.service.ILandInfoService;
import com.inspur.offline.domain.OfflineSyncRequest;
import com.inspur.offline.service.IOfflineSyncService;
import com.inspur.seed.breeding.farming.domain.entity.FarmingRecord;
import com.inspur.seed.breeding.farming.service.IFarmingRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 离线数据同步服务实现类
 *
 * @author inspur
 */
@Service
public class OfflineSyncServiceImpl implements IOfflineSyncService {

    private static final Logger log = LoggerFactory.getLogger(OfflineSyncServiceImpl.class);

    @Autowired
    private IFarmerInfoService farmerInfoService;

    @Autowired
    private ILandInfoService landInfoService;

    @Autowired
    private IFarmingRecordService farmingRecordService;

    @Override
    public AjaxResult syncFarmer(OfflineSyncRequest request) {
        try {
            Map<String, Object> formData = request.getFormData();
            if (formData == null || formData.isEmpty()) {
                return AjaxResult.error("表单数据不能为空");
            }

            // 转换为FarmerInfo对象
            FarmerInfo farmerInfo = convertToFarmerInfo(formData);

            // 数据校验
            String validationError = validateFarmer(farmerInfo);
            if (StrUtil.isNotBlank(validationError)) {
                return AjaxResult.error(validationError);
            }

            // 校验身份证号唯一性
            if (!farmerInfoService.checkIdCardUnique(farmerInfo.getIdCard(), null)) {
                return AjaxResult.error("身份证号已存在");
            }

            // 保存数据
            String farmerId = farmerInfoService.insertFarmerInfo(farmerInfo);

            // 返回结果
            Map<String, Object> result = new java.util.HashMap<>();
            result.put("id", farmerId);
            result.put("farmerId", farmerId);

            log.info("农民数据同步成功: farmerId={}, farmerName={}", farmerId, farmerInfo.getFarmerName());
            return AjaxResult.success("同步成功", result);

        } catch (Exception e) {
            log.error("同步农民数据失败", e);
            return AjaxResult.error("同步失败: " + e.getMessage());
        }
    }

    @Override
    public AjaxResult syncLand(OfflineSyncRequest request) {
        try {
            Map<String, Object> formData = request.getFormData();
            if (formData == null || formData.isEmpty()) {
                return AjaxResult.error("表单数据不能为空");
            }

            // 转换为LandInfo对象
            LandInfo landInfo = convertToLandInfo(formData);

            // 数据校验
            String validationError = validateLand(landInfo);
            if (StrUtil.isNotBlank(validationError)) {
                return AjaxResult.error(validationError);
            }

            // 保存数据
            String landId = landInfoService.insertLandInfo(landInfo);

            // 返回结果
            Map<String, Object> result = new java.util.HashMap<>();
            result.put("id", landId);
            result.put("landId", landId);

            log.info("土地数据同步成功: landId={}, landName={}", landId, landInfo.getLandName());
            return AjaxResult.success("同步成功", result);

        } catch (Exception e) {
            log.error("同步土地数据失败", e);
            return AjaxResult.error("同步失败: " + e.getMessage());
        }
    }

    @Override
    public AjaxResult syncFarmingRecord(OfflineSyncRequest request) {
        try {
            Map<String, Object> formData = request.getFormData();
            log.info("开始处理农事记录同步请求: {}", formData);

            if (formData == null || formData.isEmpty()) {
                return AjaxResult.error("表单数据不能为空");
            }
            
            // 转换为FarmingRecord对象
            FarmingRecord farmingRecord = convertToFarmingRecord(formData);
            log.info("转换后的FarmingRecord对象: {}", farmingRecord);
            
            // 数据校验
            String validationError = validateFarmingRecord(farmingRecord);
            if (StrUtil.isNotBlank(validationError)) {
                log.warn("农事记录数据校验失败: {}", validationError);
                return AjaxResult.error(validationError);
            }
            
            // 保存数据
            String id = farmingRecordService.insertFarmingRecord(farmingRecord);
            
            // 返回结果
            Map<String, Object> result = new java.util.HashMap<>();
            result.put("id", id);
            result.put("farmingRecordId", farmingRecord.getFarmingRecordId());
            
            log.info("农事记录数据同步成功: farmingId={}", id);
            return AjaxResult.success("同步成功", result);
        } catch (Exception e) {
            log.error("同步农事记录数据失败", e);
            return AjaxResult.error("同步失败: " + e.getMessage());
        }
    }

    /**
     * 将表单数据转换为FarmerInfo对象
     */
    private FarmerInfo convertToFarmerInfo(Map<String, Object> formData) {
        FarmerInfo farmerInfo = new FarmerInfo();

        // 基本信息
        farmerInfo.setFarmerName(getString(formData, "farmerName"));
        farmerInfo.setIdCard(getString(formData, "idCard"));
        farmerInfo.setGender(getString(formData, "gender"));
        farmerInfo.setPhone(getString(formData, "phone"));
        farmerInfo.setEmail(getString(formData, "email"));
        farmerInfo.setYouthCategory(getString(formData, "youthCategory"));

        // 日期处理
        Object birthdayObj = formData.get("birthday");
        if (birthdayObj != null) {
            try {
                if (birthdayObj instanceof String) {
                    String birthdayStr = (String) birthdayObj;
                    if (birthdayStr.length() >= 10) {
                        birthdayStr = birthdayStr.substring(0, 10);
                        farmerInfo.setBirthday(java.sql.Date.valueOf(birthdayStr));
                    }
                }
            } catch (Exception e) {
                log.warn("日期解析失败: {}", birthdayObj, e);
            }
        }

        // 行政区划
        farmerInfo.setRegionCode(getString(formData, "regionCode"));
        farmerInfo.setRegionName(getString(formData, "regionName"));
        farmerInfo.setZoneCode(getString(formData, "zoneCode"));
        farmerInfo.setZoneName(getString(formData, "zoneName"));
        farmerInfo.setWoredaCode(getString(formData, "woredaCode"));
        farmerInfo.setWoredaName(getString(formData, "woredaName"));
        farmerInfo.setKebeleCode(getString(formData, "kebeleCode"));
        farmerInfo.setKebeleName(getString(formData, "kebeleName"));

        // 其他信息
        farmerInfo.setAddress(getString(formData, "address"));
        farmerInfo.setUnionId(getString(formData, "unionId"));
        farmerInfo.setCooperativeId(getString(formData, "cooperativeId"));
        farmerInfo.setDaId(getString(formData, "daId"));
        farmerInfo.setRemark(getString(formData, "remark"));

        return farmerInfo;
    }

    /**
     * 将表单数据转换为LandInfo对象
     */
    private LandInfo convertToLandInfo(Map<String, Object> formData) {
        LandInfo landInfo = new LandInfo();

        // 基本信息
        landInfo.setLandName(getString(formData, "landName"));
        landInfo.setLandNo(getString(formData, "landNo"));
        landInfo.setOwnerType(getString(formData, "ownerType"));
        landInfo.setOwnerName(getString(formData, "ownerName"));
        landInfo.setOwnerIdCard(getString(formData, "ownerIdCard"));
        landInfo.setLandType(getString(formData, "landType"));
        landInfo.setLandGraphic(getString(formData, "landGraphic"));

        // 面积处理
        Object areaSizeObj = formData.get("areaSize");
        if (areaSizeObj != null) {
            try {
                if (areaSizeObj instanceof Number) {
                    landInfo.setAreaSize(new BigDecimal(areaSizeObj.toString()));
                } else if (areaSizeObj instanceof String) {
                    String areaStr = (String) areaSizeObj;
                    if (StrUtil.isNotBlank(areaStr)) {
                        landInfo.setAreaSize(new BigDecimal(areaStr));
                    }
                }
            } catch (Exception e) {
                log.warn("面积解析失败: {}", areaSizeObj, e);
            }
        }

        landInfo.setAreaUnit(getString(formData, "areaUnit"));

        // 地理信息
        Object latitudeObj = formData.get("latitude");
        if (latitudeObj != null) {
            try {
                landInfo.setLatitude(new BigDecimal(latitudeObj.toString()));
            } catch (Exception e) {
                log.warn("纬度解析失败: {}", latitudeObj, e);
            }
        }

        Object longitudeObj = formData.get("longitude");
        if (longitudeObj != null) {
            try {
                landInfo.setLongitude(new BigDecimal(longitudeObj.toString()));
            } catch (Exception e) {
                log.warn("经度解析失败: {}", longitudeObj, e);
            }
        }

        landInfo.setPlotBoundary(getString(formData, "plotBoundary"));

        // 行政区划
        landInfo.setRegionCode(getString(formData, "regionCode"));
        landInfo.setRegionName(getString(formData, "regionName"));
        landInfo.setZoneCode(getString(formData, "zoneCode"));
        landInfo.setZoneName(getString(formData, "zoneName"));
        landInfo.setWoredaCode(getString(formData, "woredaCode"));
        landInfo.setWoredaName(getString(formData, "woredaName"));
        landInfo.setKebeleCode(getString(formData, "kebeleCode"));
        landInfo.setKebeleName(getString(formData, "kebeleName"));

        // 其他信息
        landInfo.setAddress(getString(formData, "address"));
        landInfo.setFarmerId(getString(formData, "farmerId"));
        landInfo.setCurrentStatus(getString(formData, "currentStatus"));
        landInfo.setDaId(getString(formData, "daId"));
        landInfo.setRemark(getString(formData, "remark"));

        return landInfo;
    }

    /**
     * 校验农民数据
     * 参考PC端校验规则
     */
    private String validateFarmer(FarmerInfo farmerInfo) {
        // 必填字段校验
        if (StrUtil.isBlank(farmerInfo.getFarmerName())) {
            return "农民姓名不能为空";
        }

        if (StrUtil.isBlank(farmerInfo.getIdCard())) {
            return "身份证号不能为空";
        }

        // 姓名长度校验（最多100个字符）
        if (farmerInfo.getFarmerName().length() > 100) {
            return "农民姓名最多100个字符";
        }

        // 身份证号长度校验（最多50个字符）
        if (farmerInfo.getIdCard().length() > 50) {
            return "身份证号最多50个字符";
        }

        // 电话格式校验（可选）
        if (StrUtil.isNotBlank(farmerInfo.getPhone())) {
            // 简单的电话格式校验：只允许数字、+、-、空格
            if (!farmerInfo.getPhone().matches("^[0-9+\\-\\s()]+$")) {
                return "电话号码格式不正确";
            }
        }

        return null;
    }

    /**
     * 校验土地数据
     * 参考PC端校验规则
     */
    private String validateLand(LandInfo landInfo) {
        // 必填字段校验
        if (StrUtil.isBlank(landInfo.getLandName())) {
            return "地块名称不能为空";
        }

        if (StrUtil.isBlank(landInfo.getOwnerType())) {
            return "土地权属不能为空";
        }

        if (StrUtil.isBlank(landInfo.getLandType())) {
            return "地块类型不能为空";
        }

        if (landInfo.getAreaSize() == null) {
            return "地块面积不能为空";
        }

        if (landInfo.getAreaSize().compareTo(BigDecimal.ZERO) <= 0) {
            return "地块面积必须大于0";
        }

        // 地块名称长度校验（最多100个字符）
        if (landInfo.getLandName().length() > 100) {
            return "地块名称最多100个字符";
        }

        // 村代码必填校验
        if (StrUtil.isBlank(landInfo.getKebeleCode())) {
            return "村代码不能为空";
        }

        // 详细地址必填校验
        if (StrUtil.isBlank(landInfo.getAddress())) {
            return "详细地址不能为空";
        }

        return null;
    }

    /**
     * 将表单数据转换为FarmingRecord对象
     */
    private FarmingRecord convertToFarmingRecord(Map<String, Object> formData) {
        FarmingRecord farmingRecord = new FarmingRecord();

        farmingRecord.setFarmingRecordId(getString(formData, "farmingRecordId"));
        farmingRecord.setPlotId(getString(formData, "plotId"));
        farmingRecord.setTrialId(getString(formData, "trialId"));
        farmingRecord.setBatchId(getString(formData, "batchId"));
        
        // 日期处理
        Object activityDateObj = formData.get("activityDate");
        if (activityDateObj != null) {
            try {
                if (activityDateObj instanceof String) {
                    String dateStr = (String) activityDateObj;
                    if (dateStr.length() >= 10) {
                        dateStr = dateStr.substring(0, 10);
                        farmingRecord.setActivityDate(java.sql.Date.valueOf(dateStr));
                    }
                }
            } catch (Exception e) {
                log.warn("日期解析失败: {}", activityDateObj, e);
            }
        }

        farmingRecord.setActivityType(getString(formData, "activityType"));
        farmingRecord.setInputName(getString(formData, "inputName"));

        // 数量处理
        Object quantityObj = formData.get("quantity");
        if (quantityObj != null) {
            try {
                if (quantityObj instanceof Number) {
                    farmingRecord.setQuantity(new BigDecimal(quantityObj.toString()));
                } else if (quantityObj instanceof String) {
                    String quantityStr = (String) quantityObj;
                    if (StrUtil.isNotBlank(quantityStr)) {
                        farmingRecord.setQuantity(new BigDecimal(quantityStr));
                    }
                }
            } catch (Exception e) {
                log.warn("数量解析失败: {}", quantityObj, e);
            }
        }

        farmingRecord.setUnit(getString(formData, "unit"));
        farmingRecord.setOperatorId(getString(formData, "operatorId"));
        farmingRecord.setOperationDesc(getString(formData, "operationDesc"));

        return farmingRecord;
    }

    /**
     * 校验农事记录数据
     */
    private String validateFarmingRecord(FarmingRecord farmingRecord) {
        // 必填字段校验
        if (StrUtil.isBlank(farmingRecord.getPlotId())) {
            return "地块ID不能为空";
        }

        if (StrUtil.isBlank(farmingRecord.getActivityType())) {
            return "农事活动类型不能为空";
        }
        
        if (farmingRecord.getActivityDate() == null) {
            return "活动日期不能为空";
        }

        if (StrUtil.isBlank(farmingRecord.getOperatorId())) {
            return "操作员ID不能为空";
        }

        return null;
    }

    /**
     * 从Map中获取String值
     */
    private String getString(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value == null) {
            return null;
        }
        return value.toString().trim();
    }
}
