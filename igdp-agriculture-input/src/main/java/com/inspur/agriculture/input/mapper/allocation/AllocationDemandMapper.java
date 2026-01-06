package com.inspur.agriculture.input.mapper.allocation;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.agriculture.input.domain.allocation.entity.AllocationDemand;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Zone Allocation Demand Mapper
 * 区域分配额度需求项Mapper接口
 */
@Mapper
public interface AllocationDemandMapper extends BaseMapper<AllocationDemand> {

    /**
     * Get demand list by allocation ID
     *
     * @param allocationId Allocation ID
     * @return List of demand items
     */
    List<AllocationDemand> selectByAllocationId(@Param("allocationId") String allocationId);

    /**
     * Delete demand items by allocation ID
     *
     * @param allocationId Allocation ID
     * @return Number of deleted records
     */
    int deleteByAllocationId(@Param("allocationId") String allocationId);
}
