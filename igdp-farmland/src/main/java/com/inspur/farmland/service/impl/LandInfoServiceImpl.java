package com.inspur.farmland.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
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
import java.util.*;

/**
 * 土地信息Service实现类
 *
 * @author inspur
 */
@Service
public class LandInfoServiceImpl implements ILandInfoService {

    @Autowired
    private LandInfoMapper landInfoMapper;

    @Autowired
    private FarmerInfoMapper farmerInfoMapper;

    @Autowired
    private IFarmerInfoService farmerInfoService;

    // 种子用量系数（kg/公顷）
    private static final BigDecimal SEED_COEFFICIENT = new BigDecimal("30");

    // 肥料用量系数（kg/公顷）
    private static final BigDecimal FERTILIZER_COEFFICIENT = new BigDecimal("100");

    @Override
    public List<LandInfo> selectLandInfoList(LandInfo landInfo) {
        LambdaQueryWrapper<LandInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LandInfo::getStatus, "1");

        // 条件查询
        if (StrUtil.isNotBlank(landInfo.getLandName())) {
            wrapper.like(LandInfo::getLandName, landInfo.getLandName());
        }
        if (StrUtil.isNotBlank(landInfo.getLandId())) {
            wrapper.eq(LandInfo::getLandId, landInfo.getLandId());
        }
        if (StrUtil.isNotBlank(landInfo.getFarmerId())) {
            wrapper.eq(LandInfo::getFarmerId, landInfo.getFarmerId());
        }
        if (StrUtil.isNotBlank(landInfo.getFarmerName())) {
            wrapper.like(LandInfo::getFarmerName, landInfo.getFarmerName());
        }
        if (StrUtil.isNotBlank(landInfo.getKebeleCode())) {
            wrapper.eq(LandInfo::getKebeleCode, landInfo.getKebeleCode());
        }
        if (StrUtil.isNotBlank(landInfo.getLandType())) {
            wrapper.eq(LandInfo::getLandType, landInfo.getLandType());
        }
        if (StrUtil.isNotBlank(landInfo.getCurrentStatus())) {
            wrapper.eq(LandInfo::getCurrentStatus, landInfo.getCurrentStatus());
        }
        if (StrUtil.isNotBlank(landInfo.getDaId())) {
            wrapper.eq(LandInfo::getDaId, landInfo.getDaId());
        }

        // 按创建时间倒序
        wrapper.orderByDesc(LandInfo::getCreateTime);

