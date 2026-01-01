package com.inspur.seed.mapper.prebasic;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.seed.domain.prebasic.PrebasicSeedProduce;
import com.inspur.seed.dto.prebasic.PrebasicSeedProduceQueryDTO;
import com.inspur.seed.vo.prebasic.PrebasicSeedProduceVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Pre-basic Seed 生产Mapper
 */
public interface PrebasicSeedProduceMapper extends BaseMapper<PrebasicSeedProduce> {
    
    List<PrebasicSeedProduceVO> selectProduceList(PrebasicSeedProduceQueryDTO queryDTO);
    
    PrebasicSeedProduceVO selectProduceById(@Param("produceBatchId") String produceBatchId);
    
    Integer getMaxSequence(@Param("prefix") String prefix);
}
