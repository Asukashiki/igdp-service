package com.inspur.seed.breeding.breederSeed.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.seed.breeding.breederSeed.domain.entity.BreedSeedProduceResult;
import com.inspur.seed.breeding.breederSeed.domain.dto.BreedSeedProduceResultQueryDTO;
import com.inspur.seed.breeding.breederSeed.domain.vo.BreedSeedProduceResultVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Seed Production Result Mapper
 *
 * @author igdp
 */
@Mapper
public interface BreedSeedProduceResultMapper extends BaseMapper<BreedSeedProduceResult> {

    /**
     * Query Result List
     *
     * @param queryDTO query params
     * @return list
     */
    List<BreedSeedProduceResultVO> getResultList(@Param("query") BreedSeedProduceResultQueryDTO queryDTO);

    /**
     * Get Result By ID
     *
     * @param resultId result ID
     * @return result vo
     */
    BreedSeedProduceResultVO getResultById(@Param("resultId") String resultId);

    BreedSeedProduceResultVO getResultByProduceBatchId(@Param("produceBatchId") String produceBatchId);
}
