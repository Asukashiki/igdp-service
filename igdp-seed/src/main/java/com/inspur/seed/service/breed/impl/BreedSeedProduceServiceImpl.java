package com.inspur.seed.service.breed.impl;

import com.inspur.common.exception.ServiceException;
import com.inspur.common.utils.SecurityUtils;
import com.inspur.common.utils.uuid.IdUtils;
import com.inspur.seed.domain.breed.BreedSeedProduce;
import com.inspur.seed.dto.breed.BreedSeedProduceDTO;
import com.inspur.seed.dto.breed.BreedSeedProduceQueryDTO;
import com.inspur.seed.mapper.breed.BreedSeedProduceMapper;
import com.inspur.seed.service.breed.IBreedSeedProduceService;
import com.inspur.seed.vo.breed.BreedSeedProduceVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Breeder Seed 生产Service实现
 *
 * @author igdp
 */
@Service
public class BreedSeedProduceServiceImpl implements IBreedSeedProduceService {

    @Autowired
    private BreedSeedProduceMapper breedSeedProduceMapper;

    @Override
    public List<BreedSeedProduceVO> getProduceList(BreedSeedProduceQueryDTO queryDTO) {
        return breedSeedProduceMapper.selectProduceList(queryDTO);
    }

    @Override
    public BreedSeedProduceVO getProduceById(String breedSeedProduceBatchId) {
        return breedSeedProduceMapper.selectProduceById(breedSeedProduceBatchId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BreedSeedProduceVO addProduce(BreedSeedProduceDTO dto) {
        // 验证产出数量必须大于等于投入数量
        if (dto.getProduceSeedQuantrity().compareTo(dto.getInputSeedQuantity()) < 0) {
            throw new ServiceException("产出种子数量必须大于等于投入种子数量");
        }

        BreedSeedProduce produce = new BreedSeedProduce();
        BeanUtils.copyProperties(dto, produce);

        // 生成UUID作为主键
        produce.setBreedSeedProduceBatchId(IdUtils.fastSimpleUUID());

        // 自动获取当前操作人信息
        String userId = SecurityUtils.getUserId();
        String username = SecurityUtils.getUsername();
        produce.setOperatorId(userId);
        produce.setOperatorName(username);

        // 设置默认状态为已完成
        produce.setProduceStatus("FINISHED");

        // 设置创建时间
        Date now = new Date();
        produce.setCreateTime(now);
        produce.setUpdateTime(now);

        // TODO: 从关联表自动带出品种名称、作物类型、地块名称
        // 这里需要查询variety_publish表和land_info表获取相关信息
        // produce.setVarietyName(...);
        // produce.setCropType(...);
        // produce.setLandName(...);

        breedSeedProduceMapper.insert(produce);

        return getProduceById(produce.getBreedSeedProduceBatchId());
    }

    @Override
    public void delete(String breedSeedProduceBatchId) {
        breedSeedProduceMapper.deleteById(breedSeedProduceBatchId);
    }
}
