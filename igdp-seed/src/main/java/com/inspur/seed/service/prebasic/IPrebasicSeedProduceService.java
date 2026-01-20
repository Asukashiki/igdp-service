package com.inspur.seed.service.prebasic;

import com.inspur.seed.dto.prebasic.PrebasicSeedProduceDTO;
import com.inspur.seed.dto.prebasic.PrebasicSeedProduceQueryDTO;
import com.inspur.seed.vo.prebasic.PrebasicSeedProduceVO;
import java.util.List;

/**
 * Pre-basic Seed 生产Service接口
 */
public interface IPrebasicSeedProduceService {
    
    List<PrebasicSeedProduceVO> getProduceList(PrebasicSeedProduceQueryDTO queryDTO);
    
    PrebasicSeedProduceVO getProduceById(String produceBatchId);
    
    PrebasicSeedProduceVO addProduce(PrebasicSeedProduceDTO dto);
    
    void voidProduce(String produceBatchId);
}
