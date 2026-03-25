package com.inspur.seed.breeding.seedDistribution.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.inspur.common.exception.ServiceException;
import com.inspur.common.utils.uuid.IdUtils;
import com.inspur.seed.breeding.seedDistribution.service.IBreedSeedDistributeService;
import com.inspur.seed.breeding.seedDistribution.domain.entity.BreedSeedDistributeDetail;
import com.inspur.seed.breeding.seedDistribution.domain.entity.BreedSeedDistributeMain;
import com.inspur.seed.breeding.seedDistribution.domain.dto.BreedSeedDistributeDTO;
import com.inspur.seed.breeding.seedDistribution.domain.dto.BreedSeedDistributeQueryDTO;
import com.inspur.seed.domain.basic.BasicSeedProduceResult;
import com.inspur.seed.domain.prebasic.PrebasicSeedProduceResult;
import com.inspur.seed.mapper.basic.BasicSeedProduceResultMapper;
import com.inspur.seed.mapper.prebasic.PrebasicSeedProduceResultMapper;
import com.inspur.seed.multiplication.oseReceive.domain.entity.OseBreedSeedReceiveConfirm;
import com.inspur.seed.breeding.seedDistribution.mapper.BreedSeedDistributeDetailMapper;
import com.inspur.seed.breeding.seedDistribution.mapper.BreedSeedDistributeMapper;
import com.inspur.seed.Institution.ose.mapper.OseInfoMapper;
import com.inspur.seed.multiplication.oseReceive.mapper.OseReceiveConfirmMapper;
import com.inspur.seed.breeding.breederSeed.service.IBreedSeedProduceService;
import com.inspur.seed.breeding.seedDistribution.domain.vo.BreedSeedDistributeVO;
import com.inspur.seed.breeding.breederSeed.domain.vo.BreedSeedProduceVO;
import com.inspur.seed.Institution.ose.domain.vo.OseInfoVO;
import com.inspur.seed.service.basic.IBasicSeedProduceResultService;
import com.inspur.seed.breeding.breederSeed.domain.entity.BreedSeedProduceResult;
import com.inspur.seed.breeding.breederSeed.domain.vo.BreedSeedProduceResultVO;
import com.inspur.seed.breeding.breederSeed.mapper.BreedSeedProduceResultMapper;
import com.inspur.seed.service.prebasic.IPrebasicSeedProduceResultService;
import com.inspur.seed.vo.prebasic.PrebasicSeedProduceResultVO;
import com.inspur.seed.vo.basic.BasicSeedProduceResultVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Breeder Seed 分发Service实现
 *
 * @author igdp
 */
@Service
public class BreedSeedDistributeServiceImpl implements IBreedSeedDistributeService {

    @Autowired
    private BreedSeedDistributeMapper distributeMapper;

    @Autowired
    private BreedSeedDistributeDetailMapper distributeDetailMapper;

    @Autowired
    private OseReceiveConfirmMapper receiveConfirmMapper;

    @Autowired
    private OseInfoMapper oseInfoMapper;

    @Autowired
    private IBreedSeedProduceService produceService;

    @Autowired
    private PrebasicSeedProduceResultMapper prebasicSeedProduceResultMapper;

    @Autowired
    private BasicSeedProduceResultMapper basicSeedProduceResultMapper;

    @Autowired
    private IPrebasicSeedProduceResultService prebasicSeedProduceResultService;

    @Autowired
    private IBasicSeedProduceResultService basicSeedProduceResultService;

    @Autowired
    private BreedSeedProduceResultMapper breedSeedProduceResultMapper;

