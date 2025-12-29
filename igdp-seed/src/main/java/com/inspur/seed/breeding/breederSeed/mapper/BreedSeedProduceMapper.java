package com.inspur.seed.breeding.breederSeed.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.seed.breeding.breederSeed.domain.entity.BreedSeedProduce;
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

    /**
     * 根据前缀查询当前最大ID（用于生成6位序列号）
     * 例如：前缀为 P_WHEAT_Pre-Basic_Basic_ 时，返回以此前缀开头的最大 produce_batch_id
     *
     * @param prefix 前缀（包含最后一个下划线）
     * @return 最大ID（可能为null）
     */
    String selectMaxIdByPrefix(@Param("prefix") String prefix);
}
