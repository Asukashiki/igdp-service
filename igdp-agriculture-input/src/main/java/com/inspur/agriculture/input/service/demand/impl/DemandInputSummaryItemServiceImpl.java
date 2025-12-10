package com.inspur.agriculture.input.service.demand.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.inspur.agriculture.input.domain.demand.DemandInputSummaryItem;
import com.inspur.agriculture.input.dto.demand.DemandInputSummaryItemDTO;
import com.inspur.agriculture.input.dto.demand.DemandInputSummaryItemQueryDTO;
import com.inspur.agriculture.input.mapper.demand.DemandInputSummaryItemMapper;
import com.inspur.agriculture.input.service.demand.IDemandInputSummaryItemService;
import com.inspur.agriculture.input.vo.demand.DemandInputSummaryItemVO;
import com.inspur.common.exception.ServiceException;
import com.inspur.common.utils.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 农资汇聚统计 Service实现
 *
 * @author inspur
 * @date 2025-12-09
 */
@Service
public class DemandInputSummaryItemServiceImpl implements IDemandInputSummaryItemService {

    @Autowired
    private DemandInputSummaryItemMapper demandInputSummaryItemMapper;

    @Override
    public List<DemandInputSummaryItemVO> getDemandInputSummaryItemList(DemandInputSummaryItemQueryDTO queryDTO) {
        return demandInputSummaryItemMapper.selectDemandInputSummaryItemList(queryDTO);
    }

    @Override
    public DemandInputSummaryItemVO getDemandInputSummaryItemById(String id) {
        return demandInputSummaryItemMapper.selectDemandInputSummaryItemById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int addDemandInputSummaryItem(DemandInputSummaryItemDTO dto) {
        // 检查农资分类和类型组合是否已存在
        // DTO转Entity
        DemandInputSummaryItem item = new DemandInputSummaryItem();
        BeanUtils.copyProperties(dto, item);

        // 设置默认值
        if (item.getDemandCount() == null) {
            item.setDemandCount(0);
        }
        if (item.getStatus() == null || item.getStatus().isEmpty()) {
            item.setStatus("0"); // 默认待审核
        }
        item.setCreatedTime(DateUtils.getNowDate());
        item.setUpdatedTime(DateUtils.getNowDate());

        // 插入数据
        return demandInputSummaryItemMapper.insert(item);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int updateDemandInputSummaryItem(DemandInputSummaryItemDTO dto) {
        if (dto.getId() == null || dto.getId().isEmpty()) {
            throw new ServiceException("主键ID不能为空");
        }

        // 查询原记录
        DemandInputSummaryItem item = demandInputSummaryItemMapper.selectById(dto.getId());
        if (item == null) {
            throw new ServiceException("农资汇聚统计记录不存在");
        }

        // 检查农资分类和类型组合是否已存在(排除当前记录)
        int count = demandInputSummaryItemMapper.checkCategoryTypeExists(
                dto.getInputCategory(),
                dto.getInputType(),
                dto.getId()
        );
        if (count > 0) {
            throw new ServiceException("该农资分类和类型组合已存在");
        }

        // DTO转Entity
        BeanUtils.copyProperties(dto, item);

        // 设置更新时间
        item.setUpdatedTime(DateUtils.getNowDate());

        // 更新数据
        return demandInputSummaryItemMapper.updateById(item);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int deleteDemandInputSummaryItem(String id) {
        // 查询原记录
        DemandInputSummaryItem item = demandInputSummaryItemMapper.selectById(id);
        if (item == null) {
            throw new ServiceException("农资汇聚统计记录不存在");
        }

        // 物理删除(根据表结构没有del_flag字段)
        return demandInputSummaryItemMapper.deleteById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int batchDeleteDemandInputSummaryItem(List<String> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new ServiceException("删除的ID列表不能为空");
        }

        // 批量物理删除
        QueryWrapper<DemandInputSummaryItem> wrapper = new QueryWrapper<>();
        wrapper.in("id", ids);
        return demandInputSummaryItemMapper.delete(wrapper);
    }
}
