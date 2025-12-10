package com.inspur.agriculture.input.service.demand.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.inspur.agriculture.input.domain.demand.DemandInputSummary;
import com.inspur.agriculture.input.dto.demand.DemandInputSummaryDTO;
import com.inspur.agriculture.input.dto.demand.DemandInputSummaryQueryDTO;
import com.inspur.agriculture.input.mapper.demand.DemandInputSummaryMapper;
import com.inspur.agriculture.input.service.demand.IDemandInputSummaryService;
import com.inspur.agriculture.input.vo.demand.DemandInputSummaryVO;
import com.inspur.common.exception.ServiceException;
import com.inspur.common.utils.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 农资需求汇总 Service实现
 *
 * @author inspur
 * @date 2025-12-10
 */
@Service
public class DemandInputSummaryServiceImpl implements IDemandInputSummaryService {

    @Autowired
    private DemandInputSummaryMapper demandInputSummaryMapper;

    @Override
    public List<DemandInputSummaryVO> getDemandInputSummaryList(DemandInputSummaryQueryDTO queryDTO) {
        return demandInputSummaryMapper.selectDemandInputSummaryList(queryDTO);
    }

    @Override
    public DemandInputSummaryVO getDemandInputSummaryById(String id) {
        return demandInputSummaryMapper.selectDemandInputSummaryById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int addDemandInputSummary(DemandInputSummaryDTO dto) {
        // DTO转Entity
        DemandInputSummary summary = new DemandInputSummary();
        BeanUtils.copyProperties(dto, summary);

        // 设置默认值
        if (summary.getStatus() == null || summary.getStatus().isEmpty()) {
            summary.setStatus("0"); // 默认待审核
        }
        summary.setCreateTime(DateUtils.getNowDate());

        // 如果没有设置年份，使用当前年份
        if (summary.getYear() == null || summary.getYear().isEmpty()) {
            summary.setYear(String.valueOf(java.time.Year.now().getValue()));
        }

        // 插入数据
        return demandInputSummaryMapper.insert(summary);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int updateDemandInputSummary(DemandInputSummaryDTO dto) {
        if (dto.getId() == null || dto.getId().isEmpty()) {
            throw new ServiceException("主键ID不能为空");
        }

        // 查询原记录
        DemandInputSummary summary = demandInputSummaryMapper.selectById(dto.getId());
        if (summary == null) {
            throw new ServiceException("农资需求汇总记录不存在");
        }

        // DTO转Entity
        BeanUtils.copyProperties(dto, summary);

        // 更新数据
        return demandInputSummaryMapper.updateById(summary);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int deleteDemandInputSummary(String id) {
        // 查询原记录
        DemandInputSummary summary = demandInputSummaryMapper.selectById(id);
        if (summary == null) {
            throw new ServiceException("农资需求汇总记录不存在");
        }

        // 物理删除
        return demandInputSummaryMapper.deleteById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int batchDeleteDemandInputSummary(List<String> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new ServiceException("删除的ID列表不能为空");
        }

        // 批量物理删除
        QueryWrapper<DemandInputSummary> wrapper = new QueryWrapper<>();
        wrapper.in("id", ids);
        return demandInputSummaryMapper.delete(wrapper);
    }
}
