package com.inspur.seed.mapper.breed;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.seed.domain.breed.BreedSeedProduce;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
 * Breeder Seed 生产Mapper接口
 *
 * @author igdp
 */
@Mapper
public interface BreedSeedProduceMapper extends BaseMapper<BreedSeedProduce> {
    /**
     * 查询生产批次剩余可分发量
     *
     * @param breedSeedProduceBatchId 生产批次ID
     * @return 剩余可分发量
     */
    BigDecimal selectRemainingQuantity(@Param("breedSeedProduceBatchId") String breedSeedProduceBatchId);

}
