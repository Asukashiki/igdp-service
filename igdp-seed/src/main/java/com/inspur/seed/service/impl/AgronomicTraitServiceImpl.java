package com.inspur.seed.service.impl;

import cn.hutool.core.util.IdUtil;
import com.inspur.common.utils.SecurityUtils;
import com.inspur.seed.domain.AgronomicTrait;
import com.inspur.seed.mapper.AgronomicTraitMapper;
import com.inspur.seed.service.IAgronomicTraitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * 农艺性状数据Service实现类
 *
 * @author inspur
 */
@Service
public class AgronomicTraitServiceImpl implements IAgronomicTraitService {

    @Autowired
    private AgronomicTraitMapper agronomicTraitMapper;

    @Override
    public List<AgronomicTrait> selectAgronomicTraitList(AgronomicTrait agronomicTrait) {
        return agronomicTraitMapper.selectAgronomicTraitList(agronomicTrait);
    }

    @Override
    public AgronomicTrait selectAgronomicTraitById(String traitId) {
        return agronomicTraitMapper.selectAgronomicTraitById(traitId);
    }

    @Override
    public String insertAgronomicTrait(AgronomicTrait agronomicTrait) {
        // 生成主键
        String traitId = IdUtil.simpleUUID();
        agronomicTrait.setTraitId(traitId);

        // 设置创建信息
        agronomicTrait.setCreateTime(LocalDateTime.now());
        agronomicTrait.setCreateBy(SecurityUtils.getUsername());

        agronomicTraitMapper.insert(agronomicTrait);
        return traitId;
    }

    @Override
    public int updateAgronomicTrait(AgronomicTrait agronomicTrait) {
        // 设置更新信息
        agronomicTrait.setUpdateTime(LocalDateTime.now());
        agronomicTrait.setUpdateBy(SecurityUtils.getUsername());

        return agronomicTraitMapper.updateById(agronomicTrait);
    }

    @Override
    public int deleteAgronomicTraitByIds(String[] traitIds) {
        int count = 0;
        for (String traitId : traitIds) {
            AgronomicTrait trait = new AgronomicTrait();
            trait.setTraitId(traitId);
            trait.setIsDeleted(1);
            count += agronomicTraitMapper.updateById(trait);
        }
        return count;
    }
}
