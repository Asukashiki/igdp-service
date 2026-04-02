package com.inspur.farmland.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.inspur.common.annotation.DataSource;
import com.inspur.common.enums.DataSourceType;
import com.inspur.common.utils.SecurityUtils;
import com.inspur.farmland.domain.FarmerInfo;
import com.inspur.farmland.domain.LandInfo;
import com.inspur.farmland.mapper.FarmerInfoMapper;
import com.inspur.farmland.mapper.LandInfoMapper;
import com.inspur.farmland.service.IFarmerInfoService;
import com.inspur.farmland.service.ILandInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 农田信息Service实现类
 */
@Service
@DataSource(DataSourceType.IST_WEB)
public class LandInfoServiceImpl implements ILandInfoService {

    @Autowired
    private LandInfoMapper landInfoMapper;

    @Autowired
    private FarmerInfoMapper farmerInfoMapper;

    @Autowired
    private IFarmerInfoService farmerInfoService;

    @Override
    public List<LandInfo> selectLandInfoList(LandInfo landInfo) {
        LambdaQueryWrapper<LandInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LandInfo::getDelFlag, 0);

        if (StrUtil.isNotBlank(landInfo.getSearchValue())) {
            wrapper.and(w -> w.like(LandInfo::getFarmerId, landInfo.getSearchValue())
                .or().like(LandInfo::getKebeleId, landInfo.getSearchValue())
                .or().like(LandInfo::getId, landInfo.getSearchValue()));
        }
        if (landInfo.getId() != null) {
            wrapper.eq(LandInfo::getId, landInfo.getId());
        }
        if (StrUtil.isNotBlank(landInfo.getLandId())) {
            wrapper.eq(LandInfo::getId, Long.valueOf(landInfo.getLandId()));
        }
        if (StrUtil.isNotBlank(landInfo.getFarmerId())) {
            wrapper.eq(LandInfo::getFarmerId, landInfo.getFarmerId());
        }
        if (StrUtil.isNotBlank(landInfo.getKebeleId())) {
            wrapper.eq(LandInfo::getKebeleId, landInfo.getKebeleId());
        }
        if (StrUtil.isNotBlank(landInfo.getKebeleCode())) {
            wrapper.eq(LandInfo::getKebeleId, landInfo.getKebeleCode());
        }
        if (StrUtil.isNotBlank(landInfo.getStatus())) {
            wrapper.eq(LandInfo::getStatus, landInfo.getStatus());
        }
        if (StrUtil.isNotBlank(landInfo.getSoilCode())) {
            wrapper.eq(LandInfo::getSoilCode, landInfo.getSoilCode());
        }
        if (StrUtil.isNotBlank(landInfo.getIrrigationCode())) {
            wrapper.eq(LandInfo::getIrrigationCode, landInfo.getIrrigationCode());
        }

