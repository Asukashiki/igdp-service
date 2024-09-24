package com.inspur.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.system.domain.SysRoleWorkbenchItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author liyunlong
 * @version 1.0
 * @ClassName SysRoleWorkbenchItemMapper
 * @date 2024/6/11 9:54
 */
public interface SysRoleWorkbenchItemMapper extends BaseMapper<SysRoleWorkbenchItem> {
    /**
     * 根据userId获取工作台itemId列表
     * @param userId 用户id
     * @return 集合
     * */
    List<Long> selectItemIdByUserId(@Param("userId") String userId);
}
