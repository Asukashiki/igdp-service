package com.inspur.seed.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.common.exception.ServiceException;
import com.inspur.common.utils.LoginHelper;
import com.inspur.common.utils.StringUtils;
import com.inspur.common.utils.uuid.IdUtils;
import com.inspur.seed.domain.BreedingPlan;
import com.inspur.seed.domain.EnterpriseInfo;
import com.inspur.seed.mapper.BreedingPlanMapper;
import com.inspur.seed.service.IBreedingPlanService;
import com.inspur.seed.service.IEnterpriseCertifyService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 育种计划服务实现类
 *
 * @author system
 */
@Service
public class BreedingPlanServiceImpl extends ServiceImpl<BreedingPlanMapper, BreedingPlan> implements IBreedingPlanService {

    @Resource
    private IEnterpriseCertifyService enterpriseCertifyService;

    @Override
    public String addBreedingPlan(BreedingPlan breedingPlan) {
        // 检查批次ID是否已存在
        LambdaQueryWrapper<BreedingPlan> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BreedingPlan::getBatchId, breedingPlan.getBatchId());
        if (count(wrapper) > 0) {
            throw new ServiceException("育种批次ID已存在");
        }

        String userId = LoginHelper.getUsername();
        EnterpriseInfo enterpriseInfo = enterpriseCertifyService.queryByUserId(userId);
        if (enterpriseInfo == null) {
            throw new ServiceException("企业认证信息不存在");
        }
        breedingPlan.setEnterpriseId(enterpriseInfo.getEnterpriseId());

        // 生成计划ID
        String planId = "PLAN" + IdUtils.fastSimpleUUID().substring(0, 16).toUpperCase();
        breedingPlan.setPlanId(planId);

        // 设置创建信息
        breedingPlan.setCreateBy(LoginHelper.getUsername());
        breedingPlan.setCreateTime(LocalDateTime.now());

        // 保存育种计划
        save(breedingPlan);

        return planId;
    }

    @Override
    public List<BreedingPlan> queryBreedingPlanList(String enterpriseId, Integer breedingYear, String cropType) {
        LambdaQueryWrapper<BreedingPlan> wrapper = new LambdaQueryWrapper<>();

        // 企业ID筛选
        if (StringUtils.isNotEmpty(enterpriseId)) {
            wrapper.eq(BreedingPlan::getEnterpriseId, enterpriseId);
        }

        // 育种年度筛选
        if (breedingYear != null) {
            wrapper.eq(BreedingPlan::getBreedingYear, breedingYear);
        }

        // 作物类型筛选
        if (StringUtils.isNotEmpty(cropType)) {
            wrapper.eq(BreedingPlan::getCropType, cropType);
        }

        // 按创建时间倒序排列
        wrapper.orderByDesc(BreedingPlan::getCreateTime);

        return list(wrapper);
    }

    @Override
    public BreedingPlan queryByPlanId(String planId) {
        return getById(planId);
    }

    @Override
    public boolean editBreedingPlan(BreedingPlan breedingPlan) {
        // 查询现有计划信息
        BreedingPlan existingPlan = queryByPlanId(breedingPlan.getPlanId());
        if (existingPlan == null) {
            throw new ServiceException("育种计划不存在");
        }

        // 如果批次ID变更,检查新的批次ID是否已存在
        if (!existingPlan.getBatchId().equals(breedingPlan.getBatchId())) {
            LambdaQueryWrapper<BreedingPlan> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(BreedingPlan::getBatchId, breedingPlan.getBatchId());
            if (count(wrapper) > 0) {
                throw new ServiceException("育种批次ID已存在");
            }
        }

        // 设置更新信息
        breedingPlan.setUpdateBy(LoginHelper.getUsername());
        breedingPlan.setUpdateTime(LocalDateTime.now());

        return updateById(breedingPlan);
    }

    @Override
    public boolean removeBreedingPlan(String planId) {
        return removeById(planId);
    }
}
