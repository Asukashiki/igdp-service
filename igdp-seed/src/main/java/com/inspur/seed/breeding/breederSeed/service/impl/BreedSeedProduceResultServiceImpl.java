package com.inspur.seed.breeding.breederSeed.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.seed.breeding.breederSeed.domain.entity.BreedSeedProduce;
import com.inspur.seed.breeding.breederSeed.domain.entity.BreedSeedProduceResult;
import com.inspur.seed.breeding.breederSeed.mapper.BreedSeedProduceResultMapper;
import com.inspur.seed.breeding.breederSeed.service.IBreedSeedProduceResultService;
import com.inspur.seed.breeding.breederSeed.service.IBreedSeedProduceService;
import com.inspur.seed.breeding.breederSeed.domain.vo.BreedSeedProduceVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inspur.seed.breeding.breederSeed.domain.dto.BreedSeedProduceResultQueryDTO;
import com.inspur.seed.breeding.breederSeed.domain.vo.BreedSeedProduceResultVO;
import java.util.List;
import java.util.Date;

/**
 * Seed Production Result Service Implementation
 *
 * @author igdp
 */
@Service
public class BreedSeedProduceResultServiceImpl extends ServiceImpl<BreedSeedProduceResultMapper, BreedSeedProduceResult> implements IBreedSeedProduceResultService {

    @Autowired
    private IBreedSeedProduceService breedSeedProduceService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean submitResult(BreedSeedProduceResult result) {
        // 1. Save result
        if (result.getCollectionDate() == null) {
            result.setCollectionDate(new Date());
        }
        boolean saveResult = this.save(result);
        if (!saveResult) {
            return false;
        }

        // 2. Update production batch status and quantity
        BreedSeedProduce produce = new BreedSeedProduce();
        produce.setProduceBatchId(result.getProduceBatchId());
        produce.setProduceStatus("Finished");
        produce.setProduceSeedQuantrity(result.getProducedAmount());
        produce.setUpdateTime(new Date());

        // If operator is available in result, update it in main table too (optional, but good for tracking who finished it)
        if (result.getOperator() != null) {
            // Mapping operator name to operatorName (Note: operatorId might be missing if not passed)
            produce.setOperatorName(result.getOperator());
        }

        return breedSeedProduceService.updateById(produce);
    }

    @Override
    public List<BreedSeedProduceResultVO> getResultList(BreedSeedProduceResultQueryDTO queryDTO) {
        return baseMapper.getResultList(queryDTO);
    }

    @Override
    public BreedSeedProduceResultVO getResultById(String resultId) {
        BreedSeedProduceResultVO breedSeedProduceResultVO = baseMapper.getResultById(resultId);
        BreedSeedProduceVO breedSeedProduceVO = breedSeedProduceService.getProduceById(breedSeedProduceResultVO.getProduceBatchId());
        breedSeedProduceResultVO.setProduceBatchName(breedSeedProduceVO.getProduceBatchName());
        breedSeedProduceResultVO.setVarietyName(breedSeedProduceVO.getVarietyName());
        breedSeedProduceResultVO.setBreedBatchName(breedSeedProduceVO.getBreedBatchName());
        breedSeedProduceResultVO.setTrialName(breedSeedProduceVO.getTrialName());
        breedSeedProduceResultVO.setCropType(breedSeedProduceVO.getCropType());
        breedSeedProduceResultVO.setLandName(breedSeedProduceVO.getLandName());
        breedSeedProduceResultVO.setInputSeedQuantity(breedSeedProduceVO.getInputSeedQuantity());
        breedSeedProduceResultVO.setFromSeedLevel(breedSeedProduceVO.getFromSeedLevel());
        breedSeedProduceResultVO.setToSeedLevel(breedSeedProduceVO.getToSeedLevel());
        breedSeedProduceResultVO.setProduceSeedQuantrity(breedSeedProduceVO.getProduceSeedQuantrity());
        breedSeedProduceResultVO.setLandId(breedSeedProduceVO.getLandId());
        breedSeedProduceResultVO.setVarietyId(breedSeedProduceVO.getVarietyId());
        breedSeedProduceResultVO.setTrialId(breedSeedProduceVO.getTrialId());
        breedSeedProduceResultVO.setBreedBatchId(breedSeedProduceVO.getBreedBatchId());
        breedSeedProduceResultVO.setProduceBatchId(breedSeedProduceVO.getProduceBatchId());

        return breedSeedProduceResultVO;
    }
}
