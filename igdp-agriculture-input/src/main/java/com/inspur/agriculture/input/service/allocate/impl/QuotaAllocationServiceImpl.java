package com.inspur.agriculture.input.service.allocate.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.agriculture.input.domain.allocate.dto.QuotaAllocationAddDTO;
import com.inspur.agriculture.input.domain.allocate.dto.QuotaAllocationDeleteDTO;
import com.inspur.agriculture.input.domain.allocate.dto.QuotaAllocationUpdateDTO;
import com.inspur.agriculture.input.domain.allocate.entity.QuotaAllocation;
import com.inspur.agriculture.input.domain.allocate.entity.StateAnnualQuota;
import com.inspur.agriculture.input.domain.allocate.vo.QuotaAllocationVO;
import com.inspur.agriculture.input.mapper.allocate.QuotaAllocationMapper;
import com.inspur.agriculture.input.mapper.allocate.StateAnnualQuotaMapper;
import com.inspur.agriculture.input.service.allocate.QuotaAllocationService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Quota Allocation Service Implementation
 * 配额分配服务实现类
 */
@Service
public class QuotaAllocationServiceImpl extends ServiceImpl<QuotaAllocationMapper, QuotaAllocation>
        implements QuotaAllocationService {

    @Resource
    private QuotaAllocationMapper quotaAllocationMapper;

    @Resource
    private StateAnnualQuotaMapper stateAnnualQuotaMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public QuotaAllocationVO add(QuotaAllocationAddDTO dto) {
        // Validate parameters
        validateAddParams(dto);

        // Validate quota existence
        StateAnnualQuota stateQuota = stateAnnualQuotaMapper.selectById(dto.getQuotaId());
        if (stateQuota == null) {
            throw new RuntimeException("State quota not found");
        }

        // Validate year and category match
        if (!stateQuota.getYear().equals(dto.getYear()) || !stateQuota.getCategoryId().equals(dto.getCategoryId())) {
            throw new RuntimeException("Year or category does not match state quota");
        }

        // Validate allocation amount
        validateAllocationAmount(dto);

        // Build entity
        QuotaAllocation entity = new QuotaAllocation();
        BeanUtils.copyProperties(dto, entity);

        // Generate allocation name
        String allocationName = generateAllocationName(dto);
        entity.setAllocationName(allocationName);

        // Calculate remaining quota and status
        BigDecimal remainingQuota = dto.getTotalReceivedQuota().subtract(dto.getAllocatedQuota());
        entity.setTotalAllocatedQuota(dto.getAllocatedQuota());
        entity.setRemainingQuota(remainingQuota);
        entity.setAllocationStatus(calculateAllocationStatus(dto.getAllocatedQuota(), dto.getTotalReceivedQuota()));

        // Set timestamps
        LocalDateTime now = LocalDateTime.now();
        entity.setOperateTime(now);
        entity.setProgressUpdateTime(now);
        entity.setCreateTime(now);
        entity.setUpdateTime(now);

        // Save
        quotaAllocationMapper.insert(entity);

        // Return VO
        return detail(entity.getAllocationId(), dto.getOperatorDivisionId());
    }

    @Override
    public IPage<QuotaAllocationVO> page(Integer pageNum, Integer pageSize, Integer year,
                                         String categoryId, String fromDivisionId,
                                         Integer fromDivisionLevel, Integer allocationStatus,
                                         String operatorDivisionId) {
        // Validate page parameters
        if (pageNum == null || pageNum < 1) {
            pageNum = 1;
        }
        if (pageSize == null || pageSize < 1 || pageSize > 50) {
            pageSize = 10;
        }

        Page<QuotaAllocationVO> page = new Page<>(pageNum, pageSize);
        return quotaAllocationMapper.selectPageWithDetail(page, year, categoryId, fromDivisionId,
                fromDivisionLevel, allocationStatus, operatorDivisionId);
    }

    @Override
    public QuotaAllocationVO detail(String allocationId, String operatorDivisionId) {
        if (StrUtil.isBlank(allocationId)) {
            throw new RuntimeException("Invalid parameter: allocationId is required");
        }

        QuotaAllocationVO vo = quotaAllocationMapper.selectDetailById(allocationId, operatorDivisionId);
        if (vo == null) {
            throw new RuntimeException("Allocation not found");
        }

        // Set status name
        vo.setAllocationStatusName(getStatusName(vo.getAllocationStatus()));

        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public QuotaAllocationVO update(QuotaAllocationUpdateDTO dto) {
        // Validate parameters
        if (StrUtil.isBlank(dto.getAllocationId()) || dto.getNewAllocatedQuota() == null
                || dto.getNewAllocatedQuota().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Invalid parameter: allocationId and newAllocatedQuota are required");
        }

        // Check existence
        QuotaAllocation entity = quotaAllocationMapper.selectById(dto.getAllocationId());
        if (entity == null) {
            throw new RuntimeException("Allocation not found");
        }

        // Validate new amount
        BigDecimal oldAllocated = entity.getAllocatedQuota();
        BigDecimal totalAllocated = entity.getTotalAllocatedQuota();
        BigDecimal newTotalAllocated = totalAllocated.subtract(oldAllocated).add(dto.getNewAllocatedQuota());

        if (newTotalAllocated.compareTo(entity.getTotalReceivedQuota()) > 0) {
            throw new RuntimeException("Modified cumulative allocation exceeds total received quota");
        }

        // Update entity
        entity.setAllocatedQuota(dto.getNewAllocatedQuota());
        entity.setTotalAllocatedQuota(newTotalAllocated);
        entity.setRemainingQuota(entity.getTotalReceivedQuota().subtract(newTotalAllocated));
        entity.setAllocationStatus(calculateAllocationStatus(newTotalAllocated, entity.getTotalReceivedQuota()));
        entity.setModifierId(dto.getModifierId());
        entity.setModifierDivisionId(dto.getModifierDivisionId());
        entity.setProgressUpdateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());

        quotaAllocationMapper.updateById(entity);

        // Return VO
        return detail(dto.getAllocationId(), dto.getModifierDivisionId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean delete(QuotaAllocationDeleteDTO dto) {
        // Validate parameters
        if (StrUtil.isBlank(dto.getAllocationId())) {
            throw new RuntimeException("Invalid parameter: allocationId is required");
        }

        // Check existence
        QuotaAllocation entity = quotaAllocationMapper.selectById(dto.getAllocationId());
        if (entity == null) {
            throw new RuntimeException("Allocation not found");
        }

        // Check if receiver has allocated to lower level
        String receiverId = StrUtil.isNotBlank(entity.getToDivisionId())
                ? entity.getToDivisionId() : entity.getToFarmerId();
        if (StrUtil.isNotBlank(receiverId)) {
            QueryWrapper<QuotaAllocation> queryWrapper = new QueryWrapper<>();
            if (StrUtil.isNotBlank(entity.getToDivisionId())) {
                queryWrapper.eq("from_division_id", entity.getToDivisionId());
            } else {
                queryWrapper.eq("to_farmer_id", entity.getToFarmerId());
            }
            queryWrapper.gt("allocated_quota", BigDecimal.ZERO);

            Long count = quotaAllocationMapper.selectCount(queryWrapper);
            if (count > 0) {
                throw new RuntimeException("Receiver has allocated to lower level and cannot be deleted");
            }
        }

        // Delete
        int result = quotaAllocationMapper.deleteById(dto.getAllocationId());
        return result > 0;
    }

    /**
     * Validate add parameters
     */
    private void validateAddParams(QuotaAllocationAddDTO dto) {
        if (dto.getYear() == null || StrUtil.isBlank(dto.getCategoryId())
                || StrUtil.isBlank(dto.getFromDivisionId()) || dto.getFromDivisionLevel() == null
                || dto.getAllocatedQuota() == null || dto.getAllocatedQuota().compareTo(BigDecimal.ZERO) <= 0
                || StrUtil.isBlank(dto.getQuotaId()) || dto.getTotalReceivedQuota() == null) {
            throw new RuntimeException("Invalid parameter: required fields are missing");
        }

        // Validate toDivisionId and toFarmerId
        if (StrUtil.isBlank(dto.getToDivisionId()) && StrUtil.isBlank(dto.getToFarmerId())) {
            throw new RuntimeException("Invalid parameter: either toDivisionId or toFarmerId is required");
        }

        // Validate division level match
        if (dto.getFromDivisionLevel() == 4 && StrUtil.isBlank(dto.getToFarmerId())) {
            throw new RuntimeException("Invalid parameter: village level can only allocate to farmers");
        }
    }

    /**
     * Validate allocation amount
     */
    private void validateAllocationAmount(QuotaAllocationAddDTO dto) {
        // Query cumulative allocated amount
        QueryWrapper<QuotaAllocation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("from_division_id", dto.getFromDivisionId())
                .eq("year", dto.getYear())
                .eq("category_id", dto.getCategoryId());

        BigDecimal cumulativeAllocated = BigDecimal.ZERO;
        for (QuotaAllocation allocation : quotaAllocationMapper.selectList(queryWrapper)) {
            cumulativeAllocated = cumulativeAllocated.add(allocation.getAllocatedQuota());
        }

        BigDecimal newCumulative = cumulativeAllocated.add(dto.getAllocatedQuota());
        if (newCumulative.compareTo(dto.getTotalReceivedQuota()) > 0) {
            throw new RuntimeException("Cumulative allocation exceeds total received quota");
        }
    }

    /**
     * Generate allocation name
     */
    private String generateAllocationName(QuotaAllocationAddDTO dto) {
        String levelName = getLevelName(dto.getFromDivisionLevel());
        String toLevelName = dto.getFromDivisionLevel() < 4
                ? getLevelName(dto.getFromDivisionLevel() + 1) : "farmer";

        return dto.getYear() + "_" + dto.getCategoryId() + "_"
                + levelName + "_" + dto.getFromDivisionId()
                + "_to_" + toLevelName + "_"
                + (StrUtil.isNotBlank(dto.getToDivisionId()) ? dto.getToDivisionId() : dto.getToFarmerId());
    }

    /**
     * Calculate allocation status
     */
    private Integer calculateAllocationStatus(BigDecimal allocated, BigDecimal total) {
        if (allocated.compareTo(BigDecimal.ZERO) == 0) {
            return 0; // Not allocated
        } else if (allocated.compareTo(total) < 0) {
            return 1; // Partially allocated
        } else {
            return 2; // Completed
        }
    }

    /**
     * Get level name
     */
    private String getLevelName(Integer level) {
        switch (level) {
            case 1:
                return "region";
            case 2:
                return "zone";
            case 3:
                return "worede";
            case 4:
                return "kebele";
            default:
                return "unknown";
        }
    }

    /**
     * Get status name
     */
    private String getStatusName(Integer status) {
        switch (status) {
            case 0:
                return "Not Allocated";
            case 1:
                return "Partially Allocated";
            case 2:
                return "Completed";
            default:
                return "Unknown";
        }
    }
}
