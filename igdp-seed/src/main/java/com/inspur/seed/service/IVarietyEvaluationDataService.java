package com.inspur.seed.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.seed.domain.dto.VarietyEvaluationDataDTO;
import com.inspur.seed.domain.entity.VarietyEvaluationData;
import com.inspur.seed.domain.vo.VarietyEvaluationDataVO;

import java.util.List;

/**
 * 品种评估数据采集Service接口
 *
 * @author igdp
 * @date 2025-11-26
 */
public interface IVarietyEvaluationDataService extends IService<VarietyEvaluationData> {

    /**
     * 查询品种评估数据列表
     *
     * @param dto 查询条件
     * @return 列表
     */
    List<VarietyEvaluationDataVO> selectVarietyEvaluationDataList(VarietyEvaluationDataDTO dto);

    /**
     * 查询品种评估数据详情
     *
     * @param dataId 数据ID
     * @return 详情
     */
    VarietyEvaluationDataVO selectVarietyEvaluationDataById(String dataId);

    /**
     * 新增品种评估数据
     *
     * @param dto 数据
     * @return 结果
     */
    int insertVarietyEvaluationData(VarietyEvaluationDataDTO dto);

    /**
     * 修改品种评估数据
     *
     * @param dto 数据
     * @return 结果
     */
    int updateVarietyEvaluationData(VarietyEvaluationDataDTO dto);

    /**
     * 批量删除品种评估数据
     *
     * @param dataIds 需要删除的数据ID
     * @return 结果
     */
    int deleteVarietyEvaluationDataByIds(String[] dataIds);
}
