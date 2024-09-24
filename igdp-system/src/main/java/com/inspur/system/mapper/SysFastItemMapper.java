package com.inspur.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.system.domain.SysFastItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author liyunlong
 * @version 1.0
 * @ClassName SysFastItemMapper
 * @date 2024/6/14 17:23
 */
@Mapper
public interface SysFastItemMapper extends BaseMapper<SysFastItem> {
    /**
     * 根据用户ID获取快速发起列表
     * @param userId 用户id
     * @return 集合列表
     * */
    List<SysFastItem> selectListByUserId(@Param("userId")String userId);
}
