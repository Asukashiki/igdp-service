package com.inspur.seed.mapper.breed;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.seed.domain.breed.BreedSeedProduce;
import com.inspur.seed.dto.breed.BreedSeedProduceQueryDTO;
import com.inspur.seed.vo.breed.BreedSeedProduceVO;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * Breeder Seed 生产Mapper接口
 *
 * @author igdp
 */
public interface BreedSeedProduceMapper extends BaseMapper<BreedSeedProduce> {

    /**
     * 查询生产数据列表
     *
     * @param queryDTO 查询条件
     * @return 生产数据列表
     */
    List<BreedSeedProduceVO> selectProduceList(@Param("query") BreedSeedProduceQueryDTO queryDTO);

    /**
     * 根据ID查询生产数据详情
     *
     * @param breedSeedProduceBatchId 生产批次ID
     * @return 生产数据详情
     */
    BreedSeedProduceVO selectProduceById(@Param("breedSeedProduceBatchId") String breedSeedProduceBatchId);

    /**
     * 查询生产批次剩余可分发量
     *
     * @param breedSeedProduceBatchId 生产批次ID
     * @return 剩余可分发量
     */
    BigDecimal selectRemainingQuantity(@Param("breedSeedProduceBatchId") String breedSeedProduceBatchId);

    /**
     * 更新生产批次剩余量(扣减)
     *
     * @param breedSeedProduceBatchId 生产批次ID
     * @param distributeQuantity 分发数量
     * @return 更新行数
     */
    int updateRemainingQuantity(@Param("breedSeedProduceBatchId") String breedSeedProduceBatchId,
                                @Param("distributeQuantity") BigDecimal distributeQuantity);
}
