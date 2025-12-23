package com.inspur.seed.mapper.breed;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.seed.domain.breed.BreedSeedProduceResult;
import com.inspur.seed.dto.breed.BreedSeedProduceResultQueryDTO;
import com.inspur.seed.vo.breed.BreedSeedProduceResultVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Seed Production Result Mapper
 *
 * @author igdp
 */
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
}
