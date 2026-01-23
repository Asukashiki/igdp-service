package com.inspur.offline.service.impl;

import cn.hutool.core.util.StrUtil;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.farmland.domain.FarmerInfo;
import com.inspur.farmland.domain.LandInfo;
import com.inspur.farmland.service.IFarmerInfoService;
import com.inspur.farmland.service.ILandInfoService;
import com.inspur.offline.domain.OfflineSyncRequest;
import com.inspur.offline.service.IOfflineSyncService;
import com.inspur.seed.breeding.agronomicTrait.domain.dto.AgronomicTraitDetailDTO;
import com.inspur.seed.breeding.agronomicTrait.domain.dto.AgronomicTraitRecordDTO;
import com.inspur.seed.breeding.agronomicTrait.service.IAgronomicTraitRecordService;
import com.inspur.seed.breeding.environment.domain.entity.EnvironmentNewData;
import com.inspur.seed.breeding.environment.service.IEnvironmentNewDataService;
import com.inspur.seed.breeding.fieldInspection.domain.dto.BreedingYieldDataDTO;
import com.inspur.seed.breeding.fieldInspection.service.IBreedingYieldDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Offline Data Sync Service Implementation
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
    private IAgronomicTraitRecordService agronomicTraitRecordService;

    @Autowired
    private IEnvironmentNewDataService environmentNewDataService;

    @Autowired
    private IBreedingYieldDataService breedingYieldDataService;

    @Override
    public AjaxResult syncFarmer(OfflineSyncRequest request) {
        try {
            Map<String, Object> formData = request.getFormData();
            if (formData == null || formData.isEmpty()) {
                return AjaxResult.error("Form data cannot be empty");
            }

            // Convert to FarmerInfo object
            FarmerInfo farmerInfo = convertToFarmerInfo(formData);

            // Data validation
            String validationError = validateFarmer(farmerInfo);
            if (StrUtil.isNotBlank(validationError)) {
                return AjaxResult.error(validationError);
            }

            // Validate ID card uniqueness
            if (!farmerInfoService.checkIdCardUnique(farmerInfo.getIdCard(), null)) {
                return AjaxResult.error("ID card already exists");
            }

            // Save data
            String farmerId = farmerInfoService.insertFarmerInfo(farmerInfo);

            // Return result
            Map<String, Object> result = new java.util.HashMap<>();
            result.put("id", farmerId);
            result.put("farmerId", farmerId);

            log.info("Farmers' data synchronization was successful.: farmerId={}, farmerName={}", farmerId, farmerInfo.getFarmerName());
            return AjaxResult.success("Synchronization was successful", result);

        } catch (Exception e) {
            log.error("Failed to sync farmer data", e);
            return AjaxResult.error("Sync failed: " + e.getMessage());
        }
    }

    @Override
    public AjaxResult syncLand(OfflineSyncRequest request) {
        try {
            Map<String, Object> formData = request.getFormData();
            if (formData == null || formData.isEmpty()) {
                return AjaxResult.error("Form data cannot be empty");
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

            log.info("Land data synchronization was successful.: landId={}, landName={}", landId, landInfo.getLandName());
            return AjaxResult.success("Synchronization was successful", result);

        } catch (Exception e) {
            log.error("Failed to sync land data", e);
            return AjaxResult.error("Sync failed: " + e.getMessage());
        }
    }

    @Override
    public AjaxResult syncTrait(OfflineSyncRequest request) {
        try {
            Map<String, Object> formData = request.getFormData();
            if (formData == null || formData.isEmpty()) {
                return AjaxResult.error("Form data cannot be empty");
            }

            // 转换为AgronomicTraitRecordDTO对象
            AgronomicTraitRecordDTO dto = convertToTraitRecordDTO(formData);

            // 数据校验
            String validationError = validateTrait(dto);
            if (StrUtil.isNotBlank(validationError)) {
                return AjaxResult.error(validationError);
            }

            // 保存数据
            String recordId = agronomicTraitRecordService.insertRecord(dto);

            // 返回结果
            Map<String, Object> result = new java.util.HashMap<>();
            result.put("id", recordId);
            result.put("recordId", recordId);

            log.info("Agronomic trait data synchronization was successful.: recordId={}, plotId={}", recordId, dto.getPlotId());
            return AjaxResult.success("Synchronization was successful", result);

        } catch (Exception e) {
            log.error("Failed to sync agronomic trait data", e);
            return AjaxResult.error("Sync failed: " + e.getMessage());
        }
    }

    @Override
    public AjaxResult syncEnvironment(OfflineSyncRequest request) {
        try {
            Map<String, Object> formData = request.getFormData();
            if (formData == null || formData.isEmpty()) {
                return AjaxResult.error("Form data cannot be empty");
            }

            // 转换为EnvironmentNewData对象
            EnvironmentNewData environmentData = convertToEnvironmentNewData(formData);

            // 数据校验
            String validationError = validateEnvironment(environmentData);
            if (StrUtil.isNotBlank(validationError)) {
                return AjaxResult.error(validationError);
            }

            // 保存数据
            String envRecordId = environmentNewDataService.insertEnvironmentNewData(environmentData);

            // 返回结果
            Map<String, Object> result = new java.util.HashMap<>();
            result.put("id", envRecordId);
            result.put("envRecordId", envRecordId);

            log.info("Environment monitoring data synchronization was successful.: envRecordId={}, stationId={}", envRecordId, environmentData.getStationId());
            return AjaxResult.success("Synchronization was successful", result);

        } catch (Exception e) {
            log.error("Failed to sync environment monitoring data", e);
            return AjaxResult.error("Sync failed: " + e.getMessage());
        }
    }

    @Override
    public AjaxResult syncYield(OfflineSyncRequest request) {
        try {
            Map<String, Object> formData = request.getFormData();
            if (formData == null || formData.isEmpty()) {
                return AjaxResult.error("Form data cannot be empty");
            }

            // 转换为BreedingYieldDataDTO对象
            BreedingYieldDataDTO dto = convertToYieldDataDTO(formData);

            // 数据校验
            String validationError = validateYield(dto);
            if (StrUtil.isNotBlank(validationError)) {
                return AjaxResult.error(validationError);
            }

            // 保存数据
            int result = breedingYieldDataService.insertBreedingYieldData(dto);

            // 返回结果
            // 注意：insertBreedingYieldData 返回的是影响行数，不是 ID
            // 由于 MyBatis-Plus 的 ASSIGN_UUID 会自动生成 ID 并设置到实体中，
            // 但 insertBreedingYieldData 方法内部创建了新的实体，我们无法直接获取生成的 ID
            // 如果需要 ID，可以通过其他方式获取（如查询最新插入的记录）
            Map<String, Object> resultMap = new java.util.HashMap<>();
            resultMap.put("success", result > 0);
            if (StrUtil.isNotBlank(dto.getId())) {
                resultMap.put("id", dto.getId());
            }

            log.info("Breeding yield data synchronization was successful.: plotId={}, inspectionType={}", dto.getPlotId(), dto.getInspectionType());
            return AjaxResult.success("Synchronization was successful", resultMap);

        } catch (Exception e) {
            log.error("Failed to sync breeding yield data", e);
            return AjaxResult.error("Sync failed: " + e.getMessage());
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
                log.warn("Failed to parse birthday: {}", birthdayObj, e);
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
                log.warn("Failed to parse area size: {}", areaSizeObj, e);
            }
        }

        landInfo.setAreaUnit(getString(formData, "areaUnit"));

        // 地理信息
        Object latitudeObj = formData.get("latitude");
        if (latitudeObj != null) {
            try {
                landInfo.setLatitude(new BigDecimal(latitudeObj.toString()));
            } catch (Exception e) {
                log.warn("Failed to parse latitude: {}", latitudeObj, e);
            }
        }

        Object longitudeObj = formData.get("longitude");
        if (longitudeObj != null) {
            try {
                landInfo.setLongitude(new BigDecimal(longitudeObj.toString()));
            } catch (Exception e) {
                log.warn("Failed to parse longitude: {}", longitudeObj, e);
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
            return "The name of the farmer cannot be left blank.";
        }

        if (StrUtil.isBlank(farmerInfo.getIdCard())) {
            return "The ID card number cannot be left blank.";
        }

        // 姓名长度校验（最多100个字符）
        if (farmerInfo.getFarmerName().length() > 100) {
            return "The name of the farmer cannot exceed 100 characters.";
        }

        // 身份证号长度校验（最多50个字符）
        if (farmerInfo.getIdCard().length() > 50) {
            return "The ID card number cannot exceed 50 characters.";
        }

        // 电话格式校验（可选）
        if (StrUtil.isNotBlank(farmerInfo.getPhone())) {
            // 简单的电话格式校验：只允许数字、+、-、空格
            if (!farmerInfo.getPhone().matches("^[0-9+\\-\\s()]+$")) {
                return "The phone number format is incorrect.";
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
            return "The name of the plot cannot be left blank.";
        }

        if (StrUtil.isBlank(landInfo.getOwnerType())) {
            return "The land ownership cannot be left blank.";
        }

        if (StrUtil.isBlank(landInfo.getLandType())) {
            return "The type of the plot cannot be left blank.";
        }

        if (landInfo.getAreaSize() == null) {
            return "The area size of the plot cannot be left blank.";
        }

        if (landInfo.getAreaSize().compareTo(BigDecimal.ZERO) <= 0) {
            return "The area size of the plot must be greater than 0.";
        }

        // 地块名称长度校验（最多100个字符）
        if (landInfo.getLandName().length() > 100) {
            return "The name of the plot cannot exceed 100 characters.";
        }

        // 村代码必填校验
        if (StrUtil.isBlank(landInfo.getKebeleCode())) {
            return "The kebele code cannot be left blank.";
        }

        // 详细地址必填校验
        if (StrUtil.isBlank(landInfo.getAddress())) {
            return "The address cannot be left blank.";
        }

        return null;
    }

    /**
     * 将表单数据转换为AgronomicTraitRecordDTO对象
     */
    private AgronomicTraitRecordDTO convertToTraitRecordDTO(Map<String, Object> formData) {
        AgronomicTraitRecordDTO dto = new AgronomicTraitRecordDTO();

        // 基本信息
        dto.setRecordId(getString(formData, "recordId"));
        dto.setPlotId(getString(formData, "plotId"));
        dto.setTrialId(getString(formData, "trialId"));
        dto.setBatchId(getString(formData, "batchId"));
        dto.setGrowthStage(getString(formData, "growthStage"));
        dto.setObserverId(getString(formData, "observerId"));
        dto.setPhotoUrl(getString(formData, "photoUrl"));
        dto.setRemarks(getString(formData, "remarks"));
        dto.setStatus(getString(formData, "status"));

        // 观测日期处理
        Object observationDateObj = formData.get("observationDate");
        if (observationDateObj != null) {
            try {
                if (observationDateObj instanceof String) {
                    String dateStr = (String) observationDateObj;
                    // 支持多种日期格式
                    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        dto.setObservationDate(sdf1.parse(dateStr));
                    } catch (Exception e) {
                        try {
                            dto.setObservationDate(sdf2.parse(dateStr));
                        } catch (Exception e2) {
                            log.warn("Date parsing failed: {}", dateStr, e2);
                        }
                    }
                } else if (observationDateObj instanceof Number) {
                    // 时间戳
                    long timestamp = ((Number) observationDateObj).longValue();
                    dto.setObservationDate(new Date(timestamp));
                }
            } catch (Exception e) {
                log.warn("Date parsing failed: {}", observationDateObj, e);
            }
        }

        // 处理明细列表
        Object detailListObj = formData.get("detailList");
        if (detailListObj instanceof List) {
            List<Map<String, Object>> detailList = (List<Map<String, Object>>) detailListObj;
            List<AgronomicTraitDetailDTO> detailDTOList = new ArrayList<>();
            
            for (Map<String, Object> detailMap : detailList) {
                AgronomicTraitDetailDTO detailDTO = new AgronomicTraitDetailDTO();
                detailDTO.setDetailId(getString(detailMap, "detailId"));
                detailDTO.setTraitCode(getString(detailMap, "traitCode"));
                detailDTO.setTraitName(getString(detailMap, "traitName"));
                detailDTO.setUnit(getString(detailMap, "unit"));
                
                // 性状值处理
                Object traitValueObj = detailMap.get("traitValue");
                if (traitValueObj != null) {
                    try {
                        if (traitValueObj instanceof Number) {
                            detailDTO.setTraitValue(new BigDecimal(traitValueObj.toString()));
                        } else if (traitValueObj instanceof String) {
                            String valueStr = (String) traitValueObj;
                            if (StrUtil.isNotBlank(valueStr)) {
                                detailDTO.setTraitValue(new BigDecimal(valueStr));
                            }
                        }
                    } catch (Exception e) {
                        log.warn("Trait value parsing failed: {}", traitValueObj, e);
                    }
                }
                
                // 排序序号处理
                Object sortOrderObj = detailMap.get("sortOrder");
                if (sortOrderObj != null) {
                    try {
                        if (sortOrderObj instanceof Number) {
                            detailDTO.setSortOrder(((Number) sortOrderObj).intValue());
                        } else if (sortOrderObj instanceof String) {
                            String sortStr = (String) sortOrderObj;
                            if (StrUtil.isNotBlank(sortStr)) {
                                detailDTO.setSortOrder(Integer.parseInt(sortStr));
                            }
                        }
                    } catch (Exception e) {
                        log.warn("Sort order parsing failed: {}", sortOrderObj, e);
                    }
                }
                
                detailDTOList.add(detailDTO);
            }
            
            dto.setDetailList(detailDTOList);
        }

        return dto;
    }

    /**
     * 校验农艺性状数据
     */
    private String validateTrait(AgronomicTraitRecordDTO dto) {
        // 必填字段校验
        if (StrUtil.isBlank(dto.getPlotId())) {
            return "The plot ID cannot be left blank.";
        }

        if (dto.getObservationDate() == null) {
            return "The observation date cannot be left blank.";
        }

        if (StrUtil.isBlank(dto.getGrowthStage())) {
            return "The growth stage cannot be left blank.";
        }

        if (StrUtil.isBlank(dto.getObserverId())) {
            return "The observer ID cannot be left blank.";
        }

        // 明细列表校验
        if (dto.getDetailList() == null || dto.getDetailList().isEmpty()) {
            return "At least one trait data must be added.";
        }

        // 校验每条明细数据
        for (int i = 0; i < dto.getDetailList().size(); i++) {
            AgronomicTraitDetailDTO detail = dto.getDetailList().get(i);
            if (StrUtil.isBlank(detail.getTraitCode())) {
                return String.format("The trait name of the %dth trait data cannot be left blank.", i + 1);
            }
            if (detail.getTraitValue() == null) {
                return String.format("The trait value of the %dth trait data cannot be left blank.", i + 1);
            }
        }

        return null;
    }

    /**
     * 将表单数据转换为EnvironmentNewData对象
     */
    private EnvironmentNewData convertToEnvironmentNewData(Map<String, Object> formData) {
        EnvironmentNewData environmentData = new EnvironmentNewData();

        // 基本信息
        environmentData.setEnvRecordId(getString(formData, "envRecordId"));
        environmentData.setPlotId(getString(formData, "plotId"));
        environmentData.setTrialId(getString(formData, "trialId"));
        environmentData.setBatchId(getString(formData, "batchId"));
        environmentData.setStationId(getString(formData, "stationId"));
        environmentData.setParameterCode(getString(formData, "parameterCode"));
        environmentData.setUnit(getString(formData, "unit"));
        environmentData.setSource(getString(formData, "source"));
        environmentData.setObserverId(getString(formData, "observerId"));
        environmentData.setRemark(getString(formData, "remark"));

        // 时间戳处理
        Object timestampObj = formData.get("timestamp");
        if (timestampObj != null) {
            try {
                if (timestampObj instanceof String) {
                    String dateStr = (String) timestampObj;
                    // 支持多种日期格式
                    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        environmentData.setTimestamp(sdf1.parse(dateStr));
                    } catch (Exception e) {
                        try {
                            environmentData.setTimestamp(sdf2.parse(dateStr));
                        } catch (Exception e2) {
                            try {
                                Date date = sdf3.parse(dateStr);
                                // 如果是只有日期，设置为当天的 00:00
                                environmentData.setTimestamp(date);
                            } catch (Exception e3) {
                                log.warn("Timestamp parsing failed: {}", dateStr, e3);
                            }
                        }
                    }
                } else if (timestampObj instanceof Number) {
                    // 时间戳
                    long timestamp = ((Number) timestampObj).longValue();
                    environmentData.setTimestamp(new Date(timestamp));
                }
            } catch (Exception e) {
                log.warn("Timestamp parsing failed: {}", timestampObj, e);
            }
        }

        // 数值处理
        Object valueObj = formData.get("value");
        if (valueObj != null) {
            try {
                if (valueObj instanceof Number) {
                    environmentData.setValue(new BigDecimal(valueObj.toString()));
                } else if (valueObj instanceof String) {
                    String valueStr = (String) valueObj;
                    if (StrUtil.isNotBlank(valueStr)) {
                        environmentData.setValue(new BigDecimal(valueStr));
                    }
                }
            } catch (Exception e) {
                log.warn("Numerical analysis failed: {}", valueObj, e);
            }
        }

        return environmentData;
    }

    /**
     * 校验环境监测数据
     */
    private String validateEnvironment(EnvironmentNewData environmentData) {
        // 必填字段校验
        if (StrUtil.isBlank(environmentData.getPlotId())) {
            return "The plot ID cannot be left blank.";
        }

        if (StrUtil.isBlank(environmentData.getStationId())) {
            return "The station ID cannot be left blank.";
        }

        if (environmentData.getTimestamp() == null) {
            return "The timestamp cannot be left blank.";
        }

        if (StrUtil.isBlank(environmentData.getParameterCode())) {
            return "The parameter code cannot be left blank.";
        }

        if (environmentData.getValue() == null) {
            return "The value cannot be left blank.";
        }

        if (StrUtil.isBlank(environmentData.getUnit())) {
            return "The unit cannot be left blank.";
        }

        return null;
    }

    /**
     * 将表单数据转换为BreedingYieldDataDTO对象
     */
    private BreedingYieldDataDTO convertToYieldDataDTO(Map<String, Object> formData) {
        BreedingYieldDataDTO dto = new BreedingYieldDataDTO();

        // 基本信息
        dto.setId(getString(formData, "id"));
        dto.setPlotId(getString(formData, "plotId"));
        dto.setBatchId(getString(formData, "batchId"));
        dto.setTrialId(getString(formData, "trialId"));
        dto.setInspectionType(getString(formData, "inspectionType"));
        dto.setScoreCode(getString(formData, "scoreCode"));
        dto.setRecorderName(getString(formData, "recorderName"));
        dto.setRemark(getString(formData, "remark"));
        dto.setStatus(getString(formData, "status"));

        // 检验日期处理
        Object inspectionDateObj = formData.get("inspectionDate");
        if (inspectionDateObj != null) {
            try {
                if (inspectionDateObj instanceof String) {
                    String dateStr = (String) inspectionDateObj;
                    // 支持 yyyy-MM-dd 格式
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    dto.setInspectionDate(LocalDate.parse(dateStr, formatter));
                }
            } catch (Exception e) {
                log.warn("Failed to parse the inspection date: {}", inspectionDateObj, e);
            }
        }

        // 评分值处理（可能是 number 或 string）
        Object scoreValueObj = formData.get("scoreValue");
        if (scoreValueObj != null) {
            if (scoreValueObj instanceof Number) {
                dto.setScoreValue(scoreValueObj.toString());
            } else if (scoreValueObj instanceof String) {
                dto.setScoreValue((String) scoreValueObj);
            } else {
                dto.setScoreValue(scoreValueObj.toString());
            }
        }

        return dto;
    }

    /**
     * 校验田间检验数据
     */
    private String validateYield(BreedingYieldDataDTO dto) {
        // 必填字段校验
        if (StrUtil.isBlank(dto.getPlotId())) {
            return "The plot ID cannot be left blank.";
        }

        if (dto.getInspectionDate() == null) {
            return "The inspection date cannot be left blank.";
        }

        if (StrUtil.isBlank(dto.getInspectionType())) {
            return "The inspection type cannot be left blank.";
        }

        if (StrUtil.isBlank(dto.getScoreCode())) {
            return "The score code cannot be left blank.";
        }

        if (StrUtil.isBlank(dto.getScoreValue())) {
            return "The score value cannot be left blank.";
        }

        if (StrUtil.isBlank(dto.getRecorderName())) {
            return "The recorder name cannot be left blank.";
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