package com.inspur.agriculture.input.controller.allocation;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.inspur.agriculture.input.domain.allocation.dto.AllocationAddDTO;
import com.inspur.agriculture.input.domain.allocation.dto.AllocationDeleteDTO;
import com.inspur.agriculture.input.domain.allocation.dto.AllocationUpdateDTO;
import com.inspur.agriculture.input.domain.allocation.vo.AllocationDetailVO;
import com.inspur.agriculture.input.domain.allocation.vo.AllocationVO;
import com.inspur.agriculture.input.service.allocation.AllocationService;
import com.inspur.common.core.domain.AjaxResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * Zone Allocation Controller
 * 区域分配额度控制器
 */
@RestController
@RequestMapping("/api/allocation/zone")
public class ZoneAllocationController {

    @Resource
    private AllocationService allocationService;

    /**
     * Add zone allocation
     *
     * @param dto Add DTO
     * @return AjaxResult
     */
    @PostMapping("/add")
    public AjaxResult add(@RequestBody AllocationAddDTO dto) {
        try {
            dto.setLevel("zone");
            AllocationVO vo = allocationService.add(dto);
            return AjaxResult.success("Zone allocation added successfully", vo);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * Page query zone allocation
     *
     * @param allocationName Allocation name filter
     * @param year Year filter
     * @param zone Zone filter
     * @param pageNum Page number
     * @param pageSize Page size
     * @return AjaxResult
     */
    @GetMapping("/page")
    public AjaxResult page(@RequestParam(required = false) String allocationName,
                          @RequestParam(required = false) String year,
                          @RequestParam(required = false) String zone,
                          @RequestParam(required = true) Integer pageNum,
                          @RequestParam(required = true) Integer pageSize) {
        try {
            IPage<AllocationVO> page = allocationService.page(pageNum, pageSize, allocationName, year, zone, "zone");
            return AjaxResult.success("Page query successful", page);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * Get zone allocation detail
     *
     * @param id Allocation ID
     * @return AjaxResult
     */
    @GetMapping("/detail")
    public AjaxResult detail(@RequestParam(required = true) String id) {
        try {
            AllocationDetailVO vo = allocationService.detail(id);
            return AjaxResult.success("Detail query successful", vo);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * Update zone allocation
     *
     * @param dto Update DTO
     * @return AjaxResult
     */
    @PostMapping("/update")
    public AjaxResult update(@RequestBody AllocationUpdateDTO dto) {
        try {
            AllocationVO vo = allocationService.update(dto);
            return AjaxResult.success("Zone allocation updated successfully", vo);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * Delete zone allocation
     *
     * @param dto Delete DTO
     * @return AjaxResult
     */
    @PostMapping("/delete")
    public AjaxResult delete(@RequestBody AllocationDeleteDTO dto) {
        try {
            Boolean result = allocationService.delete(dto);
            if (result) {
                return AjaxResult.success("Zone allocation deleted successfully");
            } else {
                return AjaxResult.error("Delete failed");
            }
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }
}
