package com.inspur.agriculture.input.service.allocate;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.agriculture.input.domain.allocate.dto.QuotaAllocationAddDTO;
import com.inspur.agriculture.input.domain.allocate.dto.QuotaAllocationDeleteDTO;
import com.inspur.agriculture.input.domain.allocate.dto.QuotaAllocationUpdateDTO;
import com.inspur.agriculture.input.domain.allocate.dto.QuotaBatchAllocationDTO;
import com.inspur.agriculture.input.domain.allocate.entity.QuotaAllocation;
import com.inspur.agriculture.input.domain.allocate.vo.ChildDivisionVO;
import com.inspur.agriculture.input.domain.allocate.vo.QuotaAllocationSummaryVO;
import com.inspur.agriculture.input.domain.allocate.vo.QuotaAllocationVO;
import com.inspur.agriculture.input.domain.allocate.vo.ReceivedQuotaVO;

import java.util.List;

/**
 * Quota Allocation Service
 * 配额分配服务接口
 */
public interface QuotaAllocationService extends IService<QuotaAllocation> {

    /**
     * Add quota allocation
     *
     * @param dto Add DTO
     * @return QuotaAllocationVO
     */
    QuotaAllocationVO add(QuotaAllocationAddDTO dto);

    /**
     * Page query quota allocation
     *
     * @param pageNum Page number
     * @param pageSize Page size
     * @param year Year filter
     * @param categoryId Category ID filter
     * @param fromDivisionId From division ID filter
     * @param fromDivisionLevel From division level filter
     * @param allocationStatus Allocation status filter
     * @param operatorDivisionId Operator division ID filter
     * @return IPage result
     */
    IPage<QuotaAllocationVO> page(Integer pageNum, Integer pageSize, Integer year,
                                  String categoryId, String fromDivisionId,
                                  Integer fromDivisionLevel, Integer allocationStatus,
                                  String operatorDivisionId);

    /**
     * Get quota allocation detail
     *
     * @param allocationId Allocation ID
     * @param operatorDivisionId Operator division ID
     * @return QuotaAllocationVO
     */
    QuotaAllocationVO detail(String allocationId, String operatorDivisionId);

    /**
     * Update quota allocation
     *
     * @param dto Update DTO
     * @return QuotaAllocationVO
     */
    QuotaAllocationVO update(QuotaAllocationUpdateDTO dto);

    /**
     * Delete quota allocation
     *
     * @param dto Delete DTO
     * @return Delete result
     */
    Boolean delete(QuotaAllocationDeleteDTO dto);

    /**
     * Batch allocate quotas to multiple children
     *
     * @param dto Batch allocation DTO
     * @return List of created allocations
     */
    List<QuotaAllocationVO> batchAllocate(QuotaBatchAllocationDTO dto);

    /**
     * Get allocation summary for a state quota
     *
     * @param quotaId State quota ID
     * @param operatorDivisionId Operator division ID
     * @return Allocation summary
     */
    QuotaAllocationSummaryVO getAllocationSummary(String quotaId, String operatorDivisionId);

    /**
     * Get child divisions available for allocation
     *
     * @param parentDivisionId Parent division ID
     * @param year Year
     * @param categoryId Category ID
     * @param quotaId State quota ID
     * @return List of child divisions
     */
    List<ChildDivisionVO> getChildDivisions(String parentDivisionId, Integer year, String categoryId, String quotaId);

    /**
     * Get quotas received by a division
     *
     * @param divisionId Division ID
     * @param year Year filter (optional)
     * @param categoryId Category ID filter (optional)
     * @return List of received quotas
     */
    List<ReceivedQuotaVO> getReceivedQuotas(String divisionId, Integer year, String categoryId);

    /**
     * Get allocations by state quota ID
     *
     * @param quotaId State quota ID
     * @param fromDivisionId Optional filter by from division
     * @return List of allocations
     */
    List<QuotaAllocationVO> getAllocationsByQuotaId(String quotaId, String fromDivisionId);
}
