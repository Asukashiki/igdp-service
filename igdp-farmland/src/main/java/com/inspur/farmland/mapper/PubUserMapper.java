package com.inspur.farmland.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.farmland.domain.PubUser;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface PubUserMapper extends BaseMapper<PubUser> {

    @Select("select count(*) from bsp.pub_user where account = #{account}")
    int CheckUser(@Param("account")String account);

}
