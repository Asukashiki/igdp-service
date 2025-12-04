package com.inspur.agriculture.input.service.allocate;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.agriculture.input.domain.allocate.dto.QuotaAllocationAddDTO;
import com.inspur.agriculture.input.domain.allocate.dto.QuotaAllocationDeleteDTO;
import com.inspur.agriculture.input.domain.allocate.dto.QuotaAllocationUpdateDTO;
import com.inspur.agriculture.input.domain.allocate.entity.QuotaAllocation;
import com.inspur.agriculture.input.domain.allocate.vo.QuotaAllocationVO;

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
}
