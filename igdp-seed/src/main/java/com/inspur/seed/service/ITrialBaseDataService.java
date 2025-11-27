package com.inspur.seed.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.seed.domain.dto.TrialBaseDataDTO;
import com.inspur.seed.domain.entity.TrialBaseData;
import com.inspur.seed.domain.vo.TrialBaseDataVO;

import java.util.List;

/**
 * 试验基础数据采集Service接口
 *
 * @author igdp
 * @date 2025-11-26
 */
public interface ITrialBaseDataService extends IService<TrialBaseData> {

    /**
     * 查询试验基础数据采集列表
     *
     * @param dto 查询条件
     * @return 试验基础数据采集列表
     */
    List<TrialBaseDataVO> selectTrialBaseDataList(TrialBaseDataDTO dto);

    /**
     * 查询试验基础数据采集详情
     *
     * @param trialId 试验ID
     * @return 试验基础数据采集详情
     */
    TrialBaseDataVO selectTrialBaseDataById(String trialId);

    /**
     * 新增试验基础数据采集
     *
     * @param dto 试验基础数据采集
     * @return 结果
     */
    int insertTrialBaseData(TrialBaseDataDTO dto);

    /**
     * 修改试验基础数据采集
     *
     * @param dto 试验基础数据采集
     * @return 结果
     */
    int updateTrialBaseData(TrialBaseDataDTO dto);

    /**
     * 批量删除试验基础数据采集
     *
     * @param trialIds 需要删除的试验ID
     * @return 结果
     */
    int deleteTrialBaseDataByIds(String[] trialIds);
}
