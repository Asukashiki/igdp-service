package com.inspur.seed.service.breed;

import com.inspur.seed.dto.breed.BreedSeedDistributeDTO;
import com.inspur.seed.dto.breed.BreedSeedDistributeQueryDTO;
import com.inspur.seed.vo.breed.BreedSeedDistributeVO;

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
     * 新增分发数据
     *
     * @param dto 分发数据DTO
     * @return 新增的分发数据
     */
    BreedSeedDistributeVO addDistribute(BreedSeedDistributeDTO dto);
}
