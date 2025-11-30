package com.inspur.seed.service.breed;

import com.inspur.seed.dto.breed.BreedSeedProduceDTO;
import com.inspur.seed.dto.breed.BreedSeedProduceQueryDTO;
import com.inspur.seed.vo.breed.BreedSeedProduceVO;

import java.util.List;

/**
 * Breeder Seed 生产Service接口
 *
 * @author igdp
 */
public interface IBreedSeedProduceService {

    /**
     * 查询生产数据列表
     *
     * @param queryDTO 查询条件
     * @return 生产数据列表
     */
    List<BreedSeedProduceVO> getProduceList(BreedSeedProduceQueryDTO queryDTO);

    /**
     * 查询生产数据详情
     *
     * @param breedSeedProduceBatchId 生产批次ID
     * @return 生产数据详情
     */
    BreedSeedProduceVO getProduceById(String breedSeedProduceBatchId);

    /**
     * 新增生产数据
     *
     * @param dto 生产数据DTO
     * @return 新增的生产数据
     */
    BreedSeedProduceVO addProduce(BreedSeedProduceDTO dto);

    void delete(String breedSeedProduceBatchId);
}
