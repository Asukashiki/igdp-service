package com.inspur.seed.mapper.prebasic;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.seed.domain.prebasic.PrebasicSeedProduceResult;
import com.inspur.seed.dto.prebasic.PrebasicSeedProduceResultQueryDTO;
import com.inspur.seed.vo.prebasic.PrebasicSeedProduceResultVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Pre-basic Seed 生产结果Mapper
 */
public interface PrebasicSeedProduceResultMapper extends BaseMapper<PrebasicSeedProduceResult> {
    
    List<PrebasicSeedProduceResultVO> selectResultList(PrebasicSeedProduceResultQueryDTO queryDTO);
    
    PrebasicSeedProduceResultVO selectResultById(@Param("resultId") String resultId);
    
    Long countByProduceBatchId(@Param("produceBatchId") String produceBatchId);
}
