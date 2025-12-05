package com.inspur.agriculture.input.service.allocate.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.hutool.core.util.IdUtil;
import com.inspur.agriculture.input.domain.allocate.dto.QuotaAllocationAddDTO;
import com.inspur.agriculture.input.domain.allocate.dto.QuotaAllocationDeleteDTO;
import com.inspur.agriculture.input.domain.allocate.dto.QuotaAllocationUpdateDTO;
import com.inspur.agriculture.input.domain.allocate.dto.QuotaBatchAllocationDTO;
import com.inspur.agriculture.input.domain.allocate.entity.QuotaAllocation;
import com.inspur.agriculture.input.domain.allocate.entity.StateAnnualQuota;
import com.inspur.agriculture.input.domain.allocate.vo.ChildDivisionVO;
import com.inspur.agriculture.input.domain.allocate.vo.QuotaAllocationSummaryVO;
import com.inspur.agriculture.input.domain.allocate.vo.QuotaAllocationVO;
import com.inspur.agriculture.input.domain.allocate.vo.ReceivedQuotaVO;
import com.inspur.agriculture.input.mapper.allocate.QuotaAllocationMapper;
import com.inspur.agriculture.input.mapper.allocate.StateAnnualQuotaMapper;
import com.inspur.agriculture.input.service.allocate.QuotaAllocationService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<QuotaAllocationVO> batchAllocate(QuotaBatchAllocationDTO dto) {
        // Validate parameters
        if (dto.getQuotaId() == null || dto.getYear() == null || dto.getCategoryId() == null
                || dto.getFromDivisionId() == null || dto.getFromDivisionLevel() == null
                || dto.getTotalReceivedQuota() == null || dto.getItems() == null || dto.getItems().isEmpty()) {
            throw new RuntimeException("Invalid parameter: required fields are missing");
        }

        // Validate state quota exists
        StateAnnualQuota stateQuota = stateAnnualQuotaMapper.selectById(dto.getQuotaId());
        if (stateQuota == null) {
            throw new RuntimeException("State quota not found");
        }

        // Calculate total allocation amount
        BigDecimal totalAllocationAmount = dto.getItems().stream()
                .map(QuotaBatchAllocationDTO.AllocationItem::getAllocatedQuota)
                .filter(q -> q != null && q.compareTo(BigDecimal.ZERO) > 0)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Query existing allocations from this division
        QueryWrapper<QuotaAllocation> existingQuery = new QueryWrapper<>();
        existingQuery.eq("from_division_id", dto.getFromDivisionId())
                .eq("year", dto.getYear())
                .eq("category_id", dto.getCategoryId())
                .eq("quota_id", dto.getQuotaId());
        List<QuotaAllocation> existingAllocations = quotaAllocationMapper.selectList(existingQuery);

        BigDecimal existingAllocated = existingAllocations.stream()
                .map(QuotaAllocation::getAllocatedQuota)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Validate total doesn't exceed received quota
        BigDecimal newTotal = existingAllocated.add(totalAllocationAmount);
        if (newTotal.compareTo(dto.getTotalReceivedQuota()) > 0) {
            throw new RuntimeException("Total allocation exceeds received quota. Available: " +
                    dto.getTotalReceivedQuota().subtract(existingAllocated).toPlainString());
        }

        List<QuotaAllocationVO> results = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        for (QuotaBatchAllocationDTO.AllocationItem item : dto.getItems()) {
            if (item.getAllocatedQuota() == null || item.getAllocatedQuota().compareTo(BigDecimal.ZERO) <= 0) {
                continue;
            }

            // Check if allocation already exists for this receiver
            String receiverId = StrUtil.isNotBlank(item.getToDivisionId()) ? item.getToDivisionId() : item.getToFarmerId();
            QuotaAllocation existingAllocation = existingAllocations.stream()
                    .filter(a -> {
                        if (StrUtil.isNotBlank(item.getToDivisionId())) {
                            return item.getToDivisionId().equals(a.getToDivisionId());
                        } else {
                            return item.getToFarmerId().equals(a.getToFarmerId());
                        }
                    })
                    .findFirst()
                    .orElse(null);

            if (existingAllocation != null) {
                // Update existing allocation
                existingAllocation.setAllocatedQuota(existingAllocation.getAllocatedQuota().add(item.getAllocatedQuota()));
                existingAllocation.setTotalAllocatedQuota(existingAllocation.getTotalAllocatedQuota().add(item.getAllocatedQuota()));
                existingAllocation.setRemainingQuota(existingAllocation.getTotalReceivedQuota().subtract(existingAllocation.getTotalAllocatedQuota()));
                existingAllocation.setAllocationStatus(calculateAllocationStatus(existingAllocation.getTotalAllocatedQuota(), existingAllocation.getTotalReceivedQuota()));
                existingAllocation.setModifierId(dto.getOperatorId());
                existingAllocation.setModifierDivisionId(dto.getOperatorDivisionId());
                existingAllocation.setProgressUpdateTime(now);
                existingAllocation.setUpdateTime(now);
                quotaAllocationMapper.updateById(existingAllocation);
            } else {
                // Create new allocation
                QuotaAllocation entity = new QuotaAllocation();
                entity.setAllocationId(IdUtil.fastSimpleUUID());
                entity.setYear(dto.getYear());
                entity.setCategoryId(dto.getCategoryId());
                entity.setFromDivisionId(dto.getFromDivisionId());
                entity.setFromDivisionLevel(dto.getFromDivisionLevel());
                entity.setToDivisionId(item.getToDivisionId());
                entity.setToFarmerId(item.getToFarmerId());
                entity.setAllocatedQuota(item.getAllocatedQuota());
                entity.setQuotaId(dto.getQuotaId());
                entity.setTotalReceivedQuota(item.getAllocatedQuota()); // This item's received is what was allocated to it
                entity.setTotalAllocatedQuota(BigDecimal.ZERO); // Initially not allocated to children
                entity.setRemainingQuota(item.getAllocatedQuota());
                entity.setAllocationStatus(0); // Not yet allocated to children
                entity.setOperatorId(dto.getOperatorId());
                entity.setOperatorDivisionId(dto.getOperatorDivisionId());
                entity.setOperateTime(now);
                entity.setProgressUpdateTime(now);
                entity.setCreateTime(now);
                entity.setUpdateTime(now);

                // Generate allocation name
                String levelName = getLevelName(dto.getFromDivisionLevel());
                String toLevelName = dto.getFromDivisionLevel() < 4 ? getLevelName(dto.getFromDivisionLevel() + 1) : "farmer";
                String receiverName = StrUtil.isNotBlank(item.getToDivisionName()) ? item.getToDivisionName() :
                        (StrUtil.isNotBlank(item.getToFarmerName()) ? item.getToFarmerName() : receiverId);
                entity.setAllocationName(dto.getYear() + "_" + levelName + "_to_" + toLevelName + "_" + receiverName);

                quotaAllocationMapper.insert(entity);
            }
        }

        // Return updated allocation list
        return getAllocationsByQuotaId(dto.getQuotaId(), dto.getFromDivisionId());
    }

    @Override
    public QuotaAllocationSummaryVO getAllocationSummary(String quotaId, String operatorDivisionId) {
        if (StrUtil.isBlank(quotaId)) {
            throw new RuntimeException("Invalid parameter: quotaId is required");
        }

        // Get state quota
        StateAnnualQuota stateQuota = stateAnnualQuotaMapper.selectById(quotaId);
        if (stateQuota == null) {
            throw new RuntimeException("State quota not found");
        }

        QuotaAllocationSummaryVO summary = new QuotaAllocationSummaryVO();
        summary.setQuotaId(quotaId);
        summary.setQuotaName(stateQuota.getQuotaName());
        summary.setYear(stateQuota.getYear());
        summary.setCategoryId(stateQuota.getCategoryId());
        summary.setCategoryName(getCategoryName(stateQuota.getCategoryId()));
        summary.setTotalQuota(stateQuota.getTotalQuota());

        // Get all direct allocations from state level (fromDivisionLevel = 1)
        QueryWrapper<QuotaAllocation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("quota_id", quotaId)
                .eq("from_division_level", 1);
        List<QuotaAllocation> allocations = quotaAllocationMapper.selectList(queryWrapper);

        BigDecimal totalAllocated = allocations.stream()
                .map(QuotaAllocation::getAllocatedQuota)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        summary.setTotalAllocated(totalAllocated);
        summary.setRemainingQuota(stateQuota.getTotalQuota().subtract(totalAllocated));
        summary.setAllocatedChildrenCount(allocations.size());

        // Calculate progress percentage
        BigDecimal progressPercent = BigDecimal.ZERO;
        if (stateQuota.getTotalQuota().compareTo(BigDecimal.ZERO) > 0) {
            progressPercent = totalAllocated.multiply(new BigDecimal("100"))
                    .divide(stateQuota.getTotalQuota(), 2, RoundingMode.HALF_UP);
        }
        summary.setProgressPercent(progressPercent);

        // Set allocation status
        summary.setAllocationStatus(calculateAllocationStatus(totalAllocated, stateQuota.getTotalQuota()));
        summary.setAllocationStatusName(getStatusName(summary.getAllocationStatus()));

        // Build allocation details
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        List<QuotaAllocationSummaryVO.AllocationDetail> details = allocations.stream()
                .map(a -> {
                    QuotaAllocationSummaryVO.AllocationDetail detail = new QuotaAllocationSummaryVO.AllocationDetail();
                    detail.setAllocationId(a.getAllocationId());
                    detail.setToDivisionId(a.getToDivisionId());
                    detail.setToFarmerId(a.getToFarmerId());
                    detail.setAllocatedQuota(a.getAllocatedQuota());
                    if (a.getOperateTime() != null) {
                        detail.setOperateTime(a.getOperateTime().format(formatter));
                    }

                    // Calculate sub-allocation progress (how much has been allocated by receiver)
                    QueryWrapper<QuotaAllocation> subQuery = new QueryWrapper<>();
                    subQuery.eq("from_division_id", a.getToDivisionId())
                            .eq("quota_id", quotaId);
                    List<QuotaAllocation> subAllocations = quotaAllocationMapper.selectList(subQuery);
                    BigDecimal subAllocated = subAllocations.stream()
                            .map(QuotaAllocation::getAllocatedQuota)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    if (a.getAllocatedQuota().compareTo(BigDecimal.ZERO) > 0) {
                        detail.setSubAllocationProgress(subAllocated.multiply(new BigDecimal("100"))
                                .divide(a.getAllocatedQuota(), 2, RoundingMode.HALF_UP));
                    } else {
                        detail.setSubAllocationProgress(BigDecimal.ZERO);
                    }

                    return detail;
                })
                .collect(Collectors.toList());
        summary.setAllocations(details);

        return summary;
    }

    @Override
    public List<ChildDivisionVO> getChildDivisions(String parentDivisionId, Integer year, String categoryId, String quotaId) {
        // This is a mock implementation - in real scenario, this would query from division service
        // For now, return existing allocations as "children" with their allocated amounts
        List<ChildDivisionVO> children = new ArrayList<>();

        if (StrUtil.isBlank(parentDivisionId)) {
            return children;
        }

        // Query existing allocations from this parent
        QueryWrapper<QuotaAllocation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("from_division_id", parentDivisionId);
        if (year != null) {
            queryWrapper.eq("year", year);
        }
        if (StrUtil.isNotBlank(categoryId)) {
            queryWrapper.eq("category_id", categoryId);
        }
        if (StrUtil.isNotBlank(quotaId)) {
            queryWrapper.eq("quota_id", quotaId);
        }

        List<QuotaAllocation> existingAllocations = quotaAllocationMapper.selectList(queryWrapper);

        for (QuotaAllocation allocation : existingAllocations) {
            ChildDivisionVO child = new ChildDivisionVO();
            if (StrUtil.isNotBlank(allocation.getToDivisionId())) {
                child.setDivisionId(allocation.getToDivisionId());
                child.setDivisionName("Division " + allocation.getToDivisionId()); // Would be fetched from division service
                child.setDivisionLevel(allocation.getFromDivisionLevel() + 1);
            }
            child.setParentDivisionId(parentDivisionId);
            child.setAllocatedQuota(allocation.getAllocatedQuota());
            child.setHasAllocation(true);
            child.setExistingAllocationId(allocation.getAllocationId());
            children.add(child);
        }

        return children;
    }

    @Override
    public List<ReceivedQuotaVO> getReceivedQuotas(String divisionId, Integer year, String categoryId) {
        if (StrUtil.isBlank(divisionId)) {
            throw new RuntimeException("Invalid parameter: divisionId is required");
        }

        List<ReceivedQuotaVO> receivedQuotas = new ArrayList<>();

        // Query allocations where this division is the receiver
        QueryWrapper<QuotaAllocation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("to_division_id", divisionId);
        if (year != null) {
            queryWrapper.eq("year", year);
        }
        if (StrUtil.isNotBlank(categoryId)) {
            queryWrapper.eq("category_id", categoryId);
        }
        queryWrapper.orderByDesc("create_time");

        List<QuotaAllocation> allocations = quotaAllocationMapper.selectList(queryWrapper);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        for (QuotaAllocation allocation : allocations) {
            ReceivedQuotaVO vo = new ReceivedQuotaVO();
            vo.setAllocationId(allocation.getAllocationId());
            vo.setQuotaId(allocation.getQuotaId());
            vo.setYear(allocation.getYear());
            vo.setCategoryId(allocation.getCategoryId());
            vo.setCategoryName(getCategoryName(allocation.getCategoryId()));
            vo.setFromDivisionId(allocation.getFromDivisionId());
            vo.setFromDivisionLevel(allocation.getFromDivisionLevel());
            vo.setReceivedQuota(allocation.getAllocatedQuota());
            vo.setCurrentDivisionLevel(allocation.getFromDivisionLevel() + 1);

            // Calculate how much this division has allocated to its children
            QueryWrapper<QuotaAllocation> childQuery = new QueryWrapper<>();
            childQuery.eq("from_division_id", divisionId)
                    .eq("year", allocation.getYear())
                    .eq("category_id", allocation.getCategoryId())
                    .eq("quota_id", allocation.getQuotaId());
            List<QuotaAllocation> childAllocations = quotaAllocationMapper.selectList(childQuery);

            BigDecimal allocatedToChildren = childAllocations.stream()
                    .map(QuotaAllocation::getAllocatedQuota)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            vo.setAllocatedToChildren(allocatedToChildren);
            vo.setRemainingQuota(allocation.getAllocatedQuota().subtract(allocatedToChildren));
            vo.setAllocatedChildrenCount(childAllocations.size());

            // Calculate progress
            BigDecimal progressPercent = BigDecimal.ZERO;
            if (allocation.getAllocatedQuota().compareTo(BigDecimal.ZERO) > 0) {
                progressPercent = allocatedToChildren.multiply(new BigDecimal("100"))
                        .divide(allocation.getAllocatedQuota(), 2, RoundingMode.HALF_UP);
            }
            vo.setProgressPercent(progressPercent);

            // Set status
            vo.setAllocationStatus(calculateAllocationStatus(allocatedToChildren, allocation.getAllocatedQuota()));
            vo.setAllocationStatusName(getStatusName(vo.getAllocationStatus()));

            if (allocation.getOperateTime() != null) {
                vo.setReceiveTime(allocation.getOperateTime().format(formatter));
            }

            // Get quota name from state quota
            StateAnnualQuota stateQuota = stateAnnualQuotaMapper.selectById(allocation.getQuotaId());
            if (stateQuota != null) {
                vo.setQuotaName(stateQuota.getQuotaName());
            }

            receivedQuotas.add(vo);
        }

        return receivedQuotas;
    }

    @Override
    public List<QuotaAllocationVO> getAllocationsByQuotaId(String quotaId, String fromDivisionId) {
        if (StrUtil.isBlank(quotaId)) {
            throw new RuntimeException("Invalid parameter: quotaId is required");
        }

        QueryWrapper<QuotaAllocation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("quota_id", quotaId);
        if (StrUtil.isNotBlank(fromDivisionId)) {
            queryWrapper.eq("from_division_id", fromDivisionId);
        }
        queryWrapper.orderByDesc("create_time");

        List<QuotaAllocation> allocations = quotaAllocationMapper.selectList(queryWrapper);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return allocations.stream()
                .map(a -> {
                    QuotaAllocationVO vo = new QuotaAllocationVO();
                    BeanUtils.copyProperties(a, vo);
                    vo.setAllocationStatusName(getStatusName(a.getAllocationStatus()));
                    vo.setCategoryName(getCategoryName(a.getCategoryId()));
                    if (a.getOperateTime() != null) {
                        vo.setOperateTimeStr(a.getOperateTime().format(formatter));
                    }
                    return vo;
                })
                .collect(Collectors.toList());
    }

    /**
     * Get category name
     */
    private String getCategoryName(String categoryId) {
        if (categoryId == null) {
            return "Unknown";
        }
        switch (categoryId) {
            case "1":
                return "Seed";
            case "2":
                return "Fertilizer";
            case "3":
                return "Pesticide";
            case "4":
                return "Other";
            default:
                return "Unknown";
        }
    }
}
