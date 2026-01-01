package com.inspur.seed.service.basic;

import com.inspur.seed.domain.basic.BasicSeedProduceResult;
import com.inspur.seed.dto.basic.BasicSeedProduceResultQueryDTO;
import com.inspur.seed.vo.basic.BasicSeedProduceResultVO;
import java.util.List;

/**
 * Basic Seed 生产结果Service接口
 */
public interface IBasicSeedProduceResultService {
    
    int submitResult(BasicSeedProduceResult result);
    
    List<BasicSeedProduceResultVO> getResultList(BasicSeedProduceResultQueryDTO queryDTO);
    
    BasicSeedProduceResultVO getResultById(String resultId);
}
