package com.inspur.seed.service.breed.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.common.exception.ServiceException;
import com.inspur.common.utils.SecurityUtils;
import com.inspur.seed.domain.breed.BreedSeedDistributeDetail;
import com.inspur.seed.domain.breed.BreedSeedProduce;
import com.inspur.seed.domain.breed.BreedSeedProduceResult;
import com.inspur.seed.dto.breed.BreedSeedProduceDTO;
import com.inspur.seed.dto.breed.BreedSeedProduceQueryDTO;
import com.inspur.seed.mapper.breed.BreedSeedDistributeDetailMapper;
import com.inspur.seed.mapper.breed.BreedSeedProduceMapper;
import com.inspur.seed.mapper.breed.BreedSeedProduceResultMapper;
import com.inspur.seed.service.breed.IBreedSeedProduceResultService;
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

    @Autowired
    private BreedSeedProduceResultMapper breedSeedProduceResultMapper;



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
            if (StringUtils.hasText(queryDTO.getProduceStatus())) {
                queryWrapper.eq(BreedSeedProduce::getProduceStatus, queryDTO.getProduceStatus());
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
        BreedSeedProduceResult breedSeedProduceResult = breedSeedProduceResultMapper.getResultByProduceBatchId(produceBatchId);
        if (breedSeedProduceResult == null) {
            return BigDecimal.ZERO;
        }

        //
        return breedSeedProduceResult.getProducedAmount().subtract(distributedSum != null ? distributedSum : BigDecimal.ZERO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BreedSeedProduceVO addProduce(BreedSeedProduceDTO dto) {

        BreedSeedProduce produce = new BreedSeedProduce();
        BeanUtils.copyProperties(dto, produce);

        // 校验必要字段以生成主键
        if (!StringUtils.hasText(produce.getCropType())) {
            throw new ServiceException("作物类型不能为空(cropType)");
        }
        if (!StringUtils.hasText(produce.getFromSeedLevel())) {
            throw new ServiceException("来源种子等级不能为空(fromSeedLevel)");
        }
        if (!StringUtils.hasText(produce.getToSeedLevel())) {
            throw new ServiceException("去向种子等级不能为空(toSeedLevel)");
        }

        // 自定义主键规则：P_{cropType}_{From}_{To}_{6位序列}
        String prefix = String.format("P_%s_%s_%s_", sanitize(produce.getCropType()), sanitize(produce.getFromSeedLevel()), sanitize(produce.getToSeedLevel()));
        String nextId = generateNextIdByPrefix(prefix);
        produce.setProduceBatchId(nextId);

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

    /**
     * 生成带前缀的下一个ID，序列为6位，不足补零
     */
    private String generateNextIdByPrefix(String prefix) {
        String maxId = this.baseMapper.selectMaxIdByPrefix(prefix);
        int nextSeq = 1;
        if (StringUtils.hasText(maxId)) {
            // 取最后6位序列
            String[] parts = maxId.split("_");
            String last = parts[parts.length - 1];
            try {
                nextSeq = Integer.parseInt(last) + 1;
            } catch (NumberFormatException e) {
                // 回退为1
                nextSeq = 1;
            }
        }
        return prefix + String.format("%06d", nextSeq);
    }

    /**
     * 清理前缀中的空白与特殊空格，替换空白为无或中划线，避免ID异常
     */
    private String sanitize(String val) {
        if (val == null) return "";
        // 去除首尾空白，内部空白替换为无
        String v = val.trim();
        // 统一用连字符替换空格，避免与下划线冲突
        v = v.replaceAll("\\s+", "-");
        return v;
    }
}
