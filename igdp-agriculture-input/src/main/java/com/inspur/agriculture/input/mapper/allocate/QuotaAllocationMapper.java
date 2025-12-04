package com.inspur.agriculture.input.mapper.allocate;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.inspur.agriculture.input.domain.allocate.entity.QuotaAllocation;
import com.inspur.agriculture.input.domain.allocate.vo.QuotaAllocationVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * Quota Allocation Mapper
 * 配额分配Mapper接口
 */
@Mapper
public interface QuotaAllocationMapper extends BaseMapper<QuotaAllocation> {

    /**
     * Page query quota allocation with associated data
     *
     * @param page Page object
     * @param year Year filter
     * @param categoryId Category ID filter
     * @param fromDivisionId From division ID filter
     * @param fromDivisionLevel From division level filter
     * @param allocationStatus Allocation status filter
     * @param operatorDivisionId Operator division ID filter
     * @return Page result
     */
    IPage<QuotaAllocationVO> selectPageWithDetail(Page<QuotaAllocationVO> page,
                                                   @Param("year") Integer year,
                                                   @Param("categoryId") String categoryId,
                                                   @Param("fromDivisionId") String fromDivisionId,
                                                   @Param("fromDivisionLevel") Integer fromDivisionLevel,
                                                   @Param("allocationStatus") Integer allocationStatus,
                                                   @Param("operatorDivisionId") String operatorDivisionId);

    /**
     * Get quota allocation detail by allocation ID
     *
     * @param allocationId Allocation ID
     * @param operatorDivisionId Operator division ID
     * @return QuotaAllocationVO
     */
    QuotaAllocationVO selectDetailById(@Param("allocationId") String allocationId,
                                       @Param("operatorDivisionId") String operatorDivisionId);
}
