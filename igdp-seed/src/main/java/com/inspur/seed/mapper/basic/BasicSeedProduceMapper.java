package com.inspur.seed.mapper.basic;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.seed.domain.basic.BasicSeedProduce;
import com.inspur.seed.dto.basic.BasicSeedProduceQueryDTO;
import com.inspur.seed.vo.basic.BasicSeedProduceVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Basic Seed 生产Mapper
 */
public interface BasicSeedProduceMapper extends BaseMapper<BasicSeedProduce> {
    
    List<BasicSeedProduceVO> selectProduceList(BasicSeedProduceQueryDTO queryDTO);
    
    BasicSeedProduceVO selectProduceById(@Param("produceBatchId") String produceBatchId);
    
    Integer getMaxSequence(@Param("prefix") String prefix);
}
