package com.inspur.seed.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.seed.domain.dto.BreedingSeedCertificationDTO;
import com.inspur.seed.domain.entity.*;
import com.inspur.seed.domain.vo.*;
import com.inspur.seed.mapper.*;
import com.inspur.seed.service.IBreedingSeedCertificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 繁殖种子认证申请Service业务层处理
 *
 * @author igdp
 * @date 2025-11-29
 */
@Service
public class BreedingSeedCertificationServiceImpl extends ServiceImpl<BreedingSeedCertificationMapper, BreedingSeedCertification> implements IBreedingSeedCertificationService {

    @Autowired
    private BreedingSeedVarietyInfoMapper varietyInfoMapper;

    @Autowired
    private BreedingSeedTechnicalTraitMapper technicalTraitMapper;

    @Autowired
    private BreedingSeedTrialPerformanceMapper trialPerformanceMapper;

    @Autowired
    private BreedingSeedSupervisionMapper supervisionMapper;

    @Override
    public IPage<BreedingSeedCertificationVO> selectBreedingSeedCertificationPage(IPage<BreedingSeedCertification> page, BreedingSeedCertificationDTO dto) {
        QueryWrapper<BreedingSeedCertification> wrapper = buildQueryWrapper(dto);
        IPage<BreedingSeedCertification> entityPage = this.page(page, wrapper);

        IPage<BreedingSeedCertificationVO> voPage = entityPage.convert(entity -> {
            BreedingSeedCertificationVO vo = BeanUtil.copyProperties(entity, BreedingSeedCertificationVO.class);
            loadRelatedInfo(vo, entity.getAuthId());
            return vo;
        });

        return voPage;
    }

    @Override
    public List<BreedingSeedCertificationVO> selectBreedingSeedCertificationList(BreedingSeedCertificationDTO dto) {
        QueryWrapper<BreedingSeedCertification> wrapper = buildQueryWrapper(dto);
        List<BreedingSeedCertification> list = this.list(wrapper);

        return list.stream()
                .map(entity -> {
                    BreedingSeedCertificationVO vo = BeanUtil.copyProperties(entity, BreedingSeedCertificationVO.class);
                    loadRelatedInfo(vo, entity.getAuthId());
                    return vo;
                })
                .collect(Collectors.toList());
    }

