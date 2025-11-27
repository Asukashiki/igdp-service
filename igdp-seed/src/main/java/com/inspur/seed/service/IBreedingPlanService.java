package com.inspur.seed.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.seed.domain.BreedingPlan;

import java.util.List;

/**
 * 育种计划服务接口
 *
 * @author system
 */
public interface IBreedingPlanService extends IService<BreedingPlan> {

    /**
     * 新增育种计划
     *
     * @param breedingPlan 育种计划信息
     * @return 计划ID
     */
    String addBreedingPlan(BreedingPlan breedingPlan);

    /**
     * 查询育种计划列表
     *
     * @param enterpriseId 企业ID
     * @param breedingYear 育种年度
     * @param cropType 作物类型
     * @return 育种计划列表
     */
    List<BreedingPlan> queryBreedingPlanList(String enterpriseId, Integer breedingYear, String cropType);

    /**
     * 根据计划ID查询育种计划详情
     *
     * @param planId 计划ID
     * @return 育种计划详情
     */
    BreedingPlan queryByPlanId(String planId);

    /**
     * 编辑育种计划
     *
     * @param breedingPlan 育种计划信息
     * @return 更新结果
     */
    boolean editBreedingPlan(BreedingPlan breedingPlan);

    /**
     * 删除育种计划
     *
     * @param planId 计划ID
     * @return 删除结果
     */
    boolean removeBreedingPlan(String planId);
}
