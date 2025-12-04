package com.inspur.agriculture.input.controller.allocate;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.inspur.agriculture.input.domain.allocate.dto.StateAnnualQuotaAddDTO;
import com.inspur.agriculture.input.domain.allocate.dto.StateAnnualQuotaDeleteDTO;
import com.inspur.agriculture.input.domain.allocate.dto.StateAnnualQuotaUpdateDTO;
import com.inspur.agriculture.input.domain.allocate.vo.StateAnnualQuotaVO;
import com.inspur.agriculture.input.service.allocate.StateAnnualQuotaService;
import com.inspur.common.core.domain.AjaxResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * State Annual Quota Controller
 * 州级投入品年度配额控制器
 */
@RestController
@RequestMapping("/api/quota/state-annual")
public class StateAnnualQuotaController {

    @Resource
    private StateAnnualQuotaService stateAnnualQuotaService;

    /**
     * Add state annual quota
     *
     * @param dto Add DTO
     * @return AjaxResult
     */
    @PostMapping("/add")
    public AjaxResult add(@RequestBody StateAnnualQuotaAddDTO dto) {
        try {
            StateAnnualQuotaVO vo = stateAnnualQuotaService.add(dto);
            return AjaxResult.success("State annual quota added successfully", vo);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * Page query state annual quota
     *
     * @param year Year filter
     * @param categoryId Category ID filter
     * @param operatorDivisionId Operator division ID
     * @param pageNum Page number
     * @param pageSize Page size
     * @return AjaxResult
     */
    @GetMapping("/page")
    public AjaxResult page(@RequestParam(required = false) Integer year,
                          @RequestParam(required = false) String categoryId,
                          @RequestParam(required = true) String operatorDivisionId,
                          @RequestParam(required = true) Integer pageNum,
                          @RequestParam(required = true) Integer pageSize) {
        try {
            IPage<StateAnnualQuotaVO> page = stateAnnualQuotaService.page(pageNum, pageSize, year,
                    categoryId, operatorDivisionId);
            return AjaxResult.success("Page query successful", page);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * Get state annual quota detail
     *
     * @param quotaId Quota ID
     * @param operatorDivisionId Operator division ID
     * @return AjaxResult
     */
    @GetMapping("/detail")
    public AjaxResult detail(@RequestParam(required = true) String quotaId,
                            @RequestParam(required = true) String operatorDivisionId) {
        try {
            StateAnnualQuotaVO vo = stateAnnualQuotaService.detail(quotaId, operatorDivisionId);
            return AjaxResult.success("Detail query successful", vo);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * Update state annual quota
     *
     * @param dto Update DTO
     * @return AjaxResult
     */
    @PostMapping("/update")
    public AjaxResult update(@RequestBody StateAnnualQuotaUpdateDTO dto) {
        try {
            StateAnnualQuotaVO vo = stateAnnualQuotaService.update(dto);
            return AjaxResult.success("State annual quota updated successfully", vo);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * Delete state annual quota
     *
     * @param dto Delete DTO
     * @return AjaxResult
     */
    @PostMapping("/delete")
    public AjaxResult delete(@RequestBody StateAnnualQuotaDeleteDTO dto) {
        try {
            Boolean result = stateAnnualQuotaService.delete(dto);
            if (result) {
                return AjaxResult.success("State annual quota deleted successfully");
            } else {
                return AjaxResult.error("Delete failed");
            }
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }
}
