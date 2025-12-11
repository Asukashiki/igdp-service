package com.inspur.agriculture.input.mapper.oauth;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.agriculture.input.domain.oauth.PubRegion;
import org.apache.ibatis.annotations.Select;

public interface PubRegionMapper extends BaseMapper<PubRegion> {


    @Select("select * from oauth2_bsp.pub_region where region_code=#{regionCode}")
    PubRegion selectByRegionCode(String regionCode);

}
