package com.inspur.seed.service.basic.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.common.exception.ServiceException;
import com.inspur.seed.domain.basic.BasicSeedProduce;
import com.inspur.seed.domain.basic.BasicSeedProduceResult;
import com.inspur.seed.dto.basic.BasicSeedProduceResultQueryDTO;
import com.inspur.seed.mapper.basic.BasicSeedProduceMapper;
import com.inspur.seed.mapper.basic.BasicSeedProduceResultMapper;
import com.inspur.seed.service.basic.IBasicSeedProduceResultService;
import com.inspur.seed.vo.basic.BasicSeedProduceResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Basic Seed 生产结果Service实现
 *
 * @author igdp
 */
@Service
public class BasicSeedProduceResultServiceImpl extends ServiceImpl<BasicSeedProduceResultMapper, BasicSeedProduceResult> implements IBasicSeedProduceResultService {

    @Autowired
    private BasicSeedProduceMapper basicSeedProduceMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int submitResult(BasicSeedProduceResult result) {
        // 1. 验证批次是否存在
        BasicSeedProduce produce = basicSeedProduceMapper.selectById(result.getProduceBatchId());
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
        
        // 初始化剩余量等于生产量
        if (result.getRemainingQuantity() == null) {
            result.setRemainingQuantity(result.getOutputQuantity());
        }
        
        // 5. 插入结果记录
        int rows = baseMapper.insert(result);
        
        // 6. 更新批次状态为 Finished，审核状态为 S2（已审批），并更新产出数量
        if (rows > 0) {
            produce.setProduceStatus("Finished");
            produce.setFlowStatus("S2"); // 设置审核状态为已审批
            produce.setProduceSeedQuantity(result.getOutputQuantity());
            produce.setUpdateTime(new Date());
            basicSeedProduceMapper.updateById(produce);
        }
        
        return rows;
    }

    @Override
    public List<BasicSeedProduceResultVO> getResultList(BasicSeedProduceResultQueryDTO queryDTO) {
        return baseMapper.selectResultList(queryDTO);
    }

    @Override
    public BasicSeedProduceResultVO getResultById(String resultId) {
        return baseMapper.selectResultById(resultId);
    }
}
