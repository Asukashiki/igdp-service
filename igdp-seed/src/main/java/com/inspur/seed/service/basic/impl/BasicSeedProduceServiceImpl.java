package com.inspur.seed.service.basic.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.common.exception.ServiceException;
import com.inspur.common.utils.SecurityUtils;
import com.inspur.seed.domain.basic.BasicSeedProduce;
import com.inspur.seed.domain.prebasic.PrebasicSeedProduce;
import com.inspur.seed.domain.prebasic.PrebasicSeedProduceResult;
import com.inspur.seed.dto.basic.BasicSeedProduceDTO;
import com.inspur.seed.dto.basic.BasicSeedProduceQueryDTO;
import com.inspur.seed.mapper.basic.BasicSeedProduceMapper;
import com.inspur.seed.mapper.prebasic.PrebasicSeedProduceMapper;
import com.inspur.seed.mapper.prebasic.PrebasicSeedProduceResultMapper;
import com.inspur.seed.service.basic.IBasicSeedProduceService;
import com.inspur.seed.vo.basic.BasicSeedProduceVO;
import com.inspur.seed.vo.prebasic.PrebasicSeedProduceResultVO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * Basic Seed 生产Service实现
 *
 * @author igdp
 */
@Service
public class BasicSeedProduceServiceImpl extends ServiceImpl<BasicSeedProduceMapper, BasicSeedProduce> implements IBasicSeedProduceService {

    @Autowired
    private PrebasicSeedProduceMapper prebasicSeedProduceMapper;

    @Autowired
    private PrebasicSeedProduceResultMapper prebasicSeedProduceResultMapper;

    @Override
    public List<BasicSeedProduceVO> getProduceList(BasicSeedProduceQueryDTO queryDTO) {
        return baseMapper.selectProduceList(queryDTO);
    }

    @Override
    public BasicSeedProduceVO getProduceById(String produceBatchId) {
        return baseMapper.selectProduceById(produceBatchId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BasicSeedProduceVO addProduce(BasicSeedProduceDTO dto) {
        // 1. 查询来源批次信息（Pre-basic Seed）
        PrebasicSeedProduce prebasicSeed = prebasicSeedProduceMapper.selectById(dto.getPrebasicSeedBatchId());
        if (prebasicSeed == null) {
            throw new ServiceException("Source Pre-basic batch not found");
        }

        // 2. 查询Pre-basic Seed的生产结果记录并扣减剩余量
        PrebasicSeedProduceResult produceResult = prebasicSeedProduceResultMapper.selectResultByProduceBatchId(dto.getPrebasicSeedBatchId());
        if (produceResult == null) {
            throw new ServiceException("Production result not found for batch: " + dto.getPrebasicSeedBatchId());
        }

        // 验证剩余量是否足够
        if (produceResult.getRemainingQuantity() == null || 
            produceResult.getRemainingQuantity().compareTo(dto.getInputSeedQuantity()) < 0) {
            throw new ServiceException("Insufficient remaining quantity. Available: " + 
                produceResult.getRemainingQuantity() + " kg, Required: " + dto.getInputSeedQuantity() + " kg");
        }

        // 扣减剩余量
        produceResult.setRemainingQuantity(
            produceResult.getRemainingQuantity().subtract(dto.getInputSeedQuantity())
        );
        produceResult.setUpdateTime(LocalDateTime.now());
        prebasicSeedProduceResultMapper.updateById(produceResult);

        // 3. 构建实体对象
        BasicSeedProduce produce = new BasicSeedProduce();
        
        // 生成批次ID
        String batchId = generateBatchId(prebasicSeed.getCropType());
        produce.setProduceBatchId(batchId);
        
        // 用户输入字段
        produce.setProduceBatchName(dto.getProduceBatchName());
        produce.setPrebasicSeedBatchId(dto.getPrebasicSeedBatchId());
        produce.setLandId(dto.getLandId());
        produce.setLandName(dto.getLandName()); // 设置地块名称
        produce.setTime(dto.getTime());
        produce.setInputSeedQuantity(dto.getInputSeedQuantity());
        produce.setOperatorId(dto.getOperatorId());
        
        // 自动带出字段
        produce.setPrebasicSeedBatchName(prebasicSeed.getProduceBatchName());
        produce.setVarietyId(prebasicSeed.getVarietyId());
        produce.setVarietyName(prebasicSeed.getVarietyName());
        produce.setCropType(prebasicSeed.getCropType());
        produce.setBreedBatchId(prebasicSeed.getBreedBatchId());
        produce.setBreedBatchName(prebasicSeed.getBreedBatchName());
        produce.setTrialId(prebasicSeed.getTrialId());
        produce.setTrialName(prebasicSeed.getTrialName());
        
        // 获取操作人姓名
        try {
            String operatorName = SecurityUtils.getUsername();
            produce.setOperatorName(operatorName);
        } catch (Exception e) {
            produce.setOperatorName("System");
        }
        
        // 固定值字段
        produce.setFromSeedLevel("Pre-Basic");
        produce.setToSeedLevel("Basic");
        produce.setProduceStatus("Ongoing");
        produce.setFlowStatus("S1"); // 初始状态：待审核
        produce.setProduceSeedQuantity(null);
        
        // 审计字段
        Date now = new Date();
        produce.setCreateTime(now);
        produce.setUpdateTime(now);
        
        // 4. 插入数据库
        baseMapper.insert(produce);
        
        // 5. 返回VO
        return getProduceById(batchId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void voidProduce(String produceBatchId) {
        BasicSeedProduce produce = baseMapper.selectById(produceBatchId);
        if (produce == null) {
            throw new ServiceException("Batch not found");
        }
        
        if ("Void".equals(produce.getProduceStatus())) {
            throw new ServiceException("Batch already voided");
        }
        
        if ("Finished".equals(produce.getProduceStatus())) {
            throw new ServiceException("Cannot void finished batch");
        }
        
        // 验证审核状态：已审批的记录不能作废
        if ("S2".equals(produce.getFlowStatus())) {
            throw new ServiceException("Cannot void approved batch");
        }
        
        // 验证审核状态：已作废的记录不能再次作废
        if ("S10".equals(produce.getFlowStatus())) {
            throw new ServiceException("Batch already voided");
        }
        
        // 归还剩余量到Pre-basic Seed
        PrebasicSeedProduceResult produceResult = prebasicSeedProduceResultMapper.selectResultByProduceBatchId(produce.getPrebasicSeedBatchId());
        if (produceResult != null) {
            produceResult.setRemainingQuantity(
                produceResult.getRemainingQuantity().add(produce.getInputSeedQuantity())
            );
            produceResult.setUpdateTime(LocalDateTime.now());
            prebasicSeedProduceResultMapper.updateById(produceResult);
        }
        
        // 作废时只修改审核状态，不修改生产状态
        produce.setFlowStatus("S10"); // 作废状态
        produce.setUpdateTime(new Date());
        baseMapper.updateById(produce);
    }

    /**
     * 生成批次ID
     * 格式: BASIC_{作物代码}_{年度}_{序列号6位}
     */
    private String generateBatchId(String cropType) {
        // cropType 直接使用，由字典接口管理（如：WHEAT, MAIZE, RICE, BARLEY）
        int year = LocalDate.now().getYear();
        String prefix = "BASIC_" + cropType + "_" + year + "_";
        
        Integer maxSeq = baseMapper.getMaxSequence(prefix);
        int nextSeq = (maxSeq == null ? 0 : maxSeq) + 1;
        
        String sequence = String.format("%06d", nextSeq);
        return prefix + sequence;
    }
}
