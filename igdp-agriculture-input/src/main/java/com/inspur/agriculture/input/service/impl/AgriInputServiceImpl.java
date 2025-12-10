package com.inspur.agriculture.input.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.inspur.agriculture.input.domain.*;
import com.inspur.agriculture.input.mapper.*;
import com.inspur.agriculture.input.service.IAgriInputService;
import com.inspur.common.utils.DateUtils;
import com.inspur.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 农业投入品Service业务层处理
 *
 * @author igdp
 */
@Service
public class AgriInputServiceImpl implements IAgriInputService {

    @Autowired
    private AgriInputMapper agriInputMapper;

    @Autowired
    private PesticidePropertiesMapper pesticidePropertiesMapper;

    @Autowired
    private FertilizerPropertiesMapper fertilizerPropertiesMapper;

    @Autowired
    private SeedPropertiesMapper seedPropertiesMapper;

    /**
     * 查询投入品列表
     *
     * @param agriInput 投入品
     * @return 投入品集合
     */
    @Override
    public List<AgriInput> selectInputList(AgriInput agriInput) {
        return agriInputMapper.selectInputListWithProperties(agriInput);
    }

    /**
     * 根据ID查询投入品详情
     *
     * @param inputId 投入品ID
     * @return 投入品
     */
    @Override
    public AgriInput selectInputById(Long inputId) {
        return agriInputMapper.selectInputByIdWithProperties(inputId);
    }

