package com.inspur.farmland.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.farmland.domain.FarmerInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * 农民信息Mapper接口
 *
 * @author inspur
 */
@Mapper
public interface FarmerInfoMapper extends BaseMapper<FarmerInfo> {

}
