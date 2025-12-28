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
import com.inspur.seed.service.breed.IBreedSeedProduceService;
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
    private OseReceiveConfirmMapper receiveConfirmMapper;

    @Autowired
    private OseInfoMapper oseInfoMapper;

    @Autowired
    private IBreedSeedProduceService produceService;

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
        // todo 逻辑修改待处理
        for (BreedSeedDistributeDetail item : dto.getDetailList()) {
            // 验证分发数量不能超过生产批次剩余量
            BreedSeedProduceVO produceVO = produceService.getProduceById(item.getProduceBatchId());
            BigDecimal remaining = produceVO != null ? produceVO.getRemainingQuantity() : BigDecimal.ZERO;
            if (remaining == null || remaining.compareTo(item.getDistributeQuantity()) < 0) {
                throw new ServiceException("Distribution quantity exceeds the remaining quantity of the production batch");
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


            // 计算并记录剩余量
            BreedSeedProduceVO produceVO = produceService.getProduceById(item.getProduceBatchId());
            BigDecimal remaining = produceVO != null ? produceVO.getRemainingQuantity() : BigDecimal.ZERO;
            item.setProduceBatchRemaining(remaining.subtract(item.getDistributeQuantity()));

            // 从breed_seed_produce表自动带出品种名称、作物类型
            if (produceVO != null) {
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