        return landInfoMapper.selectList(wrapper);
    }

    @Override
    public LandInfo selectLandInfoByLandId(String landId) {
        LambdaQueryWrapper<LandInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LandInfo::getLandId, landId)
               .eq(LandInfo::getStatus, "1");
        return landInfoMapper.selectOne(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String insertLandInfo(LandInfo landInfo) {
        // 生成土地编码
        String landId = "LD" + IdUtil.getSnowflakeNextIdStr();
        landInfo.setLandId(landId);

        // 计算种子和肥料的估算量
        calculateMaxAmounts(landInfo);

        // 设置默认值
        landInfo.setStatus("1");
        if (StrUtil.isBlank(landInfo.getAreaUnit())) {
            landInfo.setAreaUnit("HECTARE");
        }
        if (StrUtil.isBlank(landInfo.getCurrentStatus())) {
            landInfo.setCurrentStatus("IDLE");
        }

        // 设置创建信息
        try {
            String username = SecurityUtils.getUsername();
            landInfo.setCreateBy(username);
        } catch (Exception e) {
            landInfo.setCreateBy("system");
        }

        // 如果关联了农民，填充农民信息
        if (StrUtil.isNotBlank(landInfo.getFarmerId())) {
            fillFarmerInfo(landInfo);
        }

        // 插入数据
        landInfoMapper.insert(landInfo);

        // 更新农民统计信息
        if (StrUtil.isNotBlank(landInfo.getFarmerId())) {
            farmerInfoService.updateLandStatistics(landInfo.getFarmerId());
        }

        return landId;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateLandInfo(LandInfo landInfo) {
        // 保存landId用于WHERE条件
        String landId = landInfo.getLandId();

        // 查询原土地信息
        LandInfo oldLand = selectLandInfoByLandId(landId);
        if (oldLand == null) {
            return 0;
        }

        // 重新计算种子和肥料估算量
        if (landInfo.getAreaSize() != null) {
            calculateMaxAmounts(landInfo);
        }

        // 设置更新信息
        try {
            String username = SecurityUtils.getUsername();
            landInfo.setUpdateBy(username);
        } catch (Exception e) {
            landInfo.setUpdateBy("system");
        }

        // 不允许直接修改土地编码
        landInfo.setLandId(null);

        // 更新数据
        LambdaUpdateWrapper<LandInfo> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(LandInfo::getLandId, landId)
               .eq(LandInfo::getStatus, "1");

        int rows = landInfoMapper.update(landInfo, wrapper);

        // 如果农民发生变化，更新新旧农民的统计信息
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
        // 查询土地信息
        LandInfo landInfo = selectLandInfoByLandId(landId);
        if (landInfo == null) {
            return 0;
        }

        // 逻辑删除
        LambdaUpdateWrapper<LandInfo> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(LandInfo::getLandId, landId)
               .eq(LandInfo::getStatus, "1")
               .set(LandInfo::getStatus, "0");

        try {
            String username = SecurityUtils.getUsername();
            wrapper.set(LandInfo::getUpdateBy, username);
        } catch (Exception e) {
            wrapper.set(LandInfo::getUpdateBy, "system");
        }

        int rows = landInfoMapper.update(null, wrapper);

        // 更新农民统计信息
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
        // 查询农民信息
        LambdaQueryWrapper<FarmerInfo> farmerWrapper = new LambdaQueryWrapper<>();
        farmerWrapper.eq(FarmerInfo::getFarmerId, farmerId)
                     .eq(FarmerInfo::getStatus, "1");
        FarmerInfo farmer = farmerInfoMapper.selectOne(farmerWrapper);

        if (farmer == null) {
            throw new RuntimeException("农民信息不存在");
        }

        // 更新土地关联信息
        LambdaUpdateWrapper<LandInfo> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(LandInfo::getLandId, landId)
               .eq(LandInfo::getStatus, "1")
               .set(LandInfo::getFarmerId, farmerId)
               .set(LandInfo::getFarmerName, farmer.getFarmerName())
               .set(LandInfo::getFarmerIdCard, farmer.getIdCard())
               .set(LandInfo::getFarmerPhone, farmer.getPhone());

        try {
            String username = SecurityUtils.getUsername();
            wrapper.set(LandInfo::getUpdateBy, username);
        } catch (Exception e) {
            wrapper.set(LandInfo::getUpdateBy, "system");
        }

        int rows = landInfoMapper.update(null, wrapper);

        // 更新农民统计信息
        if (rows > 0) {
            farmerInfoService.updateLandStatistics(farmerId);
        }

        return rows;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int unbindFarmer(String landId) {
        // 查询土地信息
        LandInfo landInfo = selectLandInfoByLandId(landId);
        if (landInfo == null) {
            return 0;
        }

        String oldFarmerId = landInfo.getFarmerId();

        // 解除关联
        LambdaUpdateWrapper<LandInfo> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(LandInfo::getLandId, landId)
               .eq(LandInfo::getStatus, "1")
               .set(LandInfo::getFarmerId, null)
               .set(LandInfo::getFarmerName, null)
               .set(LandInfo::getFarmerIdCard, null)
               .set(LandInfo::getFarmerPhone, null);

        try {
            String username = SecurityUtils.getUsername();
            wrapper.set(LandInfo::getUpdateBy, username);
        } catch (Exception e) {
            wrapper.set(LandInfo::getUpdateBy, "system");
        }

        int rows = landInfoMapper.update(null, wrapper);

        // 更新农民统计信息
        if (rows > 0 && StrUtil.isNotBlank(oldFarmerId)) {
            farmerInfoService.updateLandStatistics(oldFarmerId);
        }

        return rows;
    }

    @Override
    public List<LandInfo> selectLandListByFarmerId(String farmerId) {
        LambdaQueryWrapper<LandInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LandInfo::getFarmerId, farmerId)
               .eq(LandInfo::getStatus, "1")
               .orderByDesc(LandInfo::getCreateTime);
        return landInfoMapper.selectList(wrapper);
    }

    @Override
    public Map<String, Object> getLandStatistics(String kebeleCode, String woredaCode, String zoneCode) {
        LambdaQueryWrapper<LandInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LandInfo::getStatus, "1");

        // 按行政区划筛选
        if (StrUtil.isNotBlank(kebeleCode)) {
            wrapper.eq(LandInfo::getKebeleCode, kebeleCode);
        } else if (StrUtil.isNotBlank(woredaCode)) {
            wrapper.eq(LandInfo::getWoredaCode, woredaCode);
        } else if (StrUtil.isNotBlank(zoneCode)) {
            wrapper.eq(LandInfo::getZoneCode, zoneCode);
        }

        List<LandInfo> allLands = landInfoMapper.selectList(wrapper);

        // 总体统计
        int totalCount = allLands.size();
        BigDecimal totalArea = allLands.stream()
                .map(LandInfo::getAreaSize)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 按地块类型统计
        Map<String, Map<String, Object>> byTypeMap = new HashMap<>();
        for (LandInfo land : allLands) {
            String type = land.getLandType();
            byTypeMap.putIfAbsent(type, new HashMap<String, Object>() {{
                put("type", type);
                put("count", 0);
                put("area", BigDecimal.ZERO);
            }});

            Map<String, Object> typeStats = byTypeMap.get(type);
            typeStats.put("count", (Integer) typeStats.get("count") + 1);
            typeStats.put("area", ((BigDecimal) typeStats.get("area")).add(land.getAreaSize()));
        }

        // 按状态统计
        Map<String, Map<String, Object>> byStatusMap = new HashMap<>();
        for (LandInfo land : allLands) {
            String status = land.getCurrentStatus();
            byStatusMap.putIfAbsent(status, new HashMap<String, Object>() {{
                put("status", status);
                put("count", 0);
                put("area", BigDecimal.ZERO);
            }});

            Map<String, Object> statusStats = byStatusMap.get(status);
            statusStats.put("count", (Integer) statusStats.get("count") + 1);
            statusStats.put("area", ((BigDecimal) statusStats.get("area")).add(land.getAreaSize()));
        }

        // 封装结果
        Map<String, Object> result = new HashMap<>();
        result.put("totalCount", totalCount);
        result.put("totalArea", totalArea);
        result.put("byLandType", new ArrayList<>(byTypeMap.values()));
        result.put("byStatus", new ArrayList<>(byStatusMap.values()));

        return result;
    }

    /**
     * 计算种子和肥料的最大用量
     */
    private void calculateMaxAmounts(LandInfo landInfo) {
        if (landInfo.getAreaSize() != null) {
            BigDecimal area = landInfo.getAreaSize();
            landInfo.setMaxSeedAmount(area.multiply(SEED_COEFFICIENT));
            landInfo.setMaxFertilizerAmount(area.multiply(FERTILIZER_COEFFICIENT));
        }
    }

    /**
     * 填充农民信息
     */
    private void fillFarmerInfo(LandInfo landInfo) {
        LambdaQueryWrapper<FarmerInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FarmerInfo::getFarmerId, landInfo.getFarmerId())
               .eq(FarmerInfo::getStatus, "1");
        FarmerInfo farmer = farmerInfoMapper.selectOne(wrapper);

        if (farmer != null) {
            landInfo.setFarmerName(farmer.getFarmerName());
            landInfo.setFarmerIdCard(farmer.getIdCard());
            landInfo.setFarmerPhone(farmer.getPhone());
        }
    }
}
