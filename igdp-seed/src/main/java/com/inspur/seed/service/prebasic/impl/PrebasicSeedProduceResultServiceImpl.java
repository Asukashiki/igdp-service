package com.inspur.seed.service.prebasic.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.common.exception.ServiceException;
import com.inspur.seed.domain.prebasic.PrebasicSeedProduce;
import com.inspur.seed.domain.prebasic.PrebasicSeedProduceResult;
import com.inspur.seed.dto.prebasic.PrebasicSeedProduceResultQueryDTO;
import com.inspur.seed.mapper.prebasic.PrebasicSeedProduceMapper;
import com.inspur.seed.mapper.prebasic.PrebasicSeedProduceResultMapper;
import com.inspur.seed.service.prebasic.IPrebasicSeedProduceResultService;
import com.inspur.seed.vo.prebasic.PrebasicSeedProduceResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * Pre-basic Seed 生产结果Service实现
 *
 * @author igdp
 */
@Service
public class PrebasicSeedProduceResultServiceImpl extends ServiceImpl<PrebasicSeedProduceResultMapper, PrebasicSeedProduceResult> implements IPrebasicSeedProduceResultService {

    @Autowired
    private PrebasicSeedProduceMapper prebasicSeedProduceMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int submitResult(PrebasicSeedProduceResult result) {
        // 1. 验证批次是否存在
        PrebasicSeedProduce produce = prebasicSeedProduceMapper.selectById(result.getProduceBatchId());
        if (produce == null) {
            throw new ServiceException("Production batch not found");
        }
        
        // 2. 验证批次状态
        if (!"Ongoing".equals(produce.getProduceStatus())) {
            throw new ServiceException("Only ongoing batches can submit results");
        }
        
        // 3. 检查是否已提交过结果
        Long count = baseMapper.countByProduceBatchId(result.getProduceBatchId());
        if (count > 0) {
            throw new ServiceException("Result already submitted for this batch");
        }
        
        // 4. 设置默认值
        if (result.getCollectionDate() == null) {
            result.setCollectionDate(new Date());
        }
        
        // 5. 插入结果记录
        int rows = baseMapper.insert(result);
        
        // 6. 更新批次状态为 Finished，并更新产出数量
        if (rows > 0) {
            produce.setProduceStatus("Finished");
            produce.setProduceSeedQuantity(result.getOutputQuantity());
            produce.setUpdateTime(new Date());
            prebasicSeedProduceMapper.updateById(produce);
        }
        
        return rows;
    }

    @Override
    public List<PrebasicSeedProduceResultVO> getResultList(PrebasicSeedProduceResultQueryDTO queryDTO) {
        return baseMapper.selectResultList(queryDTO);
    }

    @Override
    public PrebasicSeedProduceResultVO getResultById(String resultId) {
        return baseMapper.selectResultById(resultId);
    }
}
