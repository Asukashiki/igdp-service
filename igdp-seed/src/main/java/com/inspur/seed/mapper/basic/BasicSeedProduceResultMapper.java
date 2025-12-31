package com.inspur.seed.mapper.basic;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.seed.domain.basic.BasicSeedProduceResult;
import com.inspur.seed.dto.basic.BasicSeedProduceResultQueryDTO;
import com.inspur.seed.vo.basic.BasicSeedProduceResultVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Basic Seed 生产结果Mapper
 */
public interface BasicSeedProduceResultMapper extends BaseMapper<BasicSeedProduceResult> {
    
    List<BasicSeedProduceResultVO> selectResultList(BasicSeedProduceResultQueryDTO queryDTO);
    
    BasicSeedProduceResultVO selectResultById(@Param("resultId") String resultId);
    
    Long countByProduceBatchId(@Param("produceBatchId") String produceBatchId);
    
    BasicSeedProduceResult getResultByProduceBatchId(@Param("produceBatchId") String produceBatchId);
}
