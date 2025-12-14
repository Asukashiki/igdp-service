package com.inspur.agriculture.input.service.allocation;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.agriculture.input.domain.allocation.dto.AllocationAddDTO;
import com.inspur.agriculture.input.domain.allocation.dto.AllocationDeleteDTO;
import com.inspur.agriculture.input.domain.allocation.dto.AllocationUpdateDTO;
import com.inspur.agriculture.input.domain.allocation.entity.Allocation;
import com.inspur.agriculture.input.domain.allocation.vo.AllocationDetailVO;
import com.inspur.agriculture.input.domain.allocation.vo.AllocationVO;

/**
 * Zone Allocation Service
 * 区域分配额度服务接口
 */
public interface AllocationService extends IService<Allocation> {

    /**
     * Add zone allocation
     *
     * @param dto Add DTO
     * @return ZoneAllocationVO
     */
    AllocationVO add(AllocationAddDTO dto);

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
    IPage<AllocationVO> page(Integer pageNum, Integer pageSize, String allocationName, String year, String zone, String level);

    /**
     * Get zone allocation detail
     *
     * @param id Allocation ID
     * @return ZoneAllocationDetailVO
     */
    AllocationDetailVO detail(String id);

    /**
     * Update zone allocation
     *
     * @param dto Update DTO
     * @return ZoneAllocationVO
     */
    AllocationVO update(AllocationUpdateDTO dto);

    /**
     * Delete zone allocation
     *
     * @param dto Delete DTO
     * @return Delete result
     */
    Boolean delete(AllocationDeleteDTO dto);
}
