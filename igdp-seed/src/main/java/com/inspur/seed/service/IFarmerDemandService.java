package com.inspur.seed.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.seed.domain.dto.DemandOrganDTO;
import com.inspur.seed.domain.dto.FarmerDemandAddDTO;
import com.inspur.seed.domain.dto.FarmerDemandPageDTO;
import com.inspur.seed.domain.dto.FarmerDemandUpdateDTO;
import com.inspur.seed.domain.entity.DemandFarmerDetail;
import com.inspur.seed.domain.vo.FarmerDemandDetailVO;
import com.inspur.seed.domain.vo.FarmerDemandPageVO;
import com.inspur.seed.domain.vo.FarmerInputAggregationVO;

import java.util.List;

/**
 * Farmer Demand Service Interface
 *
 * @author igdp
 * @date 2025-12-04
 */
public interface IFarmerDemandService extends IService<DemandFarmerDetail> {

    /**
     * Add farmer demand
     *
     * @param dto Farmer demand add DTO
     * @return Demand ID
     */
    String addFarmerDemand(FarmerDemandAddDTO dto);

    /**
     * Update farmer demand
     *
     * @param dto Farmer demand update DTO
     * @return Success flag
     */
    boolean updateFarmerDemand(FarmerDemandUpdateDTO dto);

    /**
     * Get farmer demand detail
     *
     * @param id Demand ID
     * @return Farmer demand detail VO
     */
    FarmerDemandDetailVO getFarmerDemandDetail(String id);

    /**
     * Query farmer demand page
     *
     * @param dto Farmer demand page query DTO
     * @return Page result
     */
    Page<FarmerDemandPageVO> getFarmerDemandPage(FarmerDemandPageDTO dto);

    /**
     * Delete farmer demand
     *
     * @param id Demand ID
     * @return Success flag
     */
    boolean deleteFarmerDemand(String id);

    /**
     * Get aggregated statistics of farmer input items
     *
     * @return List of aggregated input items grouped by category and type
     */
    List<FarmerInputAggregationVO>  getInputAggregation(DemandOrganDTO demanOrganDTO);

    int submitInputAggregation(DemandOrganDTO demandOrganDTO);
}
