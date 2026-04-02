package com.inspur.seed.breeding.seedDistribution.service;

import com.inspur.seed.breeding.seedDistribution.domain.dto.BreedSeedDistributeDTO;
import com.inspur.seed.breeding.seedDistribution.domain.dto.BreedSeedDistributeQueryDTO;
import com.inspur.seed.breeding.seedDistribution.domain.vo.BreedSeedDistributeVO;

import java.util.List;

/**
 * Breeder Seed 分发Service接口
 *
 * @author igdp
 */
public interface IBreedSeedDistributeService {

    /**
     * 查询分发数据列表
     *
     * @param queryDTO 查询条件
     * @return 分发数据列表
     */
    List<BreedSeedDistributeVO> getDistributeList(BreedSeedDistributeQueryDTO queryDTO);

    /**
     * 根据分发ID查询分发详情
     *
     * @param distributeId 分发ID
     * @return 分发详情
     */
    BreedSeedDistributeVO getDistributeById(String distributeId);

    /**
     * 新增分发数据
     *
     * @param dto 分发数据DTO
     * @return 新增的分发数据
     */
    BreedSeedDistributeVO addDistribute(BreedSeedDistributeDTO dto);
}
