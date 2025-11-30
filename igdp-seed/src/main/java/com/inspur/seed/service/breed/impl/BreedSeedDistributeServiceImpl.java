package com.inspur.seed.service.breed.impl;

import com.inspur.common.exception.ServiceException;
import com.inspur.common.utils.uuid.IdUtils;
import com.inspur.seed.domain.breed.BreedSeedDistributeDetail;
import com.inspur.seed.domain.breed.BreedSeedDistributeMain;
import com.inspur.seed.dto.breed.BreedSeedDistributeDTO;
import com.inspur.seed.dto.breed.BreedSeedDistributeQueryDTO;
import com.inspur.seed.domain.ose.OseBreedSeedReceiveConfirm;
import com.inspur.seed.mapper.breed.BreedSeedDistributeDetailMapper;
import com.inspur.seed.mapper.breed.BreedSeedDistributeMapper;
import com.inspur.seed.mapper.breed.BreedSeedProduceMapper;
import com.inspur.seed.mapper.ose.OseInfoMapper;
import com.inspur.seed.mapper.ose.OseReceiveConfirmMapper;
import com.inspur.seed.service.breed.IBreedSeedDistributeService;
import com.inspur.seed.vo.breed.BreedSeedDistributeVO;
import com.inspur.seed.vo.breed.BreedSeedProduceVO;
import com.inspur.seed.vo.ose.OseInfoVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
    private BreedSeedProduceMapper produceMapper;

    @Autowired
    private OseReceiveConfirmMapper receiveConfirmMapper;

    @Autowired
    private OseInfoMapper oseInfoMapper;

    @Override
    public List<BreedSeedDistributeVO> getDistributeList(BreedSeedDistributeQueryDTO queryDTO) {
        return distributeMapper.selectDistributeList(queryDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BreedSeedDistributeVO addDistribute(BreedSeedDistributeDTO dto) {
        // 生成分发主表ID
        String distributeId = IdUtils.fastSimpleUUID();

        // 计算明细总数量
        BigDecimal totalQuantity = BigDecimal.ZERO;
        for (BreedSeedDistributeDTO.DistributeDetailItem item : dto.getDetailList()) {
            // 验证分发数量不能超过生产批次剩余量
            BigDecimal remaining = produceMapper.selectRemainingQuantity(item.getBreedSeedProduceBatchId());
            if (remaining == null || remaining.compareTo(item.getDistributeQuantity()) < 0) {
                throw new ServiceException("分发数量超过生产批次剩余可分发量");
            }
            totalQuantity = totalQuantity.add(item.getDistributeQuantity());
        }

        // 插入分发主表
        BreedSeedDistributeMain main = new BreedSeedDistributeMain();
        BeanUtils.copyProperties(dto, main);
        main.setDistributeId(distributeId);
        main.setTotalDistributeQuantity(totalQuantity);
        main.setDistributeStatus("已分发");

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
        for (BreedSeedDistributeDTO.DistributeDetailItem item : dto.getDetailList()) {
            BreedSeedDistributeDetail detail = new BreedSeedDistributeDetail();
            detail.setDistributeDetailId(IdUtils.fastSimpleUUID());
            detail.setDistributeId(distributeId);
            detail.setBreedSeedProduceBatchId(item.getBreedSeedProduceBatchId());
            detail.setDistributeQuantity(item.getDistributeQuantity());

            // 计算并记录剩余量
            BigDecimal remaining = produceMapper.selectRemainingQuantity(item.getBreedSeedProduceBatchId());
            detail.setProduceBatchRemaining(remaining.subtract(item.getDistributeQuantity()));

            // 从breed_seed_produce表自动带出品种名称、作物类型
            BreedSeedProduceVO produceVO = produceMapper.selectProduceById(item.getBreedSeedProduceBatchId());
            if (produceVO != null) {
                detail.setVarietyName(produceVO.getVarietyName());
                detail.setCropType(produceVO.getCropType());
            }

            detailList.add(detail);
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
