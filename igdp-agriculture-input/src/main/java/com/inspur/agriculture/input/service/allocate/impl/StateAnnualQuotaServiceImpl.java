package com.inspur.agriculture.input.service.allocate.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.agriculture.input.domain.allocate.dto.StateAnnualQuotaAddDTO;
import com.inspur.agriculture.input.domain.allocate.dto.StateAnnualQuotaDeleteDTO;
import com.inspur.agriculture.input.domain.allocate.dto.StateAnnualQuotaUpdateDTO;
import com.inspur.agriculture.input.domain.allocate.entity.QuotaAllocation;
import com.inspur.agriculture.input.domain.allocate.entity.StateAnnualQuota;
import com.inspur.agriculture.input.domain.allocate.vo.StateAnnualQuotaVO;
import com.inspur.agriculture.input.mapper.allocate.QuotaAllocationMapper;
import com.inspur.agriculture.input.mapper.allocate.StateAnnualQuotaMapper;
import com.inspur.agriculture.input.service.allocate.StateAnnualQuotaService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * State Annual Quota Service Implementation
 * 州级年度配额服务实现类
 */
@Service
public class StateAnnualQuotaServiceImpl extends ServiceImpl<StateAnnualQuotaMapper, StateAnnualQuota>
        implements StateAnnualQuotaService {

    @Resource
    private StateAnnualQuotaMapper stateAnnualQuotaMapper;

    @Resource
    private QuotaAllocationMapper quotaAllocationMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public StateAnnualQuotaVO add(StateAnnualQuotaAddDTO dto) {
        // Validate parameters
        if (dto.getYear() == null || StrUtil.isBlank(dto.getCategoryId())
                || dto.getTotalQuota() == null || dto.getTotalQuota().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Invalid parameter: year, categoryId and totalQuota are required");
        }

        // Check duplicate
        QueryWrapper<StateAnnualQuota> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("year", dto.getYear())
                .eq("category_id", dto.getCategoryId())
                .eq("operator_division_id", dto.getOperatorDivisionId());
        Long count = stateAnnualQuotaMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new RuntimeException("Quota for this year and category already exists");
        }

        // Build entity
        StateAnnualQuota entity = new StateAnnualQuota();
        BeanUtils.copyProperties(dto, entity);

        // Generate quota name: year_divisionName_categoryName
        String quotaName = dto.getYear() + "_StateQuota_" + dto.getCategoryId();
        entity.setQuotaName(quotaName);
        entity.setOperateTime(LocalDateTime.now());
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());

        // Save
        stateAnnualQuotaMapper.insert(entity);

        // Return VO
        return detail(entity.getQuotaId(), dto.getOperatorDivisionId());
    }

    @Override
    public IPage<StateAnnualQuotaVO> page(Integer pageNum, Integer pageSize, Integer year,
                                          String categoryId, String operatorDivisionId) {
        // Validate page parameters
        if (pageNum == null || pageNum < 1) {
            pageNum = 1;
        }
        if (pageSize == null || pageSize < 1 || pageSize > 50) {
            pageSize = 10;
        }

        Page<StateAnnualQuotaVO> page = new Page<>(pageNum, pageSize);
        return stateAnnualQuotaMapper.selectPageWithDetail(page, year, categoryId, operatorDivisionId);
    }

    @Override
    public StateAnnualQuotaVO detail(String quotaId, String operatorDivisionId) {
        if (StrUtil.isBlank(quotaId)) {
            throw new RuntimeException("Invalid parameter: quotaId is required");
        }

        StateAnnualQuotaVO vo = stateAnnualQuotaMapper.selectDetailById(quotaId, operatorDivisionId);
        if (vo == null) {
            throw new RuntimeException("Quota not found");
        }

        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public StateAnnualQuotaVO update(StateAnnualQuotaUpdateDTO dto) {
        // Validate parameters
        if (StrUtil.isBlank(dto.getQuotaId()) || dto.getTotalQuota() == null
                || dto.getTotalQuota().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Invalid parameter: quotaId and totalQuota are required");
        }

        // Check existence
        StateAnnualQuota entity = stateAnnualQuotaMapper.selectById(dto.getQuotaId());
        if (entity == null) {
            throw new RuntimeException("Quota not found");
        }

        // Update entity
        entity.setTotalQuota(dto.getTotalQuota());
        entity.setModifierId(dto.getModifierId());
        entity.setModifierDivisionId(dto.getModifierDivisionId());
        entity.setUpdateTime(LocalDateTime.now());

        stateAnnualQuotaMapper.updateById(entity);

        // Return VO
        return detail(dto.getQuotaId(), dto.getModifierDivisionId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean delete(StateAnnualQuotaDeleteDTO dto) {
        // Validate parameters
        if (StrUtil.isBlank(dto.getQuotaId())) {
            throw new RuntimeException("Invalid parameter: quotaId is required");
        }

        // Check existence
        StateAnnualQuota entity = stateAnnualQuotaMapper.selectById(dto.getQuotaId());
        if (entity == null) {
            throw new RuntimeException("Quota not found");
        }

        // Check permission
        if (!entity.getOperatorDivisionId().equals(dto.getOperatorDivisionId())) {
            throw new RuntimeException("Permission denied: can only delete quotas from your division");
        }

        // Check if allocated
        QueryWrapper<QuotaAllocation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("quota_id", dto.getQuotaId())
                .gt("allocated_quota", BigDecimal.ZERO);
        Long count = quotaAllocationMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new RuntimeException("Quota has been allocated to lower levels and cannot be deleted");
        }

        // Delete
        int result = stateAnnualQuotaMapper.deleteById(dto.getQuotaId());
        return result > 0;
    }
}