    @Override
    public List<BreedSeedDistributeVO> getDistributeList(BreedSeedDistributeQueryDTO queryDTO) {
        return distributeMapper.selectDistributeList(queryDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BreedSeedDistributeVO addDistribute(BreedSeedDistributeDTO dto) {
        // 生成分发主表 D_当前时间戳_序列号【6位】
        String distributeId =
                "D_" + dto.getOrgan() + "_" + dto.getFromSeedLevel() + "_" + dto.getToSeedLevel() + "_" + IdUtils.fastSimpleUUID().substring(0, 6);
        // 计算明细总数量
        BigDecimal totalQuantity = BigDecimal.ZERO;

        // 验证并扣减剩余量
        for (BreedSeedDistributeDetail item : dto.getDetailList()) {
            // 根据 fromSeedLevel 的值调用不同的服务类
            if ("Breeder".equals(dto.getFromSeedLevel())) {
                // 当 fromSeedLevel 为 Breeder 时，调用育种家种子生产结果
                BreedSeedProduceResultVO produceResultVO = breedSeedProduceResultMapper.getResultByProduceBatchId(item.getProduceBatchId());

                if (produceResultVO == null) {
                    throw new ServiceException("Breeder Production result not found for batch: " + item.getProduceBatchId());
                }

                // 验证剩余量是否足够
                BigDecimal remaining = produceResultVO.getRemainingQuantity();
                if (remaining == null || remaining.compareTo(item.getDistributeQuantity()) < 0) {
                    throw new ServiceException("Insufficient remaining quantity for Breeder. Available: " + remaining + " kg, Required: " + item.getDistributeQuantity() + " kg");
                }

                // 扣减剩余量
                LambdaQueryWrapper<BreedSeedProduceResult> updateQuery = new LambdaQueryWrapper<>();
                updateQuery.eq(BreedSeedProduceResult::getProduceBatchId, item.getProduceBatchId());
                BreedSeedProduceResult updateResult = new BreedSeedProduceResult();
                BigDecimal newRemaining = remaining.subtract(item.getDistributeQuantity());
                updateResult.setRemainingQuantity(newRemaining);
                updateResult.setUpdateTime(LocalDateTime.now());
                breedSeedProduceResultMapper.update(updateResult, updateQuery);
            } else if ("Pre-Basic".equals(dto.getFromSeedLevel())) {
                // 当 fromSeedLevel 为 Pre-Basic 时，调用预原种生产结果服务
                PrebasicSeedProduceResultVO produceResultVO = prebasicSeedProduceResultService.getResultByProduceBatchId(item.getProduceBatchId());

                if (produceResultVO == null) {
                    throw new ServiceException("Pre-Basic Production result not found for batch: " + item.getProduceBatchId());
                }

                // 验证剩余量是否足够
                BigDecimal remaining = produceResultVO.getRemainingQuantity();
                if (remaining == null || remaining.compareTo(item.getDistributeQuantity()) < 0) {
                    throw new ServiceException("Insufficient remaining quantity for Pre-Basic. Available: " + remaining + " kg, Required: " + item.getDistributeQuantity() + " kg");
                }

                // 扣减剩余量 - 通过Mapper直接更新数据库
                LambdaQueryWrapper<PrebasicSeedProduceResult> updateQuery = new LambdaQueryWrapper<>();
                updateQuery.eq(PrebasicSeedProduceResult::getProduceBatchId, item.getProduceBatchId());
                PrebasicSeedProduceResult updateResult = new PrebasicSeedProduceResult();
                BigDecimal newRemaining = remaining.subtract(item.getDistributeQuantity());
                updateResult.setRemainingQuantity(newRemaining);
                updateResult.setUpdateTime(LocalDateTime.now());
                // 更新预原种生产结果
                prebasicSeedProduceResultMapper.update(updateResult, updateQuery);
            } else if ("Basic".equals(dto.getFromSeedLevel())) {
                // 当 fromSeedLevel 为 Basic 时，调用原种生产结果服务
                BasicSeedProduceResultVO produceResultVO = basicSeedProduceResultService.getResultByProduceBatchId(item.getProduceBatchId());

                if (produceResultVO == null) {
                    throw new ServiceException("Basic Production result not found for batch: " + item.getProduceBatchId());
                }

                // 验证剩余量是否足够
                BigDecimal remaining = produceResultVO.getRemainingQuantity();
                if (remaining == null || remaining.compareTo(item.getDistributeQuantity()) < 0) {
                    throw new ServiceException("Insufficient remaining quantity for Basic. Available: " + remaining + " kg, Required: " + item.getDistributeQuantity() + " kg");
                }

                // 扣减剩余量 - 通过Mapper直接更新数据库
                LambdaQueryWrapper<BasicSeedProduceResult> updateQuery = new LambdaQueryWrapper<>();
                updateQuery.eq(BasicSeedProduceResult::getProduceBatchId, item.getProduceBatchId());
                BasicSeedProduceResult updateResult = new BasicSeedProduceResult();
                BigDecimal newRemaining = remaining.subtract(item.getDistributeQuantity());
                updateResult.setRemainingQuantity(newRemaining);
                updateResult.setUpdateTime(LocalDateTime.now());
                // 更新原种生产结果
                basicSeedProduceResultMapper.update(updateResult, updateQuery);
            } else {
                throw new ServiceException("Invalid fromSeedLevel: " + dto.getFromSeedLevel());
            }

            totalQuantity = totalQuantity.add(item.getDistributeQuantity());
        }

        // 插入分发主表
        BreedSeedDistributeMain main = new BreedSeedDistributeMain();
        BeanUtils.copyProperties(dto, main);
        main.setDistributeId(distributeId);
        main.setTotalDistributeQuantity(totalQuantity);
        main.setDistributeStatus("Distributed");

        Date now = new Date();
        main.setCreateTime(now);
        main.setUpdateTime(now);

        // 从ose_info表自动带出OSE名称
        OseInfoVO oseInfoVO = oseInfoMapper.selectOseById(dto.getOseId());
        if (oseInfoVO != null) {
            main.setOseName(oseInfoVO.getOseName());
        }

        distributeMapper.insert(main);

        // 插入分发明细
        List<BreedSeedDistributeDetail> detailList = new ArrayList<>();
        for (BreedSeedDistributeDetail item : dto.getDetailList()) {
            item.setDistributeDetailId(IdUtils.fastSimpleUUID());
            item.setDistributeId(distributeId);

            // 获取生产批次信息用于填充明细
            BreedSeedProduceVO produceVO = produceService.getProduceById(item.getProduceBatchId());
            if (produceVO != null) {
                // 从breed_seed_produce表自动带出品种名称、作物类型
                item.setVarietyName(produceVO.getVarietyName());
                item.setCropType(produceVO.getCropType());
            }

            // 记录分发后的剩余量（用于显示）
            // 根据 fromSeedLevel 的值获取正确的剩余量
            if ("Breeder".equals(dto.getFromSeedLevel())) {
                // 对于育种家种子，获取育种家生产结果的剩余量
                BreedSeedProduceResultVO breedResultVO = breedSeedProduceResultMapper.getResultByProduceBatchId(item.getProduceBatchId());
                if (breedResultVO != null) {
                    item.setProduceBatchRemaining(breedResultVO.getRemainingQuantity());
                    item.setCropType(breedResultVO.getCropType());
                } else {
                    item.setProduceBatchRemaining(BigDecimal.ZERO);
                }
            } else if ("Pre-Basic".equals(dto.getFromSeedLevel())) {
                // 对于预原种，获取预原种生产结果的剩余量
                PrebasicSeedProduceResultVO prebasicResultVO = prebasicSeedProduceResultService.getResultByProduceBatchId(item.getProduceBatchId());
                if (prebasicResultVO != null) {
                    item.setProduceBatchRemaining(prebasicResultVO.getRemainingQuantity());
                    item.setCropType(prebasicResultVO.getCropType());
                } else {
                    item.setProduceBatchRemaining(BigDecimal.ZERO);
                }
                
            } else if ("Basic".equals(dto.getFromSeedLevel())) {
                // 对于原种，获取原种生产结果的剩余量
                BasicSeedProduceResultVO basicResultVO = basicSeedProduceResultService.getResultByProduceBatchId(item.getProduceBatchId());
                if (basicResultVO != null) {
                    item.setProduceBatchRemaining(basicResultVO.getRemainingQuantity());
                    item.setCropType(basicResultVO.getCropType());
                    
                } else {
                    item.setProduceBatchRemaining(BigDecimal.ZERO);
                }
            } else {
                item.setProduceBatchRemaining(BigDecimal.ZERO);
            }

            detailList.add(item);
        }

        distributeDetailMapper.batchInsert(detailList);

        // 自动创建OSE接收确认记录(状态为PENDING)
        OseBreedSeedReceiveConfirm receiveConfirm = new OseBreedSeedReceiveConfirm();
        receiveConfirm.setReceiveConfirmId(IdUtils.fastSimpleUUID());
        receiveConfirm.setDistributeId(distributeId);
        receiveConfirm.setOseId(dto.getOseId());
        receiveConfirm.setReceiveStatus("PENDING");
        receiveConfirm.setCreateTime(now);
        receiveConfirm.setUpdateTime(now);
        receiveConfirmMapper.insert(receiveConfirm);

        return distributeMapper.selectDistributeById(distributeId);
    }
}
