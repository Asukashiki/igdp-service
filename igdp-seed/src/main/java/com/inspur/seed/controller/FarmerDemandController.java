package com.inspur.seed.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.seed.domain.dto.*;
import com.inspur.seed.domain.vo.FarmerDemandDetailVO;
import com.inspur.seed.domain.vo.FarmerDemandPageVO;
import com.inspur.seed.domain.vo.FarmerInputAggregationVO;
import com.inspur.seed.service.IFarmerDemandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
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

    /**
     * Get farmer demand list by farmerId
     * Returns list of demand items for a specific farmer
     */
    @GetMapping("/getByFarmerId")
    public AjaxResult getByFarmerId(@RequestParam String farmerId) {
        List<FarmerInputAggregationVO> list = farmerDemandService.getDemandByFarmerId(farmerId);
        return AjaxResult.success(list);
    }

    /**
     * Get aggregated statistics of farmer input items
     * Returns aggregated data grouped by input category and type
     */
    @PostMapping("/input/getAggregation")
    public AjaxResult getInputAggregation(@RequestBody DemandOrganDTO demanOrganDTO) {
        return AjaxResult.success(farmerDemandService.getInputAggregation(demanOrganDTO));
    }


    @PostMapping("/input/aggregation")
    public AjaxResult submitInputAggregation(@RequestBody DemandOrganDTO demanOrganDTO) {
        return AjaxResult.success(farmerDemandService.submitInputAggregation(demanOrganDTO));
    }
}
