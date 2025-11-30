package com.inspur.seed.service;

import com.inspur.seed.domain.TrialBasic;

import java.util.List;

/**
 * 试验基础信息Service接口
 *
 * @author inspur
 */
public interface ITrialBasicService {

    /**
     * 查询试验基础信息列表
     *
     * @param trialBasic 查询条件
     * @return 试验列表
     */
    List<TrialBasic> selectTrialBasicList(TrialBasic trialBasic);

    /**
     * 根据试验ID查询试验详情（含关联地块）
     *
     * @param trialId 试验ID
     * @return 试验信息
     */
    TrialBasic selectTrialBasicById(String trialId);

    /**
     * 新增试验基础信息（含关联地块）
     *
     * @param trialBasic 试验信息
     * @return 试验ID
     */
    String insertTrialBasic(TrialBasic trialBasic);

    /**
     * 修改试验基础信息（含关联地块）
     *
     * @param trialBasic 试验信息
     * @return 影响行数
     */
    int updateTrialBasic(TrialBasic trialBasic);

    /**
     * 批量删除试验基础信息（含关联关系）
     *
     * @param trialIds 试验ID数组
     * @return 影响行数
     */
    int deleteTrialBasicByIds(String[] trialIds);

    /**
     * 获取试验下拉列表
     *
     * @param batchId 批次ID（可选）
     * @return 试验列表
     */
    List<TrialBasic> selectTrialOptions(String batchId);
}
