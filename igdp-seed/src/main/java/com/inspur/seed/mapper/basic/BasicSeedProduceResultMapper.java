package com.inspur.seed.mapper.basic;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.seed.domain.basic.BasicSeedProduceResult;
import com.inspur.seed.dto.basic.BasicSeedProduceResultQueryDTO;
import com.inspur.seed.vo.basic.BasicSeedProduceResultVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Basic Seed 生产结果Mapper
 */
public interface BasicSeedProduceResultMapper extends BaseMapper<BasicSeedProduceResult> {
    
    List<BasicSeedProduceResultVO> selectResultList(BasicSeedProduceResultQueryDTO queryDTO);
    
    BasicSeedProduceResultVO selectResultById(@Param("resultId") String resultId);
    
    Long countByProduceBatchId(@Param("produceBatchId") String produceBatchId);
    
    @Select("SELECT * FROM basic_seed_produce_result WHERE produce_batch_id = #{produceBatchId}")
    BasicSeedProduceResult selectResultByProduceBatchId(@Param("produceBatchId") String produceBatchId);
}
