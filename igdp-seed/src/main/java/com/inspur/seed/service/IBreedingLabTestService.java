package com.inspur.seed.service;

import com.inspur.seed.domain.dto.BreedingLabTestDTO;
import com.inspur.seed.domain.vo.BreedingLabTestVO;

import java.util.List;
import java.util.Map;

/**
 * 实验室测试数据Service接口
 *
 * @author igdp
 * @date 2025-11-29
 */
public interface IBreedingLabTestService {

    /**
     * 查询实验室测试数据列表
     *
     * @param dto 查询条件
     * @return 包含list和total的Map
     */
    Map<String, Object> selectBreedingLabTestList(BreedingLabTestDTO dto);

    /**
     * 查询实验室测试数据详情
     *
     * @param id 主键ID
     * @return 实验室测试数据
     */
    BreedingLabTestVO selectBreedingLabTestById(String id);

    /**
     * 新增实验室测试数据
     *
     * @param dto 实验室测试数据
     * @return 结果
     */
    int insertBreedingLabTest(BreedingLabTestDTO dto);

    /**
     * 修改实验室测试数据
     *
     * @param dto 实验室测试数据
     * @return 结果
     */
    int updateBreedingLabTest(BreedingLabTestDTO dto);

    /**
     * 批量删除实验室测试数据
     *
     * @param ids 主键数组
     * @return 结果
     */
    int deleteBreedingLabTestByIds(String[] ids);
}
