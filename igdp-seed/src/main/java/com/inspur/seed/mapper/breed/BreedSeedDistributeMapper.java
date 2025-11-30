package com.inspur.seed.mapper.breed;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.seed.domain.breed.BreedSeedDistributeMain;
import com.inspur.seed.dto.breed.BreedSeedDistributeQueryDTO;
import com.inspur.seed.vo.breed.BreedSeedDistributeVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Breeder Seed 分发主表Mapper接口
 *
 * @author igdp
 */
public interface BreedSeedDistributeMapper extends BaseMapper<BreedSeedDistributeMain> {

    /**
     * 查询分发数据列表
     *
     * @param queryDTO 查询条件
     * @return 分发数据列表
     */
    List<BreedSeedDistributeVO> selectDistributeList(@Param("query") BreedSeedDistributeQueryDTO queryDTO);

    /**
     * 根据分发ID查询分发详情(含明细)
     *
     * @param distributeId 分发ID
     * @return 分发详情
     */
    BreedSeedDistributeVO selectDistributeById(@Param("distributeId") String distributeId);
}
