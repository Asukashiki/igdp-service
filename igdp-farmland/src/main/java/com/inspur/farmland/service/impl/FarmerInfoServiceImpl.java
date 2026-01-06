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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 农民信息Service实现类
 *
 * @author inspur
 */
@Service
public class FarmerInfoServiceImpl implements com.inspur.farmland.service.IFarmerInfoService {

    @Autowired
    private FarmerInfoMapper farmerInfoMapper;

    @Autowired
    private LandInfoMapper landInfoMapper;

    @Override
    public List<FarmerInfo> selectFarmerInfoList(FarmerInfo farmerInfo) {
        LambdaQueryWrapper<FarmerInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FarmerInfo::getStatus, "1");

        // 条件查询
        // 关键字模糊查询 (同时匹配农民姓名、ID、电话)
        if (StrUtil.isNotBlank(farmerInfo.getSearchValue())) {
            wrapper.and(w -> w.like(FarmerInfo::getFarmerName, farmerInfo.getSearchValue())
                    .or().like(FarmerInfo::getIdCard, farmerInfo.getSearchValue())
                    .or().like(FarmerInfo::getPhone, farmerInfo.getSearchValue()));
        } else {
            if (StrUtil.isNotBlank(farmerInfo.getFarmerName())) {
                wrapper.like(FarmerInfo::getFarmerName, farmerInfo.getFarmerName());
            }
            if (StrUtil.isNotBlank(farmerInfo.getFarmerId())) {
                wrapper.eq(FarmerInfo::getFarmerId, farmerInfo.getFarmerId());
            }
            if (StrUtil.isNotBlank(farmerInfo.getIdCard())) {
                wrapper.eq(FarmerInfo::getIdCard, farmerInfo.getIdCard());
            }
            if (StrUtil.isNotBlank(farmerInfo.getGender())) {
                wrapper.eq(FarmerInfo::getGender, farmerInfo.getGender());
            }
            if (StrUtil.isNotBlank(farmerInfo.getPhone())) {
                wrapper.like(FarmerInfo::getPhone, farmerInfo.getPhone());
            }
        }
        if (StrUtil.isNotBlank(farmerInfo.getKebeleName())) {
            wrapper.like(FarmerInfo::getKebeleName, farmerInfo.getKebeleName());
        }
        if (StrUtil.isNotBlank(farmerInfo.getDaId())) {
            wrapper.eq(FarmerInfo::getDaId, farmerInfo.getDaId());
        }

        // 按创建时间倒序
        wrapper.orderByDesc(FarmerInfo::getCreateTime);

