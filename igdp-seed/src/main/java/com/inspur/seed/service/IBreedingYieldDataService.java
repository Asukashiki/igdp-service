package com.inspur.seed.service;

import com.inspur.seed.domain.dto.BreedingYieldDataDTO;
import com.inspur.seed.domain.vo.BreedingYieldDataVO;

import java.util.List;

/**
 * 产量数据Service接口
 *
 * @author igdp
 * @date 2025-11-29
 */
public interface IBreedingYieldDataService {

    /**
     * 查询产量数据列表
     *
     * @param dto 查询条件
     * @return 产量数据列表
     */
    List<BreedingYieldDataVO> selectBreedingYieldDataList(BreedingYieldDataDTO dto);

    /**
     * 查询产量数据详情
     *
     * @param id 主键ID
     * @return 产量数据
     */
    BreedingYieldDataVO selectBreedingYieldDataById(String id);

    /**
     * 新增产量数据
     *
     * @param dto 产量数据
     * @return 结果
     */
    int insertBreedingYieldData(BreedingYieldDataDTO dto);

    /**
     * 修改产量数据
     *
     * @param dto 产量数据
     * @return 结果
     */
    int updateBreedingYieldData(BreedingYieldDataDTO dto);

    /**
     * 批量删除产量数据
     *
     * @param ids 主键数组
     * @return 结果
     */
    int deleteBreedingYieldDataByIds(String[] ids);

    /**
     * 提交审核
     *
     * @param id 主键ID
     * @return 结果
     */
    int submitForReview(String id);

    /**
     * 作废产量数据
     *
     * @param id 主键ID
     * @param remark 作废原因
     * @return 结果
     */
    int voidYieldData(String id, String remark);
}
