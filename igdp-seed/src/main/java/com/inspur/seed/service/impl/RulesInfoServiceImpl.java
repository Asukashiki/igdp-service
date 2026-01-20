package com.inspur.seed.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.seed.domain.dto.RulesInfoDTO;
import com.inspur.seed.domain.entity.RulesInfo;
import com.inspur.seed.domain.vo.RulesInfoVO;
import com.inspur.seed.mapper.RulesInfoMapper;
import com.inspur.seed.service.IRulesInfoService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * 规则信息服务实现类
 *
 * @author igdp
 * @date 2025-12-18
 */
@Service
public class RulesInfoServiceImpl extends ServiceImpl<RulesInfoMapper, RulesInfo> implements IRulesInfoService {

    @Override
    public IPage<RulesInfoVO> selectRulesInfoPage(IPage<RulesInfo> page, RulesInfoDTO dto) {
        QueryWrapper<RulesInfo> wrapper = buildQueryWrapper(dto);
        IPage<RulesInfo> entityPage = this.page(page, wrapper);

        IPage<RulesInfoVO> voPage = entityPage.convert(entity -> 
            BeanUtil.copyProperties(entity, RulesInfoVO.class)
        );

        return voPage;
    }

    @Override
    public List<RulesInfoVO> selectRulesInfoList(RulesInfoDTO dto) {
        QueryWrapper<RulesInfo> wrapper = buildQueryWrapper(dto);
        List<RulesInfo> list = this.list(wrapper);

        return list.stream()
                .map(entity -> BeanUtil.copyProperties(entity, RulesInfoVO.class))
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public RulesInfoVO selectRulesInfoById(Integer id) {
        RulesInfo entity = this.getById(id);
        if (entity == null) {
            return null;
        }

        return BeanUtil.copyProperties(entity, RulesInfoVO.class);
    }

    @Override
    public int insertRulesInfo(RulesInfoDTO dto) {
        RulesInfo entity = BeanUtil.copyProperties(dto, RulesInfo.class);
        return this.save(entity) ? 1 : 0;
    }

    @Override
    public int updateRulesInfo(RulesInfoDTO dto) {
        RulesInfo entity = BeanUtil.copyProperties(dto, RulesInfo.class);
        return this.updateById(entity) ? 1 : 0;
    }

    @Override
    public int deleteRulesInfoByIds(Integer[] ids) {
        return this.removeByIds(java.util.Arrays.asList(ids)) ? 1 : 0;
    }

    @Override
    public boolean checkRule(String dictCode, BigDecimal value) {
        // 根据字典编码查询规则信息
        QueryWrapper<RulesInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dict_code", dictCode);
        List<RulesInfo> rules = this.list(queryWrapper);

        if (rules.isEmpty()) {
            throw new RuntimeException("未找到对应的规则信息");
        }

        RulesInfo rule = rules.get(0);
        String operator = rule.getOperator();
        BigDecimal minValue = rule.getMinValue();
        BigDecimal maxValue = rule.getMaxValue();

        switch (operator) {
            case ">":
                return value.compareTo(minValue) > 0;
            case ">=":
                return value.compareTo(minValue) >= 0;
            case "<":
                return value.compareTo(maxValue) < 0;
            case "<=":
                return value.compareTo(maxValue) <= 0;
            case "=":
                return value.compareTo(minValue) == 0;
            case "between":
                return value.compareTo(minValue) >= 0 && value.compareTo(maxValue) <= 0;
            case "outside":
                return value.compareTo(minValue) < 0 || value.compareTo(maxValue) > 0;
            default:
                throw new RuntimeException("不支持的操作符: " + operator);
        }
    }

    /**
     * 构建查询条件
     *
     * @param dto 查询条件DTO
     * @return QueryWrapper
     */
    private QueryWrapper<RulesInfo> buildQueryWrapper(RulesInfoDTO dto) {
        QueryWrapper<RulesInfo> wrapper = new QueryWrapper<>();

        if (dto != null) {
            // 字典编码
            if (dto.getDictCode() != null && !dto.getDictCode().isEmpty()) {
                wrapper.like("dict_code", dto.getDictCode());
            }
            // 检测类型
            if (dto.getInspectionType() != null && !dto.getInspectionType().isEmpty()) {
                wrapper.like("inspection_type", dto.getInspectionType());
            }
            // 条件类型
            if (dto.getConditionType() != null && !dto.getConditionType().isEmpty()) {
                wrapper.eq("condition_type", dto.getConditionType());
            }
            // 操作符
            if (dto.getOperator() != null && !dto.getOperator().isEmpty()) {
                wrapper.eq("operator", dto.getOperator());
            }
        }

        return wrapper;
    }
}