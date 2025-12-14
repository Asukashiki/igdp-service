package com.inspur.agriculture.input.mapper.allocation;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.inspur.agriculture.input.domain.allocation.entity.Allocation;
import com.inspur.agriculture.input.domain.allocation.vo.AllocationVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * Zone Allocation Mapper
 * 区域分配额度Mapper接口
 */
@Mapper
public interface AllocationMapper extends BaseMapper<Allocation> {

    /**
     * Page query zone allocation with associated data
     *
     * @param page Page object
     * @param allocationName Allocation name filter
     * @param year Year filter
     * @param zone Zone filter
     * @return Page result
     */
    IPage<AllocationVO> selectPageWithDetail(Page<AllocationVO> page,
                                             @Param("allocationName") String allocationName,
                                             @Param("year") String year,
                                             @Param("zone") String zone,
                                             @Param("level") String level);

    /**
     * Get zone allocation detail by ID
     *
     * @param id Allocation ID
     * @return ZoneAllocationVO
     */
    AllocationVO selectDetailById(@Param("id") String id);
}
