package com.inspur.seed.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.seed.domain.dto.FarmerDemandAddDTO;
import com.inspur.seed.domain.dto.FarmerDemandDeleteDTO;
import com.inspur.seed.domain.dto.FarmerDemandPageDTO;
import com.inspur.seed.domain.dto.FarmerDemandUpdateDTO;
import com.inspur.seed.domain.vo.FarmerDemandDetailVO;
import com.inspur.seed.domain.vo.FarmerDemandPageVO;
import com.inspur.seed.service.IFarmerDemandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Farmer Demand Controller
 * DA Farmer Demand Entry Module
 *
 * @author igdp
 * @date 2025-12-04
 */
@RestController
@RequestMapping("/seed/demand/farmer")
public class FarmerDemandController {

    @Autowired
    private IFarmerDemandService farmerDemandService;

    /**
     * Add farmer demand
     */
    @PostMapping("/add")
    public AjaxResult add(@Validated @RequestBody FarmerDemandAddDTO dto) {
        String demandId = farmerDemandService.addFarmerDemand(dto);
        Map<String, String> result = new HashMap<>();
        result.put("id", demandId);
        return AjaxResult.success("Operation successful", result);
    }

    /**
     * Update farmer demand
     */
    @PostMapping("/update")
    public AjaxResult update(@Validated @RequestBody FarmerDemandUpdateDTO dto) {
        farmerDemandService.updateFarmerDemand(dto);
        return AjaxResult.success("Operation successful");
    }

    /**
     * Get farmer demand detail
     */
    @GetMapping("/detail")
    public AjaxResult getDetail(@RequestParam String id) {
        FarmerDemandDetailVO detail = farmerDemandService.getFarmerDemandDetail(id);
        return AjaxResult.success("Operation successful", detail);
    }

    /**
     * Query farmer demand page list
     */
    @PostMapping("/page")
    public AjaxResult getPage(@RequestBody FarmerDemandPageDTO dto) {
        Page<FarmerDemandPageVO> page = farmerDemandService.getFarmerDemandPage(dto);
        return AjaxResult.success("Operation successful", page);
    }

    /**
     * Delete farmer demand
     */
    @PostMapping("/delete")
    public AjaxResult delete(@Validated @RequestBody FarmerDemandDeleteDTO dto) {
        farmerDemandService.deleteFarmerDemand(dto.getId());
        return AjaxResult.success("Deleted successfully");
    }
}
