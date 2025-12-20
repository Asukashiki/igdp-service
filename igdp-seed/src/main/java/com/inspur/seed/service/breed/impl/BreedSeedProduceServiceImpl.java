package com.inspur.seed.service.breed.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.common.exception.ServiceException;
import com.inspur.common.utils.SecurityUtils;
import com.inspur.common.utils.uuid.IdUtils;
import com.inspur.seed.domain.breed.BreedSeedDistributeDetail;
import com.inspur.seed.domain.breed.BreedSeedProduce;
import com.inspur.seed.dto.breed.BreedSeedProduceDTO;
import com.inspur.seed.dto.breed.BreedSeedProduceQueryDTO;
import com.inspur.seed.mapper.breed.BreedSeedDistributeDetailMapper;
import com.inspur.seed.mapper.breed.BreedSeedProduceMapper;
import com.inspur.seed.service.breed.IBreedSeedProduceService;
import com.inspur.seed.vo.breed.BreedSeedProduceVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Breeder Seed 生产Service实现
 *
 * @author igdp
 */
@Service
public class BreedSeedProduceServiceImpl extends ServiceImpl<BreedSeedProduceMapper,BreedSeedProduce> implements IBreedSeedProduceService {

    @Autowired
    private BreedSeedDistributeDetailMapper distributeDetailMapper;



    @Override
    public List<BreedSeedProduceVO> getProduceList(BreedSeedProduceQueryDTO queryDTO) {
        // 使用MyBatis-Plus的LambdaQueryWrapper构建查询条件
        LambdaQueryWrapper<BreedSeedProduce> queryWrapper = Wrappers.lambdaQuery();

        // 添加查询条件
        if (queryDTO != null) {
            if (StringUtils.hasText(queryDTO.getProduceBatchId())) {
                queryWrapper.eq(BreedSeedProduce::getProduceBatchId, queryDTO.getProduceBatchId());
            }
            if (StringUtils.hasText(queryDTO.getVarietyId())) {
                queryWrapper.eq(BreedSeedProduce::getVarietyId, queryDTO.getVarietyId());
            }
            if (queryDTO.getStartTime() != null) {
                queryWrapper.ge(BreedSeedProduce::getTime, queryDTO.getStartTime());
            }
            if (queryDTO.getEndTime() != null) {
                queryWrapper.le(BreedSeedProduce::getTime, queryDTO.getEndTime());
            }
        }

        // 添加排序
        queryWrapper.orderByDesc(BreedSeedProduce::getCreateTime);

        // 执行查询
        List<BreedSeedProduce> entities = this.list(queryWrapper);

        // 转换为VO并计算remaining_quantity
        return entities.stream().map(entity -> {
            BreedSeedProduceVO vo = new BreedSeedProduceVO();
            BeanUtils.copyProperties(entity, vo);

            // 计算剩余数量
            BigDecimal remainingQuantity = calculateRemainingQuantity(entity.getProduceBatchId());
            vo.setRemainingQuantity(remainingQuantity);

            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public BreedSeedProduceVO getProduceById(String breedSeedProduceBatchId) {
        BreedSeedProduce entity = this.getById(breedSeedProduceBatchId);
        if (entity == null) {
            return null;
        }

        BreedSeedProduceVO vo = new BreedSeedProduceVO();
        BeanUtils.copyProperties(entity, vo);

        // 计算剩余数量
        BigDecimal remainingQuantity = calculateRemainingQuantity(breedSeedProduceBatchId);
        vo.setRemainingQuantity(remainingQuantity);

        return vo;
    }

    /**
     * 计算剩余可分发量
     *
     * @param produceBatchId 生产批次ID
     * @return 剩余可分发量
     */
    private BigDecimal calculateRemainingQuantity(String produceBatchId) {
        // 查询该生产批次的所有分发明细
        LambdaQueryWrapper<BreedSeedDistributeDetail> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(BreedSeedDistributeDetail::getProduceBatchId, produceBatchId);

        List<BreedSeedDistributeDetail> details = distributeDetailMapper.selectList(queryWrapper);

        // 计算已分发总量
        BigDecimal distributedSum = details.stream()
                .map(BreedSeedDistributeDetail::getDistributeQuantity)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 获取生产总量
        BreedSeedProduce produce = this.getById(produceBatchId);
        if (produce == null) {
            return BigDecimal.ZERO;
        }

        //
        return produce.getInputSeedQuantity().subtract(distributedSum != null ? distributedSum : BigDecimal.ZERO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BreedSeedProduceVO addProduce(BreedSeedProduceDTO dto) {

        BreedSeedProduce produce = new BreedSeedProduce();
        BeanUtils.copyProperties(dto, produce);

        // 生成UUID作为主键
        produce.setProduceBatchId(IdUtils.fastSimpleUUID());

        // 自动获取当前操作人信息
        String userId = SecurityUtils.getUserId();
        String username = SecurityUtils.getUsername();
        produce.setOperatorId(userId);
        produce.setOperatorName(username);

        // 设置默认状态为已完成
        produce.setProduceStatus("Onging");

        // 设置创建时间
        Date now = new Date();
        produce.setCreateTime(now);
        produce.setUpdateTime(now);


        this.baseMapper.insert(produce);

        return getProduceById(produce.getProduceBatchId());
    }

    @Override
    public void delete(String produceBatchId) {
        this.baseMapper.deleteById(produceBatchId);
    }

    /**
     * 更新生产批次剩余量(扣减)
     *
     * @param produceBatchId 生产批次ID
     * @param distributeQuantity 分发数量
     * @return 是否更新成功
     */
    public boolean updateRemainingQuantity(String produceBatchId, BigDecimal distributeQuantity) {
        // 先检查剩余量是否足够
        BigDecimal remainingQuantity = calculateRemainingQuantity(produceBatchId);
        if (remainingQuantity.compareTo(distributeQuantity) < 0) {
            return false; // 剩余量不足
        }

        // 更新时间戳
        BreedSeedProduce produce = new BreedSeedProduce();
        produce.setUpdateTime(new Date());

        LambdaQueryWrapper<BreedSeedProduce> updateWrapper = new  LambdaQueryWrapper();
        updateWrapper.eq(BreedSeedProduce::getProduceBatchId, produceBatchId);
        this.update(updateWrapper);

        return this.update(produce, updateWrapper);
    }
}
