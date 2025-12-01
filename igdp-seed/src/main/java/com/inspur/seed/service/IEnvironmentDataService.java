package com.inspur.seed.service;

import com.inspur.seed.domain.EnvironmentData;

import java.util.List;

/**
 * 环境属性数据Service接口
 *
 * @author inspur
 */
public interface IEnvironmentDataService {

    /**
     * 查询环境属性数据列表
     *
     * @param environmentData 查询条件
     * @return 环境数据列表
     */
    List<EnvironmentData> selectEnvironmentDataList(EnvironmentData environmentData);

    /**
     * 根据ID查询环境数据详情
     *
     * @param envId 环境数据ID
     * @return 环境数据
     */
    EnvironmentData selectEnvironmentDataById(String envId);

    /**
     * 新增环境属性数据
     *
     * @param environmentData 环境数据
     * @return 环境数据ID
     */
    String insertEnvironmentData(EnvironmentData environmentData);

    /**
     * 修改环境属性数据
     *
     * @param environmentData 环境数据
     * @return 影响行数
     */
    int updateEnvironmentData(EnvironmentData environmentData);

    /**
     * 批量删除环境属性数据
     *
     * @param envIds 环境数据ID数组
     * @return 影响行数
     */
    int deleteEnvironmentDataByIds(String[] envIds);
}
