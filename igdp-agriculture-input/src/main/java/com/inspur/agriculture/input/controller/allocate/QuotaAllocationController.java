package com.inspur.agriculture.input.controller.allocate;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.inspur.agriculture.input.domain.allocate.dto.QuotaAllocationAddDTO;
import com.inspur.agriculture.input.domain.allocate.dto.QuotaAllocationDeleteDTO;
import com.inspur.agriculture.input.domain.allocate.dto.QuotaAllocationUpdateDTO;
import com.inspur.agriculture.input.domain.allocate.dto.QuotaBatchAllocationDTO;
import com.inspur.agriculture.input.domain.allocate.vo.ChildDivisionVO;
import com.inspur.agriculture.input.domain.allocate.vo.QuotaAllocationSummaryVO;
import com.inspur.agriculture.input.domain.allocate.vo.QuotaAllocationVO;
import com.inspur.agriculture.input.domain.allocate.vo.ReceivedQuotaVO;
import com.inspur.agriculture.input.service.allocate.QuotaAllocationService;
import com.inspur.common.core.domain.AjaxResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * Quota Allocation Controller
 * 投入品配额逐级分配控制器
 */
@RestController
@RequestMapping("/api/quota/allocation")
public class QuotaAllocationController {

    @Resource
    private QuotaAllocationService quotaAllocationService;

    /**
     * Add quota allocation
     *
     * @param dto Add DTO
     * @return AjaxResult
     */
    @PostMapping("/add")
    public AjaxResult add(@RequestBody QuotaAllocationAddDTO dto) {
        try {
            QuotaAllocationVO vo = quotaAllocationService.add(dto);
            return AjaxResult.success("Quota allocation added successfully", vo);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * Page query quota allocation
     *
     * @param year Year filter
     * @param categoryId Category ID filter
     * @param fromDivisionId From division ID filter
     * @param fromDivisionLevel From division level filter
     * @param allocationStatus Allocation status filter
     * @param operatorDivisionId Operator division ID
     * @param pageNum Page number
     * @param pageSize Page size
     * @return AjaxResult
     */
    @GetMapping("/page")
    public AjaxResult page(@RequestParam(required = false) Integer year,
                          @RequestParam(required = false) String categoryId,
                          @RequestParam(required = false) String fromDivisionId,
                          @RequestParam(required = false) Integer fromDivisionLevel,
                          @RequestParam(required = false) Integer allocationStatus,
                          @RequestParam(required = true) String operatorDivisionId,
                          @RequestParam(required = true) Integer pageNum,
                          @RequestParam(required = true) Integer pageSize) {
        try {
            IPage<QuotaAllocationVO> page = quotaAllocationService.page(pageNum, pageSize, year,
                    categoryId, fromDivisionId, fromDivisionLevel, allocationStatus, operatorDivisionId);
            return AjaxResult.success("Page query successful", page);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * Get quota allocation detail
     *
     * @param allocationId Allocation ID
     * @param operatorDivisionId Operator division ID
     * @return AjaxResult
     */
    @GetMapping("/detail")
    public AjaxResult detail(@RequestParam(required = true) String allocationId,
                            @RequestParam(required = true) String operatorDivisionId) {
        try {
            QuotaAllocationVO vo = quotaAllocationService.detail(allocationId, operatorDivisionId);
            return AjaxResult.success("Detail query successful", vo);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * Update quota allocation
     *
     * @param dto Update DTO
     * @return AjaxResult
     */
    @PostMapping("/update")
    public AjaxResult update(@RequestBody QuotaAllocationUpdateDTO dto) {
        try {
            QuotaAllocationVO vo = quotaAllocationService.update(dto);
            return AjaxResult.success("Quota allocation updated successfully", vo);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * Delete quota allocation
     *
     * @param dto Delete DTO
     * @return AjaxResult
     */
    @PostMapping("/delete")
    public AjaxResult delete(@RequestBody QuotaAllocationDeleteDTO dto) {
        try {
            Boolean result = quotaAllocationService.delete(dto);
            if (result) {
                return AjaxResult.success("Quota allocation deleted successfully");
            } else {
                return AjaxResult.error("Delete failed");
            }
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * Batch allocate quotas to multiple children
     *
     * @param dto Batch allocation DTO
     * @return AjaxResult
     */
    @PostMapping("/batch")
    public AjaxResult batchAllocate(@RequestBody QuotaBatchAllocationDTO dto) {
        try {
            List<QuotaAllocationVO> result = quotaAllocationService.batchAllocate(dto);
            return AjaxResult.success("Batch allocation successful", result);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * Get allocation summary for a state quota
     *
     * @param quotaId State quota ID
     * @param operatorDivisionId Operator division ID
     * @return AjaxResult
     */
    @GetMapping("/summary")
    public AjaxResult getAllocationSummary(@RequestParam(required = true) String quotaId,
                                           @RequestParam(required = true) String operatorDivisionId) {
        try {
            QuotaAllocationSummaryVO summary = quotaAllocationService.getAllocationSummary(quotaId, operatorDivisionId);
            return AjaxResult.success("Summary query successful", summary);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * Get child divisions available for allocation
     *
     * @param parentDivisionId Parent division ID
     * @param year Year
     * @param categoryId Category ID
     * @param quotaId State quota ID
     * @return AjaxResult
     */
    @GetMapping("/children")
    public AjaxResult getChildDivisions(@RequestParam(required = true) String parentDivisionId,
                                        @RequestParam(required = false) Integer year,
                                        @RequestParam(required = false) String categoryId,
                                        @RequestParam(required = false) String quotaId) {
        try {
            List<ChildDivisionVO> children = quotaAllocationService.getChildDivisions(parentDivisionId, year, categoryId, quotaId);
            return AjaxResult.success("Children query successful", children);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * Get quotas received by a division
     *
     * @param divisionId Division ID
     * @param year Year filter
     * @param categoryId Category ID filter
     * @return AjaxResult
     */
    @GetMapping("/received")
    public AjaxResult getReceivedQuotas(@RequestParam(required = true) String divisionId,
                                        @RequestParam(required = false) Integer year,
                                        @RequestParam(required = false) String categoryId) {
        try {
            List<ReceivedQuotaVO> receivedQuotas = quotaAllocationService.getReceivedQuotas(divisionId, year, categoryId);
            return AjaxResult.success("Received quotas query successful", receivedQuotas);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * Get allocations by state quota ID
     *
     * @param quotaId State quota ID
     * @param fromDivisionId Optional filter by from division
     * @return AjaxResult
     */
    @GetMapping("/by-quota")
    public AjaxResult getAllocationsByQuotaId(@RequestParam(required = true) String quotaId,
                                              @RequestParam(required = false) String fromDivisionId) {
        try {
            List<QuotaAllocationVO> allocations = quotaAllocationService.getAllocationsByQuotaId(quotaId, fromDivisionId);
            return AjaxResult.success("Allocations query successful", allocations);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }
}
