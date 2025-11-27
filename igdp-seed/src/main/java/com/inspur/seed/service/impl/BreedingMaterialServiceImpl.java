package com.inspur.seed.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.common.exception.ServiceException;
import com.inspur.common.utils.LoginHelper;
import com.inspur.common.utils.StringUtils;
import com.inspur.common.utils.uuid.IdUtils;
import com.inspur.seed.domain.BreedingMaterial;
import com.inspur.seed.mapper.BreedingMaterialMapper;
import com.inspur.seed.service.IBreedingMaterialService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 育种材料登记服务实现类
 *
 * @author system
 */
@Service
public class BreedingMaterialServiceImpl extends ServiceImpl<BreedingMaterialMapper, BreedingMaterial> implements IBreedingMaterialService {

    @Override
    public Map<String, String> addBreedingMaterial(BreedingMaterial breedingMaterial) {
        // 生成材料ID
        String materialId = "MAT" + IdUtils.fastSimpleUUID().substring(0, 16).toUpperCase();
        breedingMaterial.setMaterialId(materialId);

        // 生成登记编码(格式: REG + 年月日 + 随机6位)
        String registrationCode = "REG" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
                + IdUtils.fastSimpleUUID().substring(0, 6).toUpperCase();
        breedingMaterial.setRegistrationCode(registrationCode);

        // 设置操作时间
        breedingMaterial.setOperationTime(LocalDateTime.now());

        // 设置创建信息
        breedingMaterial.setCreateBy(LoginHelper.getUsername());
        breedingMaterial.setCreateTime(LocalDateTime.now());

        // 保存育种材料登记
        save(breedingMaterial);

        // 返回材料ID和登记编码
        Map<String, String> result = new HashMap<>();
        result.put("materialId", materialId);
        result.put("registrationCode", registrationCode);
        return result;
    }

    @Override
    public List<BreedingMaterial> queryBreedingMaterialList(String batchId, String seedType, LocalDate receiveDateStart, LocalDate receiveDateEnd) {
        LambdaQueryWrapper<BreedingMaterial> wrapper = new LambdaQueryWrapper<>();

        // 育种批次ID筛选
        if (StringUtils.isNotEmpty(batchId)) {
            wrapper.eq(BreedingMaterial::getBatchId, batchId);
        }

        // 种子类别筛选
        if (StringUtils.isNotEmpty(seedType)) {
            wrapper.eq(BreedingMaterial::getSeedType, seedType);
        }

        // 接收日期范围筛选
        if (receiveDateStart != null) {
            wrapper.ge(BreedingMaterial::getReceiveDate, receiveDateStart);
        }
        if (receiveDateEnd != null) {
            wrapper.le(BreedingMaterial::getReceiveDate, receiveDateEnd);
        }

        // 按操作时间倒序排列
        wrapper.orderByDesc(BreedingMaterial::getOperationTime);

        return list(wrapper);
    }

    @Override
    public BreedingMaterial queryByMaterialId(String materialId) {
        return getById(materialId);
    }

    @Override
    public boolean editBreedingMaterial(BreedingMaterial breedingMaterial) {
        // 查询现有材料信息
        BreedingMaterial existingMaterial = queryByMaterialId(breedingMaterial.getMaterialId());
        if (existingMaterial == null) {
            throw new ServiceException("育种材料不存在");
        }

        // 设置更新信息
        breedingMaterial.setUpdateBy(LoginHelper.getUsername());
        breedingMaterial.setUpdateTime(LocalDateTime.now());

        return updateById(breedingMaterial);
    }

    @Override
    public boolean removeBreedingMaterial(String materialId) {
        return removeById(materialId);
    }
}
