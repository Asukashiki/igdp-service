package com.inspur.seed.service.basic;

import com.inspur.seed.dto.basic.BasicSeedProduceDTO;
import com.inspur.seed.dto.basic.BasicSeedProduceQueryDTO;
import com.inspur.seed.vo.basic.BasicSeedProduceVO;
import java.util.List;

/**
 * Basic Seed 生产Service接口
 */
public interface IBasicSeedProduceService {
    
    List<BasicSeedProduceVO> getProduceList(BasicSeedProduceQueryDTO queryDTO);
    
    BasicSeedProduceVO getProduceById(String produceBatchId);
    
    BasicSeedProduceVO addProduce(BasicSeedProduceDTO dto);
    
    void voidProduce(String produceBatchId);
}
