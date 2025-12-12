package com.inspur.agriculture.input.service;

import com.inspur.agriculture.input.domain.AgriInput;

import java.util.List;
import java.util.Map;

/**
 * 农业投入品服务接口
 *
 * @author igdp
 */
public interface IAgriInputService {

    /**
     * 查询投入品列表
     *
     * @param agriInput 投入品查询条件
     * @return 投入品列表
     */
    List<AgriInput> selectInputList(AgriInput agriInput);

    /**
     * 根据ID查询投入品详情
     *
     * @param inputId 投入品ID
     * @return 投入品信息
     */
    AgriInput selectInputById(Long inputId);

    /**
     * 新增投入品（自动生成业务ID）
     *
     * @param agriInput 投入品信息
     * @return 新增结果（影响行数）
     */
    int insertInput(AgriInput agriInput);

    /**
     * 修改投入品
     *
     * @param agriInput 投入品信息
     * @return 修改结果（影响行数）
     */
    int updateInput(AgriInput agriInput);

    /**
     * 批量删除投入品
     *
     * @param inputIds 需要删除的投入品ID数组
     * @return 删除结果（影响行数）
     */
    int deleteInputByIds(Long[] inputIds);

    /**
     * 删除投入品（逻辑删除）
     *
     * @param inputId 投入品ID
     * @return 删除结果（影响行数）
     */
    int deleteInputById(Long inputId);

    /**
     * 获取投入品统计信息
     *
     * @return 统计信息
     */
    Map<String, Object> getInputStatistics();
}