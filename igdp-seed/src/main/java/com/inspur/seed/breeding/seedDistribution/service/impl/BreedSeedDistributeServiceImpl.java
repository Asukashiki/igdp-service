package com.inspur.seed.breeding.seedDistribution.service.impl;

import com.inspur.common.exception.ServiceException;
import com.inspur.common.utils.uuid.IdUtils;
import com.inspur.seed.breeding.seedDistribution.service.IBreedSeedDistributeService;
import com.inspur.seed.breeding.seedDistribution.domain.entity.BreedSeedDistributeDetail;
import com.inspur.seed.breeding.seedDistribution.domain.entity.BreedSeedDistributeMain;
import com.inspur.seed.breeding.seedDistribution.domain.dto.BreedSeedDistributeDTO;
import com.inspur.seed.breeding.seedDistribution.domain.dto.BreedSeedDistributeQueryDTO;
import com.inspur.seed.multiplication.oseReceive.domain.entity.OseBreedSeedReceiveConfirm;
import com.inspur.seed.breeding.seedDistribution.mapper.BreedSeedDistributeDetailMapper;
import com.inspur.seed.breeding.seedDistribution.mapper.BreedSeedDistributeMapper;
import com.inspur.seed.Institution.ose.mapper.OseInfoMapper;
import com.inspur.seed.multiplication.oseReceive.mapper.OseReceiveConfirmMapper;
import com.inspur.seed.breeding.breederSeed.service.IBreedSeedProduceService;
import com.inspur.seed.breeding.seedDistribution.domain.vo.BreedSeedDistributeVO;
import com.inspur.seed.breeding.breederSeed.domain.vo.BreedSeedProduceVO;
import com.inspur.seed.Institution.ose.domain.vo.OseInfoVO;
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
    private com.inspur.seed.breeding.breederSeed.mapper.BreedSeedProduceResultMapper produceResultMapper;

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
            // 1. 获取生产结果记录
            com.inspur.seed.breeding.breederSeed.domain.entity.BreedSeedProduceResult produceResult = 
                produceResultMapper.getResultByProduceBatchId(item.getProduceBatchId());
            
            if (produceResult == null) {
                throw new ServiceException("Production result not found for batch: " + item.getProduceBatchId());
            }
            
            // 2. 验证剩余量是否足够
            BigDecimal remaining = produceResult.getRemainingQuantity();
            if (remaining == null || remaining.compareTo(item.getDistributeQuantity()) < 0) {
                throw new ServiceException("Insufficient remaining quantity. Available: " + remaining + " kg, Required: " + item.getDistributeQuantity() + " kg");
            }
            
            // 3. 扣减剩余量
            BigDecimal newRemaining = remaining.subtract(item.getDistributeQuantity());
            produceResult.setRemainingQuantity(newRemaining);
            produceResult.setUpdateTime(LocalDateTime.now());
            produceResultMapper.updateById(produceResult);
            
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
                // 记录分发后的剩余量（用于显示）
                item.setProduceBatchRemaining(produceVO.getRemainingQuantity().subtract(item.getDistributeQuantity()));
                // 从breed_seed_produce表自动带出品种名称、作物类型
                item.setVarietyName(produceVO.getVarietyName());
                item.setCropType(produceVO.getCropType());
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