    @Override
    public BreedingSeedCertificationVO selectBreedingSeedCertificationById(String dataId) {
        BreedingSeedCertification entity = this.getById(dataId);
        if (entity == null) {
            return null;
        }

        BreedingSeedCertificationVO vo = BeanUtil.copyProperties(entity, BreedingSeedCertificationVO.class);
        loadRelatedInfo(vo, entity.getAuthId());
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertBreedingSeedCertification(BreedingSeedCertificationDTO dto) {
        // 保存主表数据
        BreedingSeedCertification entity = BeanUtil.copyProperties(dto, BreedingSeedCertification.class);
        entity.setDelFlag("0");
        this.save(entity);

        String authId = entity.getAuthId();
        String breedingBatchId = entity.getBreedingBatchId();

        // 保存品种信息
        if (dto.getVarietyInfo() != null) {
            BreedingSeedVarietyInfo varietyInfo = BeanUtil.copyProperties(dto.getVarietyInfo(), BreedingSeedVarietyInfo.class);
            varietyInfo.setAuthId(authId);
            varietyInfo.setBreedingBatchId(breedingBatchId);
            varietyInfo.setDelFlag("0");
            varietyInfoMapper.insert(varietyInfo);
        }

        // 保存技术性状信息
        if (dto.getTechnicalTrait() != null) {
            BreedingSeedTechnicalTrait technicalTrait = BeanUtil.copyProperties(dto.getTechnicalTrait(), BreedingSeedTechnicalTrait.class);
            technicalTrait.setAuthId(authId);
            technicalTrait.setBreedingBatchId(breedingBatchId);
            technicalTrait.setDelFlag("0");
            technicalTraitMapper.insert(technicalTrait);
        }

        // 保存试验与性能信息
        if (dto.getTrialPerformance() != null) {
            BreedingSeedTrialPerformance trialPerformance = BeanUtil.copyProperties(dto.getTrialPerformance(), BreedingSeedTrialPerformance.class);
            trialPerformance.setAuthId(authId);
            trialPerformance.setBreedingBatchId(breedingBatchId);
            trialPerformance.setDelFlag("0");
            trialPerformanceMapper.insert(trialPerformance);
        }

        // 保存监管信息
        if (dto.getSupervision() != null) {
            BreedingSeedSupervision supervision = BeanUtil.copyProperties(dto.getSupervision(), BreedingSeedSupervision.class);
            supervision.setAuthId(authId);
            supervision.setBreedingBatchId(breedingBatchId);
            supervision.setDelFlag("0");
            supervisionMapper.insert(supervision);
        }

        return 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateBreedingSeedCertification(BreedingSeedCertificationDTO dto) {
        // 更新主表数据
        BreedingSeedCertification entity = BeanUtil.copyProperties(dto, BreedingSeedCertification.class);
        this.updateById(entity);

        String authId = entity.getAuthId();

        // 更新品种信息
        if (dto.getVarietyInfo() != null) {
            BreedingSeedVarietyInfo varietyInfo = BeanUtil.copyProperties(dto.getVarietyInfo(), BreedingSeedVarietyInfo.class);
            if (StrUtil.isNotBlank(varietyInfo.getDataId())) {
                varietyInfoMapper.updateById(varietyInfo);
            } else {
                varietyInfo.setAuthId(authId);
                varietyInfo.setBreedingBatchId(entity.getBreedingBatchId());
                varietyInfo.setDelFlag("0");
                varietyInfoMapper.insert(varietyInfo);
            }
        }

        // 更新技术性状信息
        if (dto.getTechnicalTrait() != null) {
            BreedingSeedTechnicalTrait technicalTrait = BeanUtil.copyProperties(dto.getTechnicalTrait(), BreedingSeedTechnicalTrait.class);
            if (StrUtil.isNotBlank(technicalTrait.getDataId())) {
                technicalTraitMapper.updateById(technicalTrait);
            } else {
                technicalTrait.setAuthId(authId);
                technicalTrait.setBreedingBatchId(entity.getBreedingBatchId());
                technicalTrait.setDelFlag("0");
                technicalTraitMapper.insert(technicalTrait);
            }
        }

        // 更新试验与性能信息
        if (dto.getTrialPerformance() != null) {
            BreedingSeedTrialPerformance trialPerformance = BeanUtil.copyProperties(dto.getTrialPerformance(), BreedingSeedTrialPerformance.class);
            if (StrUtil.isNotBlank(trialPerformance.getDataId())) {
                trialPerformanceMapper.updateById(trialPerformance);
            } else {
                trialPerformance.setAuthId(authId);
                trialPerformance.setBreedingBatchId(entity.getBreedingBatchId());
                trialPerformance.setDelFlag("0");
                trialPerformanceMapper.insert(trialPerformance);
            }
        }

        // 更新监管信息
        if (dto.getSupervision() != null) {
            BreedingSeedSupervision supervision = BeanUtil.copyProperties(dto.getSupervision(), BreedingSeedSupervision.class);
            if (StrUtil.isNotBlank(supervision.getDataId())) {
                supervisionMapper.updateById(supervision);
            } else {
                supervision.setAuthId(authId);
                supervision.setBreedingBatchId(entity.getBreedingBatchId());
                supervision.setDelFlag("0");
                supervisionMapper.insert(supervision);
            }
        }

        return 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteBreedingSeedCertificationByIds(String[] dataIds) {
        List<BreedingSeedCertification> list = Arrays.stream(dataIds)
                .map(id -> {
                    BreedingSeedCertification entity = new BreedingSeedCertification();
                    entity.setDataId(id);
                    entity.setDelFlag("2");
                    return entity;
                })
                .collect(Collectors.toList());

        // 同时删除关联的子表数据
        for (String dataId : dataIds) {
            BreedingSeedCertification certification = this.getById(dataId);
            if (certification != null) {
                String authId = certification.getAuthId();
                deleteRelatedInfo(authId);
            }
        }

        return this.updateBatchById(list) ? list.size() : 0;
    }

    /**
     * 构建查询条件
     */
    private QueryWrapper<BreedingSeedCertification> buildQueryWrapper(BreedingSeedCertificationDTO dto) {
        QueryWrapper<BreedingSeedCertification> wrapper = new QueryWrapper<>();

        if (dto != null) {
            if (StrUtil.isNotBlank(dto.getApplyOrgName())) {
                wrapper.like("apply_org_name", dto.getApplyOrgName());
            }
            if (StrUtil.isNotBlank(dto.getCropType())) {
                wrapper.eq("crop_type", dto.getCropType());
            }
            if (StrUtil.isNotBlank(dto.getVarietyName())) {
                wrapper.like("variety_name", dto.getVarietyName());
            }
            if (dto.getRecordDate() != null) {
                wrapper.eq("record_date", dto.getRecordDate());
            }
        }

        wrapper.eq("del_flag", "0");
        wrapper.orderByDesc("create_time");

        return wrapper;
    }

    /**
     * 加载关联信息
     */
    private void loadRelatedInfo(BreedingSeedCertificationVO vo, String authId) {
        // 加载品种信息
        QueryWrapper<BreedingSeedVarietyInfo> varietyWrapper = new QueryWrapper<>();
        varietyWrapper.eq("auth_id", authId).eq("del_flag", "0");
        BreedingSeedVarietyInfo varietyInfo = varietyInfoMapper.selectOne(varietyWrapper);
        if (varietyInfo != null) {
            vo.setVarietyInfo(BeanUtil.copyProperties(varietyInfo, BreedingSeedVarietyInfoVO.class));
        }

        // 加载技术性状信息
        QueryWrapper<BreedingSeedTechnicalTrait> traitWrapper = new QueryWrapper<>();
        traitWrapper.eq("auth_id", authId).eq("del_flag", "0");
        BreedingSeedTechnicalTrait technicalTrait = technicalTraitMapper.selectOne(traitWrapper);
        if (technicalTrait != null) {
            vo.setTechnicalTrait(BeanUtil.copyProperties(technicalTrait, BreedingSeedTechnicalTraitVO.class));
        }

        // 加载试验与性能信息
        QueryWrapper<BreedingSeedTrialPerformance> trialWrapper = new QueryWrapper<>();
        trialWrapper.eq("auth_id", authId).eq("del_flag", "0");
        BreedingSeedTrialPerformance trialPerformance = trialPerformanceMapper.selectOne(trialWrapper);
        if (trialPerformance != null) {
            vo.setTrialPerformance(BeanUtil.copyProperties(trialPerformance, BreedingSeedTrialPerformanceVO.class));
        }

        // 加载监管信息
        QueryWrapper<BreedingSeedSupervision> supervisionWrapper = new QueryWrapper<>();
        supervisionWrapper.eq("auth_id", authId).eq("del_flag", "0");
        BreedingSeedSupervision supervision = supervisionMapper.selectOne(supervisionWrapper);
        if (supervision != null) {
            vo.setSupervision(BeanUtil.copyProperties(supervision, BreedingSeedSupervisionVO.class));
        }
    }

    /**
     * 删除关联信息
     */
    private void deleteRelatedInfo(String authId) {
        // 删除品种信息
        QueryWrapper<BreedingSeedVarietyInfo> varietyWrapper = new QueryWrapper<>();
        varietyWrapper.eq("auth_id", authId);
        BreedingSeedVarietyInfo varietyInfo = new BreedingSeedVarietyInfo();
        varietyInfo.setDelFlag("2");
        varietyInfoMapper.update(varietyInfo, varietyWrapper);

        // 删除技术性状信息
        QueryWrapper<BreedingSeedTechnicalTrait> traitWrapper = new QueryWrapper<>();
        traitWrapper.eq("auth_id", authId);
        BreedingSeedTechnicalTrait technicalTrait = new BreedingSeedTechnicalTrait();
        technicalTrait.setDelFlag("2");
        technicalTraitMapper.update(technicalTrait, traitWrapper);

        // 删除试验与性能信息
        QueryWrapper<BreedingSeedTrialPerformance> trialWrapper = new QueryWrapper<>();
        trialWrapper.eq("auth_id", authId);
        BreedingSeedTrialPerformance trialPerformance = new BreedingSeedTrialPerformance();
        trialPerformance.setDelFlag("2");
        trialPerformanceMapper.update(trialPerformance, trialWrapper);

        // 删除监管信息
        QueryWrapper<BreedingSeedSupervision> supervisionWrapper = new QueryWrapper<>();
        supervisionWrapper.eq("auth_id", authId);
        BreedingSeedSupervision supervision = new BreedingSeedSupervision();
        supervision.setDelFlag("2");
        supervisionMapper.update(supervision, supervisionWrapper);
    }
}
