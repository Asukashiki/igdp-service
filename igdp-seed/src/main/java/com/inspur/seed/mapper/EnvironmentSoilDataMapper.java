package com.inspur.seed.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.seed.domain.entity.EnvironmentSoilData;
import org.apache.ibatis.annotations.Mapper;

/**
 * 环境与土壤属性数据采集Mapper接口
 *
 * @author igdp
 * @date 2025-11-26
 */
@Mapper
public interface EnvironmentSoilDataMapper extends BaseMapper<EnvironmentSoilData> {

}