        return farmerInfoMapper.selectList(wrapper);
    }

    @Override
    public FarmerInfo selectFarmerInfoByFarmerId(String farmerId) {
        LambdaQueryWrapper<FarmerInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FarmerInfo::getFarmerId, farmerId)
                .eq(FarmerInfo::getStatus, "1");
        return farmerInfoMapper.selectOne(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String insertFarmerInfo(FarmerInfo farmerInfo) {
        // 生成农民编码
        String farmerId = "FM" + IdUtil.getSnowflakeNextIdStr();
        farmerInfo.setFarmerId(farmerId);

        // 设置默认值
        farmerInfo.setStatus("1");
        farmerInfo.setTotalLandArea(BigDecimal.ZERO);
        farmerInfo.setLandCount(0);

        // 设置创建信息
        try {
            String username = SecurityUtils.getUsername();
            farmerInfo.setCreateBy(username);
            farmerInfo.setUpdateBy(username);
            farmerInfo.setUpdateTime(LocalDateTime.now());
        } catch (Exception e) {
            farmerInfo.setCreateBy("system");
        }

        // 插入数据
        farmerInfoMapper.insert(farmerInfo);

        return farmerId;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateFarmerInfo(FarmerInfo farmerInfo) {
        // 保存farmerId用于WHERE条件
        String farmerId = farmerInfo.getFarmerId();

        // 设置更新信息
        try {
            String username = SecurityUtils.getUsername();
            farmerInfo.setUpdateBy(username);
        } catch (Exception e) {
            farmerInfo.setUpdateBy("system");
        }

        // 不允许修改统计字段
        farmerInfo.setFarmerId(null);
        farmerInfo.setTotalLandArea(null);
        farmerInfo.setLandCount(null);

        // 更新数据
        LambdaUpdateWrapper<FarmerInfo> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(FarmerInfo::getFarmerId, farmerId)
                .eq(FarmerInfo::getStatus, "1");

        return farmerInfoMapper.update(farmerInfo, wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteFarmerInfoByFarmerId(String farmerId) {
        // 解除关联的土地
        LambdaUpdateWrapper<LandInfo> landWrapper = new LambdaUpdateWrapper<>();
        landWrapper.eq(LandInfo::getFarmerId, farmerId)
                .eq(LandInfo::getStatus, "1")
                .set(LandInfo::getFarmerId, null)
                .set(LandInfo::getFarmerName, null)
                .set(LandInfo::getFarmerIdCard, null)
                .set(LandInfo::getFarmerPhone, null);
        landInfoMapper.update(null, landWrapper);

        // 逻辑删除农民
        LambdaUpdateWrapper<FarmerInfo> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(FarmerInfo::getFarmerId, farmerId)
                .eq(FarmerInfo::getStatus, "1")
                .set(FarmerInfo::getStatus, "0");

        try {
            String username = SecurityUtils.getUsername();
            wrapper.set(FarmerInfo::getUpdateBy, username);
        } catch (Exception e) {
            wrapper.set(FarmerInfo::getUpdateBy, "system");
        }

        return farmerInfoMapper.update(null, wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Integer> deleteFarmerInfoByIds(String[] farmerIds) {
        int successCount = 0;
        int failCount = 0;

        for (String farmerId : farmerIds) {
            try {
                int rows = deleteFarmerInfoByFarmerId(farmerId);
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
    public List<Map<String, Object>> selectFarmerOptions(String kebeleCode, String keyword) {
        LambdaQueryWrapper<FarmerInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FarmerInfo::getStatus, "1")
                .select(FarmerInfo::getFarmerId, FarmerInfo::getFarmerName,
                        FarmerInfo::getIdCard, FarmerInfo::getPhone);

        // 村代码筛选
        if (StrUtil.isNotBlank(kebeleCode)) {
            wrapper.eq(FarmerInfo::getKebeleCode, kebeleCode);
        }

        // 关键词搜索
        if (StrUtil.isNotBlank(keyword)) {
            wrapper.and(w -> w.like(FarmerInfo::getFarmerName, keyword)
                    .or()
                    .like(FarmerInfo::getIdCard, keyword));
        }

        List<FarmerInfo> list = farmerInfoMapper.selectList(wrapper);

        // 转换为Map列表
        List<Map<String, Object>> options = new ArrayList<>();
        for (FarmerInfo farmer : list) {
            Map<String, Object> option = new HashMap<>();
            option.put("farmerId", farmer.getFarmerId());
            option.put("farmerName", farmer.getFarmerName());
            option.put("idCard", farmer.getIdCard());
            option.put("phone", farmer.getPhone());
            options.add(option);
        }

        return options;
    }

    @Override
    public boolean checkIdCardUnique(String idCard, String farmerId) {
        LambdaQueryWrapper<FarmerInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FarmerInfo::getIdCard, idCard)
                .eq(FarmerInfo::getStatus, "1");

        // 修改时排除自己
        if (StrUtil.isNotBlank(farmerId)) {
            wrapper.ne(FarmerInfo::getFarmerId, farmerId);
        }

        return farmerInfoMapper.selectCount(wrapper) == 0;
    }

    @Override
    public FarmerInfo selectFarmerByIdCard(String idCard) {
        if (StrUtil.isBlank(idCard)) {
            return null;
        }
        LambdaQueryWrapper<FarmerInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FarmerInfo::getIdCard, idCard)
               .eq(FarmerInfo::getStatus, "1");
        return farmerInfoMapper.selectOne(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateLandStatistics(String farmerId) {
        // 查询该农民的所有土地
        LambdaQueryWrapper<LandInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LandInfo::getFarmerId, farmerId)
                .eq(LandInfo::getStatus, "1");
        List<LandInfo> landList = landInfoMapper.selectList(wrapper);

        // 计算统计数据
        int landCount = landList.size();
        BigDecimal totalArea = landList.stream()
                .map(LandInfo::getAreaSize)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 更新农民统计信息
        LambdaUpdateWrapper<FarmerInfo> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(FarmerInfo::getFarmerId, farmerId)
                .set(FarmerInfo::getLandCount, landCount)
                .set(FarmerInfo::getTotalLandArea, totalArea);

        farmerInfoMapper.update(null, updateWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importFarmerData(List<FarmerInfo> farmerList, boolean updateSupport) {
        int successCount = 0;
        int failCount = 0;
        int updateCount = 0;
        StringBuilder failMsg = new StringBuilder();

        for (FarmerInfo farmer : farmerList) {
            try {
                // 验证必填字段
                if (StrUtil.isBlank(farmer.getFarmerName())) {
                    failCount++;
                    failMsg.append("农民姓名不能为空; ");
                    continue;
                }

                // 检查身份证是否已存在
                FarmerInfo existFarmer = null;
                if (StrUtil.isNotBlank(farmer.getIdCard())) {
                    LambdaQueryWrapper<FarmerInfo> wrapper = new LambdaQueryWrapper<>();
                    wrapper.eq(FarmerInfo::getIdCard, farmer.getIdCard())
                            .eq(FarmerInfo::getStatus, "1");
                    existFarmer = farmerInfoMapper.selectOne(wrapper);
                }

                if (existFarmer != null) {
                    if (updateSupport) {
                        // 更新已存在的记录
                        farmer.setFarmerId(existFarmer.getFarmerId());
                        updateFarmerInfo(farmer);
                        updateCount++;
                    } else {
                        failCount++;
                        failMsg.append("身份证号 ").append(farmer.getIdCard()).append(" 已存在; ");
                    }
                } else {
                    // 新增记录
                    insertFarmerInfo(farmer);
                    successCount++;
                }
            } catch (Exception e) {
                failCount++;
                failMsg.append("导入 ").append(farmer.getFarmerName()).append(" 失败: ").append(e.getMessage())
                        .append("; ");
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("successCount", successCount);
        result.put("updateCount", updateCount);
        result.put("failCount", failCount);
        if (failMsg.length() > 0) {
            result.put("failMsg", failMsg.toString());
        }

        return result;
    }
}
