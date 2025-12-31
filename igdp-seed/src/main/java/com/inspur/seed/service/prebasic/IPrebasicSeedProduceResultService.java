package com.inspur.seed.service.prebasic;

import com.inspur.seed.domain.prebasic.PrebasicSeedProduceResult;
import com.inspur.seed.dto.prebasic.PrebasicSeedProduceResultQueryDTO;
import com.inspur.seed.vo.prebasic.PrebasicSeedProduceResultVO;
import java.util.List;

/**
 * Pre-basic Seed 生产结果Service接口
 */
public interface IPrebasicSeedProduceResultService {
    
    int submitResult(PrebasicSeedProduceResult result);
    
    List<PrebasicSeedProduceResultVO> getResultList(PrebasicSeedProduceResultQueryDTO queryDTO);
    
    PrebasicSeedProduceResultVO getResultById(String resultId);
}
