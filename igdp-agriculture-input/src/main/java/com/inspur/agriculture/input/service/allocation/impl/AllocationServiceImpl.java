package com.inspur.agriculture.input.service.allocation.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.agriculture.input.domain.allocation.dto.AllocationAddDTO;
import com.inspur.agriculture.input.domain.allocation.dto.AllocationDeleteDTO;
import com.inspur.agriculture.input.domain.allocation.dto.AllocationUpdateDTO;
import com.inspur.agriculture.input.domain.allocation.entity.Allocation;
import com.inspur.agriculture.input.domain.allocation.entity.AllocationDemand;
import com.inspur.agriculture.input.domain.allocation.entity.AllocationQuota;
import com.inspur.agriculture.input.domain.allocation.vo.AllocationDetailVO;
import com.inspur.agriculture.input.domain.allocation.vo.AllocationVO;
import com.inspur.agriculture.input.mapper.allocation.AllocationDemandMapper;
import com.inspur.agriculture.input.mapper.allocation.AllocationMapper;
import com.inspur.agriculture.input.mapper.allocation.AllocationQuotaMapper;
import com.inspur.agriculture.input.service.allocation.AllocationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Zone Allocation Service Implementation
 * 区域分配额度服务实现类
 */
@Service
public class AllocationServiceImpl extends ServiceImpl<AllocationMapper, Allocation> implements AllocationService {

    @Resource
    private AllocationMapper allocationMapper;

    @Resource
    private AllocationDemandMapper allocationDemandMapper;

    @Resource
    private AllocationQuotaMapper allocationQuotaMapper;

    /**
     * Add zone allocation
     *
     * @param dto Add DTO
     * @return ZoneAllocationVO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AllocationVO add(AllocationAddDTO dto) {
        // Save main allocation
        Allocation allocation = new Allocation();
        allocation.setAllocationName(dto.getAllocationName());
        allocation.setYear(dto.getYear());
        allocation.setZone(dto.getZone());
        allocation.setZoneName(dto.getZoneName());
        allocation.setLevel(dto.getLevel());
        allocation.setCreateTime(LocalDateTime.now());
        allocation.setUpdateTime(LocalDateTime.now());
        allocationMapper.insert(allocation);

        // Save demand items
        List<AllocationDemand> demandList = dto.getDemandList();
        if (demandList != null && !demandList.isEmpty()) {
            for (AllocationDemand demand : demandList) {
                demand.setAllocationId(allocation.getId());
                allocationDemandMapper.insert(demand);
            }
        }

        // Save quota items
        List<AllocationQuota> quotaList = dto.getQuotaList();
        if (quotaList != null && !quotaList.isEmpty()) {
            for (AllocationQuota quota : quotaList) {
                quota.setAllocationId(allocation.getId());
                allocationQuotaMapper.insert(quota);
            }
        }

        // Return VO
        AllocationVO vo = new AllocationVO();
        vo.setId(allocation.getId());
        vo.setAllocationName(allocation.getAllocationName());
        vo.setYear(allocation.getYear());
        vo.setZone(allocation.getZone());
        vo.setCreateTime(allocation.getCreateTime());
        vo.setUpdateTime(allocation.getUpdateTime());
        return vo;
    }

    /**
     * Page query zone allocation
     *
     * @param pageNum Page number
     * @param pageSize Page size
     * @param allocationName Allocation name filter
     * @param year Year filter
     * @param zone Zone filter
     * @return IPage result
     */
    @Override
    public IPage<AllocationVO> page(Integer pageNum, Integer pageSize, String allocationName, String year, String zone, String level) {
        Page<AllocationVO> page = new Page<>(pageNum, pageSize);
        return allocationMapper.selectPageWithDetail(page, allocationName, year, zone, level);
    }

    /**
     * Get zone allocation detail
     *
     * @param id Allocation ID
     * @return ZoneAllocationDetailVO
     */
    @Override
    public AllocationDetailVO detail(String id) {
        AllocationDetailVO detailVO = new AllocationDetailVO();

        // Get main allocation
        AllocationVO main = allocationMapper.selectDetailById(id);
        detailVO.setMain(main);

        // Get demand list
        List<AllocationDemand> demandList = allocationDemandMapper.selectByAllocationId(id);
        detailVO.setDemandList(demandList);

        // Get quota list
        List<AllocationQuota> quotaList = allocationQuotaMapper.selectByAllocationId(id);
        detailVO.setQuotaList(quotaList);

        return detailVO;
    }

    /**
     * Update zone allocation
     *
     * @param dto Update DTO
     * @return ZoneAllocationVO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AllocationVO update(AllocationUpdateDTO dto) {
        // Update main allocation
        Allocation allocation = new Allocation();
        allocation.setId(dto.getId());
        allocation.setAllocationName(dto.getAllocationName());
        allocation.setYear(dto.getYear());
        allocation.setZone(dto.getZone());
        allocation.setUpdateTime(LocalDateTime.now());
        allocationMapper.updateById(allocation);

        // Delete old demand and quota items
        allocationDemandMapper.deleteByAllocationId(dto.getId());
        allocationQuotaMapper.deleteByAllocationId(dto.getId());

        // Save new demand items
        List<AllocationDemand> demandList = dto.getDemandList();
        if (demandList != null && !demandList.isEmpty()) {
            for (AllocationDemand demand : demandList) {
                demand.setAllocationId(dto.getId());
                allocationDemandMapper.insert(demand);
            }
        }

        // Save new quota items
        List<AllocationQuota> quotaList = dto.getQuotaList();
        if (quotaList != null && !quotaList.isEmpty()) {
            for (AllocationQuota quota : quotaList) {
                quota.setAllocationId(dto.getId());
                allocationQuotaMapper.insert(quota);
            }
        }

        // Return VO
        AllocationVO vo = new AllocationVO();
        vo.setId(allocation.getId());
        vo.setAllocationName(allocation.getAllocationName());
        vo.setYear(allocation.getYear());
        vo.setZone(allocation.getZone());
        vo.setCreateTime(allocation.getCreateTime());
        vo.setUpdateTime(allocation.getUpdateTime());
        return vo;
    }

    /**
     * Delete zone allocation
     *
     * @param dto Delete DTO
     * @return Delete result
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean delete(AllocationDeleteDTO dto) {
        // Delete demand and quota items first
        allocationDemandMapper.deleteByAllocationId(dto.getId());
        allocationQuotaMapper.deleteByAllocationId(dto.getId());

        // Delete main allocation
        return allocationMapper.deleteById(dto.getId()) > 0;
    }
}