        wrapper.orderByDesc(LandInfo::getCreateTime);
        return landInfoMapper.selectList(wrapper);
    }

    @Override
    public LandInfo selectLandInfoByLandId(String landId) {
        if (StrUtil.isBlank(landId)) {
            return null;
        }
        LambdaQueryWrapper<LandInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LandInfo::getId, Long.valueOf(landId))
            .eq(LandInfo::getDelFlag, 0);
        return landInfoMapper.selectOne(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String insertLandInfo(LandInfo landInfo) {
        fillDefaultFieldsForInsert(landInfo);
        landInfoMapper.insert(landInfo);
        if (StrUtil.isNotBlank(landInfo.getFarmerId())) {
            farmerInfoService.updateLandStatistics(landInfo.getFarmerId());
        }
        return String.valueOf(landInfo.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateLandInfo(LandInfo landInfo) {
        String landId = landInfo.getLandId();
        LandInfo oldLand = selectLandInfoByLandId(landId);
        if (oldLand == null) {
            return 0;
        }

        fillDefaultFieldsForUpdate(landInfo);
        landInfo.setId(null);
        landInfo.setCreateBy(null);
        landInfo.setCreateTime(null);
        landInfo.setCreateDept(null);
        landInfo.setDelFlag(null);
        landInfo.setTenantId(null);

        LambdaUpdateWrapper<LandInfo> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(LandInfo::getId, Long.valueOf(landId))
            .eq(LandInfo::getDelFlag, 0);

        int rows = landInfoMapper.update(landInfo, wrapper);

        String oldFarmerId = oldLand.getFarmerId();
        String newFarmerId = landInfo.getFarmerId();
        if (!Objects.equals(oldFarmerId, newFarmerId)) {
            if (StrUtil.isNotBlank(oldFarmerId)) {
                farmerInfoService.updateLandStatistics(oldFarmerId);
            }
            if (StrUtil.isNotBlank(newFarmerId)) {
                farmerInfoService.updateLandStatistics(newFarmerId);
            }
        }
        return rows;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteLandInfoByLandId(String landId) {
        LandInfo landInfo = selectLandInfoByLandId(landId);
        if (landInfo == null) {
            return 0;
        }

        LambdaUpdateWrapper<LandInfo> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(LandInfo::getId, Long.valueOf(landId))
            .eq(LandInfo::getDelFlag, 0)
            .set(LandInfo::getDelFlag, 1)
            .set(LandInfo::getUpdateTime, LocalDateTime.now());

        Long userId = getCurrentUserId();
        if (userId != null) {
            wrapper.set(LandInfo::getUpdateBy, String.valueOf(userId));
        }

        int rows = landInfoMapper.update(null, wrapper);
        if (rows > 0 && StrUtil.isNotBlank(landInfo.getFarmerId())) {
            farmerInfoService.updateLandStatistics(landInfo.getFarmerId());
        }
        return rows;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Integer> deleteLandInfoByIds(String[] landIds) {
        int successCount = 0;
        int failCount = 0;
        for (String landId : landIds) {
            try {
                int rows = deleteLandInfoByLandId(landId);
                if (rows > 0) {
                    successCount++;
                } else {
                    failCount++;
                }
            } catch (Exception e) {
                failCount++;
            }
        }

        Map<String, Integer> result = new HashMap<>();
        result.put("successCount", successCount);
        result.put("failCount", failCount);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int bindFarmer(String landId, String farmerId) {
        LambdaQueryWrapper<FarmerInfo> farmerWrapper = new LambdaQueryWrapper<>();
        farmerWrapper.eq(FarmerInfo::getFarmerId, farmerId)
            .eq(FarmerInfo::getStatus, "1");
        FarmerInfo farmer = farmerInfoMapper.selectOne(farmerWrapper);
        if (farmer == null) {
            throw new RuntimeException("农民信息不存在");
        }

        LambdaUpdateWrapper<LandInfo> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(LandInfo::getId, Long.valueOf(landId))
            .eq(LandInfo::getDelFlag, 0)
            .set(LandInfo::getFarmerId, farmerId)
            .set(LandInfo::getUpdateTime, LocalDateTime.now());

        Long userId = getCurrentUserId();
        if (userId != null) {
            wrapper.set(LandInfo::getUpdateBy, String.valueOf(userId));
        }

        int rows = landInfoMapper.update(null, wrapper);
        if (rows > 0) {
            farmerInfoService.updateLandStatistics(farmerId);
        }
        return rows;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int unbindFarmer(String landId) {
        LandInfo landInfo = selectLandInfoByLandId(landId);
        if (landInfo == null) {
            return 0;
        }

        String oldFarmerId = landInfo.getFarmerId();
        LambdaUpdateWrapper<LandInfo> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(LandInfo::getId, Long.valueOf(landId))
            .eq(LandInfo::getDelFlag, 0)
            .set(LandInfo::getFarmerId, "")
            .set(LandInfo::getUpdateTime, LocalDateTime.now());

        int rows = landInfoMapper.update(null, wrapper);
        if (rows > 0 && StrUtil.isNotBlank(oldFarmerId)) {
            farmerInfoService.updateLandStatistics(oldFarmerId);
        }
        return rows;
    }

    @Override
    public List<LandInfo> selectLandListByFarmerId(String farmerId) {
        LambdaQueryWrapper<LandInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LandInfo::getFarmerId, farmerId)
            .eq(LandInfo::getDelFlag, 0)
            .orderByDesc(LandInfo::getCreateTime);
        return landInfoMapper.selectList(wrapper);
    }

    @Override
    public Map<String, Object> getLandStatistics(String kebeleCode, String woredaCode, String zoneCode) {
        LambdaQueryWrapper<LandInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LandInfo::getDelFlag, 0);
        if (StrUtil.isNotBlank(kebeleCode)) {
            wrapper.eq(LandInfo::getKebeleId, kebeleCode);
        }

        List<LandInfo> allLands = landInfoMapper.selectList(wrapper);
        int totalCount = allLands.size();
        BigDecimal totalArea = allLands.stream()
            .map(LandInfo::getAreaTa)
            .filter(Objects::nonNull)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, Map<String, Object>> byStatusMap = new HashMap<>();
        for (LandInfo land : allLands) {
            String status = StrUtil.blankToDefault(land.getStatus(), "0");
            byStatusMap.putIfAbsent(status, new HashMap<String, Object>() {{
                put("status", status);
                put("count", 0);
                put("area", BigDecimal.ZERO);
            }});
            Map<String, Object> statusStats = byStatusMap.get(status);
            statusStats.put("count", (Integer) statusStats.get("count") + 1);
            statusStats.put("area", ((BigDecimal) statusStats.get("area")).add(defaultArea(land.getAreaTa())));
        }

        Map<String, Map<String, Object>> byIrrigationMap = new HashMap<>();
        for (LandInfo land : allLands) {
            String irrigationCode = StrUtil.blankToDefault(land.getIrrigationCode(), "");
            byIrrigationMap.putIfAbsent(irrigationCode, new HashMap<String, Object>() {{
                put("irrigationCode", irrigationCode);
                put("count", 0);
                put("area", BigDecimal.ZERO);
            }});
            Map<String, Object> irrigationStats = byIrrigationMap.get(irrigationCode);
            irrigationStats.put("count", (Integer) irrigationStats.get("count") + 1);
            irrigationStats.put("area", ((BigDecimal) irrigationStats.get("area")).add(defaultArea(land.getAreaTa())));
        }

        Map<String, Object> result = new HashMap<>();
        result.put("totalCount", totalCount);
        result.put("totalArea", totalArea);
        result.put("byStatus", new ArrayList<>(byStatusMap.values()));
        result.put("byIrrigation", new ArrayList<>(byIrrigationMap.values()));
        return result;
    }

    private void fillDefaultFieldsForInsert(LandInfo landInfo) {
        if (StrUtil.isBlank(landInfo.getStatus())) {
            landInfo.setStatus("0");
        }
        if (landInfo.getDelFlag() == null) {
            landInfo.setDelFlag(0);
        }
        if (StrUtil.isBlank(landInfo.getTenantId())) {
            landInfo.setTenantId("000000");
        }
        LocalDateTime now = LocalDateTime.now();
        landInfo.setCreateTime(now);
        landInfo.setUpdateTime(now);

        Long userId = getCurrentUserId();
        String operatorId = userId == null ? "0" : String.valueOf(userId);
        landInfo.setCreateBy(operatorId);
        landInfo.setUpdateBy(operatorId);

        Long deptId = getCurrentDeptId();
        landInfo.setCreateDept(deptId == null ? 0L : deptId);
    }

    private void fillDefaultFieldsForUpdate(LandInfo landInfo) {
        landInfo.setUpdateTime(LocalDateTime.now());
        Long userId = getCurrentUserId();
        landInfo.setUpdateBy(userId == null ? "0" : String.valueOf(userId));
    }

    private Long getCurrentUserId() {
        try {
            String userId = SecurityUtils.getUserId();
            return StrUtil.isBlank(userId) ? null : Long.valueOf(userId);
        } catch (Exception e) {
            return null;
        }
    }

    private Long getCurrentDeptId() {
        try {
            String deptId = SecurityUtils.getDeptId();
            return StrUtil.isBlank(deptId) ? null : Long.valueOf(deptId);
        } catch (Exception e) {
            return null;
        }
    }

    private BigDecimal defaultArea(BigDecimal area) {
        return area == null ? BigDecimal.ZERO : area;
    }
}
