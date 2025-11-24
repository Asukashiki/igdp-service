package com.inspur.farmland.management.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.farmland.management.bean.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户Mapper接口
 * 
 * @author inspur
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}