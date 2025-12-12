package com.inspur.agriculture.input.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.inspur.agriculture.input.domain.AgriInput;
import com.inspur.agriculture.input.mapper.AgriInputMapper;
import com.inspur.agriculture.input.service.IAgriInputService;
import com.inspur.common.utils.DateUtils;
import com.inspur.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 农业投入品服务实现类
 *
 * @author igdp
 */
@Service
public class AgriInputServiceImpl implements IAgriInputService {

    @Autowired
    private AgriInputMapper agriInputMapper;

    // 单机自增序列（6位数字，最大值999999，分布式场景可替换为数据库序列）
    private static final AtomicInteger SEQ = new AtomicInteger(1);

    /**
     * 查询投入品列表（适配所有字段查询）
     */
    @Override
    public List<AgriInput> selectInputList(AgriInput agriInput) {
        LambdaQueryWrapper<AgriInput> wrapper = new LambdaQueryWrapper<>();
        // 过滤删除标识
        wrapper.eq(AgriInput::getDelFlag, "0");
        // 动态拼接查询条件
        if (StringUtils.hasText(agriInput.getInputName())) {
            wrapper.like(AgriInput::getInputName, agriInput.getInputName());
        }
        if (StringUtils.hasText(agriInput.getType())) {
            wrapper.eq(AgriInput::getType, agriInput.getType());
        }
        if (StringUtils.hasText(agriInput.getAgriculturalInputType())) {
            wrapper.like(AgriInput::getAgriculturalInputType, agriInput.getAgriculturalInputType());
        }
        if (StringUtils.hasText(agriInput.getRegisterCode())) {
            wrapper.like(AgriInput::getRegisterCode, agriInput.getRegisterCode());
        }
        if (StringUtils.hasText(agriInput.getInputSku())) {
            wrapper.eq(AgriInput::getInputSku, agriInput.getInputSku());
        }
        if (StringUtils.hasText(agriInput.getStatus())) {
            wrapper.eq(AgriInput::getStatus, agriInput.getStatus());
        }
        if (StringUtils.hasText(agriInput.getTrademark())) {
            wrapper.like(AgriInput::getTrademark, agriInput.getTrademark());
        }
        // 按创建时间倒序
        wrapper.orderByDesc(AgriInput::getCreateTime);
        return agriInputMapper.selectList(wrapper);
    }

    /**
     * 根据ID查询投入品详情（返回所有字段）
     */
    @Override
    public AgriInput selectInputById(Long inputId) {
        LambdaQueryWrapper<AgriInput> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AgriInput::getInputId, inputId)
                .eq(AgriInput::getDelFlag, "0");
        return agriInputMapper.selectOne(wrapper);
    }

    /**
     * 新增投入品（自动填充通用信息 + 生成业务ID）
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int insertInput(AgriInput agriInput) {
        // 自动填充通用信息
        agriInput.setCreateTime(DateUtils.getNowDate());
        agriInput.setUpdateTime(DateUtils.getNowDate());
        try {
            String username = SecurityUtils.getUsername();
            agriInput.setCreatePeople(username);
            agriInput.setUpdatePeople(username);
        } catch (Exception e) {
            agriInput.setCreatePeople("system");
            agriInput.setUpdatePeople("system");
        }
        // 默认值设置
        agriInput.setDelFlag("0");
        if (!StringUtils.hasText(agriInput.getStatus())) {
            agriInput.setStatus("active");
        }

        // 生成业务ID
        agriInput.setInputBizId(generateInputBizId(agriInput));

        // 插入数据库
        return agriInputMapper.insert(agriInput);
    }

    /**
     * 生成业务ID
     */
    private String generateInputBizId(AgriInput agriInput) {
        // 类型代码映射：农药01/化肥02/种子03/其他04
        String typeCode;
        switch (agriInput.getType()) {
            case "pesticide":
                typeCode = "01";
                break;
            case "fertilizer":
                typeCode = "02";
                break;
            case "seed":
                typeCode = "03";
                break;
            case "other":
                typeCode = "04";
                break;
            default:
                typeCode = "99";
                break;
        }

        // 品类代码（取品类名称前4位，无则0000）
        String agriTypeCode = "0000";
        if (StringUtils.hasText(agriInput.getAgriculturalInputType())) {
            String typeName = agriInput.getAgriculturalInputType().replaceAll("\\s+", "");
            agriTypeCode = typeName.length() >= 4 ? typeName.substring(0, 4) : String.format("%-4s", typeName).replace(' ', '0');
        }

        // 年度（YYYY格式）
        String year = new SimpleDateFormat("yyyy").format(DateUtils.getNowDate());

        // 6位自增序列
        int seq = SEQ.getAndIncrement();
        String seqStr = String.format("%06d", seq);
        if (seq > 999999) {
            SEQ.set(1);
        }

        // 拼接业务ID：IN_类型代码_品类代码_年度_6位序列
        return String.format("IN_%s_%s_%s_%s", typeCode, agriTypeCode, year, seqStr);
    }

    /**
     * 修改投入品（自动更新修改信息）
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int updateInput(AgriInput agriInput) {
        // 自动填充修改信息
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
     * 批量删除投入品（逻辑删除）
     */
    @Override
    public int deleteInputByIds(Long[] inputIds) {
        int rows = 0;
        for (Long inputId : inputIds) {
            rows += deleteInputById(inputId);
        }
        return rows;
    }

    /**
     * 逻辑删除投入品（自动更新修改信息）
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
     * 获取投入品统计信息（全类型）
     */
    @Override
    public Map<String, Object> getInputStatistics() {
        Map<String, Object> statistics = new HashMap<>();
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