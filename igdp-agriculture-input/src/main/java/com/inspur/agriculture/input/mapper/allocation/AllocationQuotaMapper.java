package com.inspur.agriculture.input.mapper.allocation;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.agriculture.input.domain.allocation.entity.AllocationQuota;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Zone Allocation Quota Mapper
 * 区域分配额度配项Mapper接口
 */
@Mapper
public interface AllocationQuotaMapper extends BaseMapper<AllocationQuota> {

    /**
     * Get quota list by allocation ID
     *
     * @param allocationId Allocation ID
     * @return List of quota items
     */
    List<AllocationQuota> selectByAllocationId(@Param("allocationId") String allocationId);

    /**
     * Delete quota items by allocation ID
     *
     * @param allocationId Allocation ID
     * @return Number of deleted records
     */
    int deleteByAllocationId(@Param("allocationId") String allocationId);
}
