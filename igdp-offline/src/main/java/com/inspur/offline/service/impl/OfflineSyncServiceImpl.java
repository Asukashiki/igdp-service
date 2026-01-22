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
import com.inspur.seed.domain.dto.FarmerDemandAddDTO;
import com.inspur.seed.domain.entity.C1BreedingTracking;
import com.inspur.seed.domain.vo.FarmerDemandDetailVO;
import com.inspur.seed.service.IC1BreedingTrackingService;
import com.inspur.seed.service.IFarmerDemandService;
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
    private IAgronomicTraitRecordService agronomicTraitRecordService;

    @Autowired
    private IEnvironmentNewDataService environmentNewDataService;

    @Autowired
    private IBreedingYieldDataService breedingYieldDataService;

    @Autowired
    private IC1BreedingTrackingService c1BreedingTrackingService;

    @Autowired
    private IFarmerDemandService farmerDemandService;

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
    public AjaxResult syncTrait(OfflineSyncRequest request) {
        try {
            Map<String, Object> formData = request.getFormData();
            if (formData == null || formData.isEmpty()) {
                return AjaxResult.error("表单数据不能为空");
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

            log.info("农艺性状数据同步成功: recordId={}, plotId={}", recordId, dto.getPlotId());
            return AjaxResult.success("同步成功", result);

        } catch (Exception e) {
            log.error("同步农艺性状数据失败", e);
            return AjaxResult.error("同步失败: " + e.getMessage());
        }
    }

    @Override
    public AjaxResult syncEnvironment(OfflineSyncRequest request) {
        try {
            Map<String, Object> formData = request.getFormData();
            if (formData == null || formData.isEmpty()) {
                return AjaxResult.error("表单数据不能为空");
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

            log.info("环境监测数据同步成功: envRecordId={}, stationId={}", envRecordId, environmentData.getStationId());
            return AjaxResult.success("同步成功", result);

        } catch (Exception e) {
            log.error("同步环境监测数据失败", e);
            return AjaxResult.error("同步失败: " + e.getMessage());
        }
    }

    @Override
    public AjaxResult syncYield(OfflineSyncRequest request) {
        try {
            Map<String, Object> formData = request.getFormData();
            if (formData == null || formData.isEmpty()) {
                return AjaxResult.error("表单数据不能为空");
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

            log.info("田间检验数据同步成功: plotId={}, inspectionType={}", dto.getPlotId(), dto.getInspectionType());
            return AjaxResult.success("同步成功", resultMap);

        } catch (Exception e) {
            log.error("同步田间检验数据失败", e);
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
                            log.warn("日期解析失败: {}", dateStr, e2);
                        }
                    }
                } else if (observationDateObj instanceof Number) {
                    // 时间戳
                    long timestamp = ((Number) observationDateObj).longValue();
                    dto.setObservationDate(new Date(timestamp));
                }
            } catch (Exception e) {
                log.warn("日期解析失败: {}", observationDateObj, e);
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
                        log.warn("性状值解析失败: {}", traitValueObj, e);
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
                        log.warn("排序序号解析失败: {}", sortOrderObj, e);
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
            return "地块ID不能为空";
        }

        if (dto.getObservationDate() == null) {
            return "观测日期不能为空";
        }

        if (StrUtil.isBlank(dto.getGrowthStage())) {
            return "生长阶段不能为空";
        }

        if (StrUtil.isBlank(dto.getObserverId())) {
            return "观测员不能为空";
        }

        // 明细列表校验
        if (dto.getDetailList() == null || dto.getDetailList().isEmpty()) {
            return "至少需要添加一条性状数据";
        }

        // 校验每条明细数据
        for (int i = 0; i < dto.getDetailList().size(); i++) {
            AgronomicTraitDetailDTO detail = dto.getDetailList().get(i);
            if (StrUtil.isBlank(detail.getTraitCode())) {
                return String.format("第%d条性状数据的性状名称不能为空", i + 1);
            }
            if (detail.getTraitValue() == null) {
                return String.format("第%d条性状数据的性状值不能为空", i + 1);
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
                                log.warn("日期解析失败: {}", dateStr, e3);
                            }
                        }
                    }
                } else if (timestampObj instanceof Number) {
                    // 时间戳
                    long timestamp = ((Number) timestampObj).longValue();
                    environmentData.setTimestamp(new Date(timestamp));
                }
            } catch (Exception e) {
                log.warn("日期解析失败: {}", timestampObj, e);
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
                log.warn("数值解析失败: {}", valueObj, e);
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
            return "地块ID不能为空";
        }

        if (StrUtil.isBlank(environmentData.getStationId())) {
            return "气象站ID不能为空";
        }

        if (environmentData.getTimestamp() == null) {
            return "采集时间不能为空";
        }

        if (StrUtil.isBlank(environmentData.getParameterCode())) {
            return "参数代码不能为空";
        }

        if (environmentData.getValue() == null) {
            return "数值不能为空";
        }

        if (StrUtil.isBlank(environmentData.getUnit())) {
            return "单位不能为空";
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
                log.warn("检验日期解析失败: {}", inspectionDateObj, e);
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
            return "地块ID不能为空";
        }

        if (dto.getInspectionDate() == null) {
            return "检验日期不能为空";
        }

        if (StrUtil.isBlank(dto.getInspectionType())) {
            return "检验类型不能为空";
        }

        if (StrUtil.isBlank(dto.getScoreCode())) {
            return "评分代码不能为空";
        }

        if (StrUtil.isBlank(dto.getScoreValue())) {
            return "评分值不能为空";
        }

        if (StrUtil.isBlank(dto.getRecorderName())) {
            return "记录人员不能为空";
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

    @Override
    public AjaxResult syncC1BreedingTracking(OfflineSyncRequest request) {
        try {
            Map<String, Object> formData = request.getFormData();
            if (formData == null || formData.isEmpty()) {
                return AjaxResult.error("表单数据不能为空");
            }

            // 转换为C1BreedingTracking对象
            C1BreedingTracking trackingData = convertToC1BreedingTracking(formData);

            // 数据校验
            String validationError = validateC1BreedingTracking(trackingData);
            if (StrUtil.isNotBlank(validationError)) {
                return AjaxResult.error(validationError);
            }

            // 离线数据的ID是手动输入的，需要判断是否已存在防止重复提交
            String offlineId = getString(formData, "id");
            String resultId;

            if (StrUtil.isNotBlank(offlineId)) {
                // 检查数据库中是否已存在该ID
                C1BreedingTracking existingRecord = c1BreedingTrackingService.getDetailById(offlineId);
                if (existingRecord != null) {
                    // 已存在则返回已同步的信息，避免重复提交
                    log.info("繁育田间检测数据已存在，跳过同步: id={}", offlineId);
                    Map<String, Object> result = new java.util.HashMap<>();
                    result.put("id", offlineId);
                    result.put("trackingId", existingRecord.getTrackingId());
                    result.put("alreadyExists", true);
                    return AjaxResult.success("数据已同步", result);
                }
                // 不存在则使用离线ID进行新增
                trackingData.setId(offlineId);
            }

            // 新增数据
            boolean addResult = c1BreedingTrackingService.add(trackingData);
            if (!addResult) {
                return AjaxResult.error("新增繁育田间检测数据失败");
            }
            resultId = trackingData.getId();
            log.info("繁育田间检测数据同步成功: id={}, trackingId={}", resultId, trackingData.getTrackingId());

            // 返回结果
            Map<String, Object> result = new java.util.HashMap<>();
            result.put("id", resultId);
            result.put("trackingId", trackingData.getTrackingId());
            result.put("alreadyExists", false);

            return AjaxResult.success("同步成功", result);

        } catch (Exception e) {
            log.error("同步繁育田间检测数据失败", e);
            return AjaxResult.error("同步失败: " + e.getMessage());
        }
    }

    /**
     * 将表单数据转换为C1BreedingTracking对象
     */
    private C1BreedingTracking convertToC1BreedingTracking(Map<String, Object> formData) {
        C1BreedingTracking tracking = new C1BreedingTracking();

        // 基本信息
        tracking.setTrackingId(getString(formData, "trackingId"));
        tracking.setBatchId(getString(formData, "batchId"));
        tracking.setStageName(getString(formData, "stageName"));
        tracking.setLocation(getString(formData, "location"));
        tracking.setTrackingResult(getString(formData, "trackingResult"));
        tracking.setTrackingDesc(getString(formData, "trackingDesc"));

        // 种子相关
        tracking.setSeedClass(getString(formData, "seedClass"));
        tracking.setLotId(getString(formData, "lotId"));
        tracking.setStage(getString(formData, "stage"));
        tracking.setScore(getString(formData, "score"));
        tracking.setInspectionValue(getString(formData, "inspectionValue"));

        // 测试次数
        Object testCountObj = formData.get("testCount");
        if (testCountObj != null) {
            try {
                if (testCountObj instanceof Number) {
                    tracking.setTestCount(((Number) testCountObj).intValue());
                } else if (testCountObj instanceof String) {
                    String testCountStr = (String) testCountObj;
                    if (StrUtil.isNotBlank(testCountStr)) {
                        tracking.setTestCount(Integer.parseInt(testCountStr));
                    }
                }
            } catch (Exception e) {
                log.warn("测试次数解析失败: {}", testCountObj, e);
            }
        }

        tracking.setOperator(getString(formData, "operator"));
        tracking.setCreatedBy(getString(formData, "createdBy"));
        tracking.setUpdatedBy(getString(formData, "updatedBy"));

        // 日期处理
        Object startDateObj = formData.get("startDate");
        if (startDateObj != null) {
            try {
                if (startDateObj instanceof String) {
                    String dateStr = (String) startDateObj;
                    if (dateStr.length() >= 10) {
                        dateStr = dateStr.substring(0, 10);
                        tracking.setStartDate(LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                    }
                }
            } catch (Exception e) {
                log.warn("开始日期解析失败: {}", startDateObj, e);
            }
        }

        Object endDateObj = formData.get("endDate");
        if (endDateObj != null) {
            try {
                if (endDateObj instanceof String) {
                    String dateStr = (String) endDateObj;
                    if (dateStr.length() >= 10) {
                        dateStr = dateStr.substring(0, 10);
                        tracking.setEndDate(LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                    }
                }
            } catch (Exception e) {
                log.warn("结束日期解析失败: {}", endDateObj, e);
            }
        }

        return tracking;
    }

    /**
     * 校验繁育田间检测数据
     */
    private String validateC1BreedingTracking(C1BreedingTracking tracking) {
        // 批次ID必填校验
        if (StrUtil.isBlank(tracking.getBatchId())) {
            return "批次ID不能为空";
        }

        // 种子级别必填校验
        if (StrUtil.isBlank(tracking.getSeedClass())) {
            return "种子级别不能为空";
        }

        // 检测阶段必填校验
        if (StrUtil.isBlank(tracking.getStage())) {
            return "检测阶段不能为空";
        }

        return null;
    }

    @Override
    public AjaxResult syncFarmerDemand(OfflineSyncRequest request) {
        try {
            Map<String, Object> formData = request.getFormData();
            if (formData == null || formData.isEmpty()) {
                return AjaxResult.error("表单数据不能为空");
            }

            // 转换为FarmerDemandAddDTO对象
            FarmerDemandAddDTO dto = convertToFarmerDemandAddDTO(formData);

            // 数据校验
            String validationError = validateFarmerDemand(dto);
            if (StrUtil.isNotBlank(validationError)) {
                return AjaxResult.error(validationError);
            }

            // 离线数据的ID是手动输入的，需要判断是否已存在防止重复提交
            String offlineId = getString(formData, "id");

            if (StrUtil.isNotBlank(offlineId)) {
                // 检查数据库中是否已存在该ID
                FarmerDemandDetailVO existingRecord = farmerDemandService.getFarmerDemandDetail(offlineId);
                if (existingRecord != null) {
                    // 已存在则返回已同步的信息，避免重复提交
                    log.info("农户需求数据已存在，跳过同步: id={}", offlineId);
                    Map<String, Object> result = new java.util.HashMap<>();
                    result.put("id", offlineId);
                    result.put("alreadyExists", true);
                    return AjaxResult.success("数据已同步", result);
                }
            }

            // 新增数据 - 使用服务层的addFarmerDemand方法
            String demandId = farmerDemandService.addFarmerDemand(dto);
            
            // 检查是否因为重复年份而失败
            if ("1".equals(demandId)) {
                return AjaxResult.error("该农户在当前年份已存在需求数据");
            }
            
            log.info("农户需求数据同步成功: id={}, farmerId={}", demandId, dto.getFarmerId());

            // 返回结果
            Map<String, Object> result = new java.util.HashMap<>();
            result.put("id", demandId);
            result.put("alreadyExists", false);

            return AjaxResult.success("同步成功", result);

        } catch (Exception e) {
            log.error("同步农户需求数据失败", e);
            return AjaxResult.error("同步失败: " + e.getMessage());
        }
    }

    /**
     * 将表单数据转换为FarmerDemandAddDTO对象
     */
    @SuppressWarnings("unchecked")
    private FarmerDemandAddDTO convertToFarmerDemandAddDTO(Map<String, Object> formData) {
        FarmerDemandAddDTO dto = new FarmerDemandAddDTO();

        // 基本信息
        dto.setBatchId(getString(formData, "batchId"));
        dto.setFarmerId(getString(formData, "farmerId"));
        dto.setFarmerName(getString(formData, "farmerName"));
        dto.setFarmerIdNumber(getString(formData, "farmerIdNumber"));

        // 地区信息
        dto.setRegion(getString(formData, "region"));
        dto.setZone(getString(formData, "zone"));
        dto.setWoreda(getString(formData, "woreda"));
        dto.setKebele(getString(formData, "kebele"));
        dto.setZoneName(getString(formData, "zoneName"));
        dto.setWoredaName(getString(formData, "woredaName"));
        dto.setKebeleName(getString(formData, "kebeleName"));
        dto.setVillage(getString(formData, "village"));

        // 土地面积
        Object landAreaObj = formData.get("landArea");
        if (landAreaObj != null) {
            try {
                if (landAreaObj instanceof Number) {
                    dto.setLandArea(new BigDecimal(landAreaObj.toString()));
                } else if (landAreaObj instanceof String) {
                    String areaStr = (String) landAreaObj;
                    if (StrUtil.isNotBlank(areaStr)) {
                        dto.setLandArea(new BigDecimal(areaStr));
                    }
                }
            } catch (Exception e) {
                log.warn("土地面积解析失败: {}", landAreaObj, e);
            }
        }

        // 其他信息
        dto.setYear(getString(formData, "year"));
        dto.setRemark(getString(formData, "remark"));
        dto.setDaUserId(getString(formData, "daUserId"));
        dto.setDaUserName(getString(formData, "daUserName"));
        dto.setCreateBy(getString(formData, "createBy"));

        // 处理inputItems嵌套列表
        Object inputItemsObj = formData.get("inputItems");
        if (inputItemsObj instanceof List) {
            List<Map<String, Object>> itemsList = (List<Map<String, Object>>) inputItemsObj;
            List<FarmerDemandAddDTO.InputItemDTO> inputItems = new ArrayList<>();
            
            for (Map<String, Object> itemMap : itemsList) {
                FarmerDemandAddDTO.InputItemDTO item = new FarmerDemandAddDTO.InputItemDTO();
                item.setInputCategory(getString(itemMap, "inputCategory"));
                item.setInputType(getString(itemMap, "inputType"));
                item.setVariety(getString(itemMap, "variety"));
                item.setSpecification(getString(itemMap, "specification"));
                item.setUnit(getString(itemMap, "unit"));
                item.setYear(getString(itemMap, "year"));
                item.setSeason(getString(itemMap, "season"));

                // 数量
                Object quantityObj = itemMap.get("quantity");
                if (quantityObj != null) {
                    try {
                        if (quantityObj instanceof Number) {
                            item.setQuantity(new BigDecimal(quantityObj.toString()));
                        } else if (quantityObj instanceof String) {
                            String quantityStr = (String) quantityObj;
                            if (StrUtil.isNotBlank(quantityStr)) {
                                item.setQuantity(new BigDecimal(quantityStr));
                            }
                        }
                    } catch (Exception e) {
                        log.warn("数量解析失败: {}", quantityObj, e);
                    }
                }

                // 作物用地面积
                Object cropLandObj = itemMap.get("cropLand");
                if (cropLandObj != null) {
                    try {
                        if (cropLandObj instanceof Number) {
                            item.setCropLand(new BigDecimal(cropLandObj.toString()));
                        } else if (cropLandObj instanceof String) {
                            String cropLandStr = (String) cropLandObj;
                            if (StrUtil.isNotBlank(cropLandStr)) {
                                item.setCropLand(new BigDecimal(cropLandStr));
                            }
                        }
                    } catch (Exception e) {
                        log.warn("作物用地面积解析失败: {}", cropLandObj, e);
                    }
                }

                // 化肥用量
                Object fertilizerAmountObj = itemMap.get("fertilizerAmount");
                if (fertilizerAmountObj != null) {
                    try {
                        if (fertilizerAmountObj instanceof Number) {
                            item.setFertilizerAmount(((Number) fertilizerAmountObj).doubleValue());
                        } else if (fertilizerAmountObj instanceof String) {
                            String fertilizerAmountStr = (String) fertilizerAmountObj;
                            if (StrUtil.isNotBlank(fertilizerAmountStr)) {
                                item.setFertilizerAmount(Double.parseDouble(fertilizerAmountStr));
                            }
                        }
                    } catch (Exception e) {
                        log.warn("化肥用量解析失败: {}", fertilizerAmountObj, e);
                    }
                }

                inputItems.add(item);
            }
            dto.setInputItems(inputItems);
        }

        return dto;
    }

    /**
     * 校验农户需求数据
     */
    private String validateFarmerDemand(FarmerDemandAddDTO dto) {
        // 农户ID必填校验
        if (StrUtil.isBlank(dto.getFarmerId())) {
            return "农户ID不能为空";
        }

        // 农户姓名必填校验
        if (StrUtil.isBlank(dto.getFarmerName())) {
            return "农户姓名不能为空";
        }

        // 农户身份证号必填校验
        if (StrUtil.isBlank(dto.getFarmerIdNumber())) {
            return "农户身份证号不能为空";
        }

        // Woreda必填校验
        if (StrUtil.isBlank(dto.getWoreda())) {
            return "Woreda不能为空";
        }

        // Kebele必填校验
        if (StrUtil.isBlank(dto.getKebele())) {
            return "Kebele不能为空";
        }

        // 需求项目必填校验
        if (dto.getInputItems() == null || dto.getInputItems().isEmpty()) {
            return "需求项目不能为空";
        }

        // 校验每个需求项目
        for (FarmerDemandAddDTO.InputItemDTO item : dto.getInputItems()) {
            if (StrUtil.isBlank(item.getInputCategory())) {
                return "投入品类目不能为空";
            }
            if (StrUtil.isBlank(item.getInputType())) {
                return "投入品类型不能为空";
            }
            if (StrUtil.isBlank(item.getVariety())) {
                return "品种不能为空";
            }
            if (StrUtil.isBlank(item.getUnit())) {
                return "单位不能为空";
            }
            if (item.getQuantity() == null) {
                return "数量不能为空";
            }
        }

        return null;
    }
}
