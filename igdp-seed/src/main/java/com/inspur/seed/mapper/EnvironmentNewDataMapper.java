package com.inspur.seed.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.seed.domain.EnvironmentNewData;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 环境监测新数据Mapper接口
 * Environment New Data Mapper Interface
 *
 * @author inspur
 */
@Mapper
public interface EnvironmentNewDataMapper extends BaseMapper<EnvironmentNewData> {

    /**
     * 查询环境监测数据列表
     * Query environment new data list
     *
     * @param environmentNewData 查询条件
     * @return 环境数据列表
     */
    List<EnvironmentNewData> selectEnvironmentNewDataList(EnvironmentNewData environmentNewData);

    /**
     * 根据ID查询环境数据详情
     * Query environment data by ID
     *
     * @param envRecordId 环境记录ID
     * @return 环境数据
     */
    EnvironmentNewData selectEnvironmentNewDataById(String envRecordId);
}
