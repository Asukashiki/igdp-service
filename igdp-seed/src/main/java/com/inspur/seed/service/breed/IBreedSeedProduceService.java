package com.inspur.seed.service.breed;

import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.seed.domain.breed.BreedSeedProduce;
import com.inspur.seed.dto.breed.BreedSeedProduceDTO;
import com.inspur.seed.dto.breed.BreedSeedProduceQueryDTO;
import com.inspur.seed.vo.breed.BreedSeedProduceVO;

import java.math.BigDecimal;
import java.util.List;

/**
 * Breeder Seed 生产Service接口
 *
 * @author igdp
 */
public interface IBreedSeedProduceService extends IService<BreedSeedProduce> {

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

    /**
     * 更新生产批次剩余量(扣减)
     *
     * @param breedSeedProduceBatchId 生产批次ID
     * @param distributeQuantity 分发数量
     * @return 是否更新成功
     */
    boolean updateRemainingQuantity(String breedSeedProduceBatchId, BigDecimal distributeQuantity);

    void delete(String breedSeedProduceBatchId);
}
