package com.inspur.seed.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.seed.domain.EnvironmentData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 环境属性数据Mapper接口
 *
 * @author inspur
 */
@Mapper
public interface EnvironmentDataMapper extends BaseMapper<EnvironmentData> {

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
    EnvironmentData selectEnvironmentDataById(@Param("envId") String envId);
}