    /**
     * 新增投入品
     *
     * @param agriInput 投入品
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int insertInput(AgriInput agriInput) {
        // 设置创建信息
        agriInput.setCreateTime(DateUtils.getNowDate());
        try {
            String username = SecurityUtils.getUsername();
            agriInput.setCreatePeople(username);
        } catch (Exception e) {
            agriInput.setCreatePeople("system");
        }
        agriInput.setDelFlag("0");

        // 自动生成批次号（格式: BATCH-YYYYMMDD-随机6位数字）
        if (agriInput.getBatchId() == null || agriInput.getBatchId().trim().isEmpty()) {
            String dateStr = DateUtils.dateTimeNow("yyyyMMdd");
            String randomNum = String.format("%06d", (int)(Math.random() * 1000000));
            agriInput.setBatchId("BATCH-" + dateStr + "-" + randomNum);
        }

        // 插入投入品基本信息
        int rows = agriInputMapper.insert(agriInput);

        // 根据类型插入对应的特性信息
        if (rows > 0) {
            Long inputId = agriInput.getInputId();
            String type = agriInput.getType();

            if ("pesticide".equals(type) && agriInput.getPesticideProperties() != null) {
                PesticideProperties properties = agriInput.getPesticideProperties();
                properties.setInputId(inputId);
                pesticidePropertiesMapper.insert(properties);
            } else if ("fertilizer".equals(type) && agriInput.getFertilizerProperties() != null) {
                FertilizerProperties properties = agriInput.getFertilizerProperties();
                properties.setInputId(inputId);
                fertilizerPropertiesMapper.insert(properties);
            } else if ("seed".equals(type) && agriInput.getSeedProperties() != null) {
                SeedProperties properties = agriInput.getSeedProperties();
                properties.setInputId(inputId);
                seedPropertiesMapper.insert(properties);
            }
        }

        return rows;
    }

    /**
     * 修改投入品
     *
     * @param agriInput 投入品
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int updateInput(AgriInput agriInput) {
        // 设置更新信息
        agriInput.setUpdateTime(DateUtils.getNowDate());
        try {
            String username = SecurityUtils.getUsername();
            agriInput.setUpdatePeople(username);
        } catch (Exception e) {
            agriInput.setUpdatePeople("system");
        }

        // 更新投入品基本信息
        int rows = agriInputMapper.updateById(agriInput);

        // 根据类型更新对应的特性信息
        if (rows > 0) {
            Long inputId = agriInput.getInputId();
            String type = agriInput.getType();

            if ("pesticide".equals(type) && agriInput.getPesticideProperties() != null) {
                PesticideProperties properties = agriInput.getPesticideProperties();
                properties.setInputId(inputId);

                // 查询是否存在
                LambdaQueryWrapper<PesticideProperties> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(PesticideProperties::getInputId, inputId);
                PesticideProperties existProperties = pesticidePropertiesMapper.selectOne(wrapper);

                if (existProperties != null) {
                    properties.setPesticideId(existProperties.getPesticideId());
                    pesticidePropertiesMapper.updateById(properties);
                } else {
                    pesticidePropertiesMapper.insert(properties);
                }
            } else if ("fertilizer".equals(type) && agriInput.getFertilizerProperties() != null) {
                FertilizerProperties properties = agriInput.getFertilizerProperties();
                properties.setInputId(inputId);

                LambdaQueryWrapper<FertilizerProperties> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(FertilizerProperties::getInputId, inputId);
                FertilizerProperties existProperties = fertilizerPropertiesMapper.selectOne(wrapper);

                if (existProperties != null) {
                    properties.setFertilizerId(existProperties.getFertilizerId());
                    fertilizerPropertiesMapper.updateById(properties);
                } else {
                    fertilizerPropertiesMapper.insert(properties);
                }
            } else if ("seed".equals(type) && agriInput.getSeedProperties() != null) {
                SeedProperties properties = agriInput.getSeedProperties();
                properties.setInputId(inputId);

                LambdaQueryWrapper<SeedProperties> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(SeedProperties::getInputId, inputId);
                SeedProperties existProperties = seedPropertiesMapper.selectOne(wrapper);

                if (existProperties != null) {
                    properties.setSeedId(existProperties.getSeedId());
                    seedPropertiesMapper.updateById(properties);
                } else {
                    seedPropertiesMapper.insert(properties);
                }
            }
        }

        return rows;
    }

    /**
     * 批量删除投入品
     *
     * @param inputIds 需要删除的投入品ID
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int deleteInputByIds(Long[] inputIds) {
        int rows = 0;
        for (Long inputId : inputIds) {
            rows += deleteInputById(inputId);
        }
        return rows;
    }

    /**
     * 删除投入品信息（逻辑删除）
     *
     * @param inputId 投入品ID
     * @return 结果
     */
    @Override
    public int deleteInputById(Long inputId) {
        AgriInput agriInput = new AgriInput();
        agriInput.setInputId(inputId);
        agriInput.setDelFlag("2");
        agriInput.setUpdateTime(DateUtils.getNowDate());
        try {
            String username = SecurityUtils.getUsername();
            agriInput.setUpdatePeople(username);
        } catch (Exception e) {
            agriInput.setUpdatePeople("system");
        }
        return agriInputMapper.updateById(agriInput);
    }

    /**
     * 获取投入品统计信息
     *
     * @return 统计信息
     */
    @Override
    public Map<String, Object> getInputStatistics() {
        Map<String, Object> statistics = new HashMap<>();

        // 查询所有未删除的投入品
        LambdaQueryWrapper<AgriInput> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AgriInput::getDelFlag, "0");

        List<AgriInput> allInputs = agriInputMapper.selectList(wrapper);

        // 总数
        statistics.put("total", allInputs.size());

        // 按类型统计
        long pesticideCount = allInputs.stream().filter(i -> "pesticide".equals(i.getType())).count();
        long fertilizerCount = allInputs.stream().filter(i -> "fertilizer".equals(i.getType())).count();
        long seedCount = allInputs.stream().filter(i -> "seed".equals(i.getType())).count();
        long otherCount = allInputs.stream().filter(i -> "other".equals(i.getType())).count();

        statistics.put("pesticide", pesticideCount);
        statistics.put("fertilizer", fertilizerCount);
        statistics.put("seed", seedCount);
        statistics.put("other", otherCount);

        // 按状态统计
        long activeCount = allInputs.stream().filter(i -> "active".equals(i.getStatus())).count();
        long inactiveCount = allInputs.stream().filter(i -> "inactive".equals(i.getStatus())).count();

        statistics.put("active", activeCount);
        statistics.put("inactive", inactiveCount);

        return statistics;
    }
}
