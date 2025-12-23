package com.inspur.seed.service.breed;

import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.seed.domain.breed.BreedSeedProduceResult;

import com.inspur.seed.dto.breed.BreedSeedProduceResultQueryDTO;
import com.inspur.seed.vo.breed.BreedSeedProduceResultVO;
import java.util.List;

/**
 * Seed Production Result Service Interface
 *
 * @author igdp
 */
public interface IBreedSeedProduceResultService extends IService<BreedSeedProduceResult> {
    /**
     * Submit production result
     * @param result Result data
     * @return boolean
     */
    boolean submitResult(BreedSeedProduceResult result);

    /**
     * Get result list
     * @param queryDTO query params
     * @return list
     */
    List<BreedSeedProduceResultVO> getResultList(BreedSeedProduceResultQueryDTO queryDTO);

    /**
     * Get result details
     * @param resultId result ID
     * @return result vo
     */
    BreedSeedProduceResultVO getResultById(String resultId);
}
